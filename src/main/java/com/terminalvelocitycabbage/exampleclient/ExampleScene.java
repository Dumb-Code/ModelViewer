package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.DirectionalLight;
import com.terminalvelocitycabbage.engine.client.renderer.model.AnimatedModel;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.renderer.scenes.Scene;
import com.terminalvelocitycabbage.engine.debug.Log;
import net.dumbcode.studio.animation.events.AnimationEventRegister;
import net.dumbcode.studio.animation.instance.ModelAnimationHandler;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;

public class ExampleScene extends Scene {

	Texture texture;
	boolean animation = false;

	public ExampleScene(Camera camera) {
		super(camera);
	}

	@Override
	public void init() {

		//Load gerald model to a Model object from dcm file
		AnimatedModel model = GameClient.loadModel();

		//Load Animation
		if (new File("animation.dca").exists()) {
			model.animations.put("animation", GameClient.loadAnimation());
			animation = true;
		} else {
			Log.warn("No animation found, not adding one.");
		}

		//Load Texture
		if (new File("texture.png").exists()) {
			texture = GameClient.loadTexture();
			model.setMaterial(Material.builder().texture(texture).build());
		} else {
			Log.warn("No texture found, using default material");
			model.setMaterial(Material.builder().build());
		}

		//Create a game object from the model loaded and add the game object to the list of active objects
		ModeledGameObject modelObject = objectHandler.add("model", new ModeledGameObject(model));
		objectHandler.getObject("model").move(0F, 0F, -30F);
		if (animation) {
			model.startAnimation("animation").loopForever();
		}

		//Add animation event listener here and just print the data
		model.handler.setSrc(modelObject);
		AnimationEventRegister.registerEvent("event", (data, src) -> Log.info(data + ", " + src));

		//Bind all GameObjects
		for (ModeledGameObject gameObject : objectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.bind();
		}

		//Create some light
		objectHandler.add("sun", new DirectionalLight(new Vector3f(-0.68f, 0.55f, 0.42f), new Vector4f(1, 1, 0.5f, 1), 1.0f));
	}

	@Override
	public void update(float deltaTime) {

		//Animate the model
		ModeledGameObject model = objectHandler.getObject("model");
		ModelAnimationHandler modelHandler = ((AnimatedModel) model.getModel()).handler;
		if (animation) {
			modelHandler.animate(deltaTime / 1000F);
		}
		model.queueUpdate();
	}

	@Override
	public void destroy() {
		for (ModeledGameObject gameObject : objectHandler.getAllOfType(ModeledGameObject.class)) {
			gameObject.destroy();
		}
	}
}
