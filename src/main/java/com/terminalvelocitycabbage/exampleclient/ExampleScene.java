package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.DirectionalLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.SpotLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.Attenuation;
import com.terminalvelocitycabbage.engine.client.renderer.model.AnimatedModel;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.renderer.model.loader.AnimatedModelLoader;
import com.terminalvelocitycabbage.engine.client.renderer.scenes.Scene;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.debug.Log;
import net.dumbcode.studio.animation.events.AnimationEventRegister;
import net.dumbcode.studio.animation.instance.ModelAnimationHandler;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class ExampleScene extends Scene {

	Texture texture;
	boolean animation = false;

	@Override
	public void init() {

		//Load gerald model to a Model object from dcm file
		AnimatedModel model = GameClient.loadModel();
		if (new File("animation.dca").exists()) {
			model.animations.put("animation", GameClient.loadAnimation());
			animation = true;
		}
		texture = GameClient.loadTexture();

		model.setMaterial(Material.builder().texture(texture).build());
		//Create a game object from the model loaded and add the game object to the list of active objects
		ModeledGameObject modelObject = objectHandler.add("model", new ModeledGameObject(model));
		objectHandler.getObject("model").move(0F, 0F, -30F);
		if (animation) {
			model.startAnimation("animation", true);
		}
		//Add animation event listener here
		model.handler.setSrc(modelObject);
		//AnimationEventRegister.registerEvent("event", (data, src) -> Log.info(data + ", " + src));

		//bind all Game Objects
		for (ModeledGameObject gameObject : objectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.bind();
		}

		//Create some light
		Attenuation plAttenuation = new Attenuation(0.0f, 0.0f, 1.0f);
		objectHandler.add("blueLight", new PointLight(new Vector3f(0, 2, -0.5f), new Vector3f(0,0,1), 1.0f, plAttenuation));
		objectHandler.add("whiteLight", new PointLight(new Vector3f(0, 4, -0.5f), new Vector3f(1,1,1), 1.0f, plAttenuation));
		Attenuation slAttenuation = new Attenuation(0.0f, 0.0f, 0.02f);
		objectHandler.add("redSpotLight", new SpotLight(new Vector3f(0, 2, 0), new Vector3f(1, 0, 0), 1.0f, slAttenuation, new Vector3f(0, 1, 0), 140));
		objectHandler.add("sun", new DirectionalLight(new Vector3f(-0.68f, 0.55f, 0.42f), new Vector4f(1, 1, 0.5f, 1), 1.0f));
	}

	@Override
	public void update(float deltaTime) {

		//Move around the point lights
		objectHandler.getObject("blueLight").move(0, (float)Math.sin(glfwGetTime())/10, 0);
		objectHandler.getObject("whiteLight").move(0, (float)Math.cos(glfwGetTime())/8, 0);

		//Animate the model
		ModeledGameObject model = objectHandler.getObject("model");
		ModelAnimationHandler modelHandler = ((AnimatedModel) model.getModel()).handler;
		if (animation) {
			modelHandler.animate(deltaTime / 50F);
		}
		model.queueUpdate();
	}

	@Override
	public void destroy() {
		//Cleanup
		for (ModeledGameObject gameObject : objectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.destroy();
		}
	}
}
