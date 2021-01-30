package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.model.AnimatedModel;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.client.state.State;
import com.terminalvelocitycabbage.engine.client.state.StateHandler;
import com.terminalvelocitycabbage.engine.debug.Log;
import net.dumbcode.studio.animation.info.AnimationInfo;
import net.dumbcode.studio.animation.info.AnimationLoader;
import net.dumbcode.studio.model.ModelLoader;
import net.dumbcode.studio.model.RotationOrder;

import java.io.*;

public class GameClient extends ClientBase {

	public static final String ID = "modelviewer";

	private static GameClientRenderer clientRenderer;

	public GameClient() {
		instance = this;
		clientRenderer = new GameClientRenderer(1900, 1000, "DumbCode Studio Model Viewer", 20f);
		init();
		start();
	}

	public static void main(String[] args) {
		new GameClient();
	}

	public static GameClient getInstance() {
		return (GameClient)instance;
	}

	public static GameClientRenderer getClientRenderer() {
		return clientRenderer;
	}

	@Override
	public void init() {
		clientRenderer.init();
	}

	@Override
	public void start() {
		clientRenderer.run();
	}

	public static AnimatedModel loadModel() {
		File file = new File("model.dcm");
		try {
			InputStream is = new FileInputStream(file);
			return new AnimatedModel(ModelLoader.loadModel(is, RotationOrder.XYZ));
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("model not found");
	}

	public static AnimationInfo loadAnimation() {
		File file = new File("animation.dca");
		try {
			InputStream is = new FileInputStream(file);
			return AnimationLoader.loadAnimation(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.warn("animation not found");
		return null;
	}

	public static Texture loadTexture() {
		try {
			return new Texture(new FileInputStream("texture.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("texture not found");
		}
	}
}
