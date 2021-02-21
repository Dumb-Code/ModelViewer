package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.components.FirstPersonCamera;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderHandler;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderProgram;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import org.joml.Vector3f;

import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.FRAGMENT;
import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.VERTEX;
import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.SHADER;
import static org.lwjgl.opengl.GL20.*;

public class GameClientRenderer extends Renderer {

	private final ShaderHandler shaderHandler = new ShaderHandler();
	private GameInputHandler inputHandler;

	public GameClientRenderer(int width, int height, String title, float tickRate) {
		super(width, height, title, new GameInputHandler(), tickRate);
		getWindow().setvSync(true);
	}

	@Override
	public void init() {
		super.init();

		//Create Shaders
		//Create default shader that is used for textured elements
		shaderHandler.newProgram("default");
		shaderHandler.queueShader("default", VERTEX, SHADER, new Identifier(GameClient.ID, "default.vert"));
		shaderHandler.queueShader("default", FRAGMENT, SHADER, new Identifier(GameClient.ID, "default.frag"));
		shaderHandler.build("default");

		//Store InputHandler
		inputHandler = (GameInputHandler) getWindow().getInputHandler();

		//Create Scenes
		sceneHandler.addScene("example", new ExampleScene(new FirstPersonCamera(60, 0.1f, 1000.0f)));

		//Init the scene
		sceneHandler.loadScene("example");
	}

	@Override
	public void loop() {
		super.loop();

		//Setup the frame for drawing
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		//Move the camera around from client inputs
		FirstPersonCamera firstPersonCamera = (FirstPersonCamera) sceneHandler.getActiveScene().getCamera();
		firstPersonCamera.resetDeltas();
		if (inputHandler.moveForward()) firstPersonCamera.queueMove(0, 0, -1);
		if (inputHandler.moveBackward()) firstPersonCamera.queueMove(0, 0, 1);
		if (inputHandler.moveRight()) firstPersonCamera.queueMove(1, 0, 0);
		if (inputHandler.moveLeft()) firstPersonCamera.queueMove(-1, 0, 0);
		if (inputHandler.moveUp()) firstPersonCamera.queueMove(0, 1, 0);
		if (inputHandler.moveDown()) firstPersonCamera.queueMove(0, -1, 0);
		if (inputHandler.isRightButtonPressed()) {
			firstPersonCamera.queueRotate(inputHandler.getMouseDeltaX(), inputHandler.getMouseDeltaY());
		}
		firstPersonCamera.update(getDeltaTimeInSeconds());
		inputHandler.resetDeltas();

		//Update the texture if requested
		if (inputHandler.reloadTexture()) {
			((ModeledGameObject) sceneHandler.getActiveScene().objectHandler.getObject("model")).getModel().setMaterial(Material.builder().texture(GameClient.loadTexture()).build());
			inputHandler.markReloaded();
		}

		//Update and render the scene
		sceneHandler.update(getDeltaTimeInMillis());
		renderDefault(sceneHandler.getActiveScene().getCamera(), shaderHandler.get("default"));

		//Update the window title with current framerate
		getWindow().setTitle("FPS: " + String.valueOf(getFramerate()).split("\\.")[0] + " (" + getFrameTimeAverageMillis() + "ms)");

		//Send the frame to the GPU
		push();
	}

	@Override
	public void destroy() {
		super.destroy();
		shaderHandler.cleanup();
		canvasHandler.cleanup();
		sceneHandler.cleanup();
	}

	private void renderDefault(Camera camera, ShaderProgram shaderProgram) {

		shaderProgram.enable();

		//Setup Shader Uniforms for the camera and objects
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		shaderProgram.createUniform("normalTransformationMatrix");
		//Setup Shader Uniforms for the lighting
		shaderProgram.createUniform("specularPower");
		shaderProgram.createUniform("ambientLight");
		shaderProgram.createDirectionalLightUniform("directionalLight");
		//Setup Shader Uniform for the material
		shaderProgram.createMaterialUniform("material");

		//Draw whatever changes were pushed
		for (ModeledGameObject gameObject : sceneHandler.getActiveScene().getObjectsOfType(ModeledGameObject.class)) {

			//Update the game object
			gameObject.update();

			//View
			shaderProgram.setUniform("projectionMatrix", camera.getProjectionMatrix());
			shaderProgram.setUniform("modelViewMatrix", gameObject.getModelViewMatrix(camera.getViewMatrix()));
			shaderProgram.setUniform("normalTransformationMatrix", gameObject.getTransformationMatrix());
			//Lighting
			shaderProgram.setUniform("ambientLight", new Vector3f(0.3f, 0.3f, 0.3f));
			shaderProgram.setUniform("specularPower", 10.0f); //Reflected light intensity
			shaderProgram.setUniform("directionalLight", sceneHandler.getActiveScene().objectHandler.getObject("sun"));
			//Material
			shaderProgram.setUniform("material", gameObject.getModel().getMaterial());

			//Render the object to the screen
			gameObject.render();
		}
	}
}
