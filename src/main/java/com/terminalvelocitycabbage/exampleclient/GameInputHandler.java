package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.input.KeyBind;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameInputHandler extends InputHandler {

	public static KeyBind CLOSE;

	public static KeyBind FORWARD;
	public static KeyBind BACKWARDS;
	public static KeyBind LEFT;
	public static KeyBind RIGHT;
	public static KeyBind UP;
	public static KeyBind DOWN;

	public static KeyBind RELOAD_TEXTURE;

	Vector3f cameraMoveVector = new Vector3f(0);
	boolean reloadTexture = false;

	@Override
	public void init(Window window) {
		super.init(window);
		CLOSE = new KeyBind(GLFW_KEY_ESCAPE, KeyBind.ANY, GLFW_RELEASE, KeyBind.NONE);
		FORWARD = new KeyBind(GLFW_KEY_W);
		BACKWARDS = new KeyBind(GLFW_KEY_S);
		LEFT = new KeyBind(GLFW_KEY_A);
		RIGHT = new KeyBind(GLFW_KEY_D);
		UP = new KeyBind(GLFW_KEY_SPACE);
		DOWN = new KeyBind(GLFW_KEY_LEFT_SHIFT);
		RELOAD_TEXTURE = new KeyBind(GLFW_KEY_T, KeyBind.ANY, GLFW_RELEASE, KeyBind.NONE);
	}

	@Override
	public void processInput(KeyBind keyBind) {

		reloadTexture = false;

		//Escape closes the program by telling glfw that it should close
		if (keyBind.equalsKeyAndAction(CLOSE)) {
			setFocus(false);
			glfwSetWindowShouldClose(keyBind.getWindow(), true);
			return;
		}

		if (keyBind.equalsKeyAndAction(RELOAD_TEXTURE)) {
			reloadTexture = true;
		}

		//Process movement inputs
		cameraMoveVector.set(0, 0, 0);
		if (keyBind.isKeyPressed(FORWARD)) {
			cameraMoveVector.z--;
		}
		if (keyBind.isKeyPressed(BACKWARDS)) {
			cameraMoveVector.z++;
		}
		if (keyBind.isKeyPressed(LEFT)) {
			cameraMoveVector.x--;
		}
		if (keyBind.isKeyPressed(RIGHT)) {
			cameraMoveVector.x++;
		}
		if (keyBind.isKeyPressed(DOWN)) {
			cameraMoveVector.y--;
		}
		if (keyBind.isKeyPressed(UP)) {
			cameraMoveVector.y++;
		}
	}

	public Vector3f getCameraPositionMoveVector() {
		return cameraMoveVector;
	}

	public boolean reloadTexture() {
		return reloadTexture;
	}
}
