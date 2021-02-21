package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.input.KeyBind;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.debug.Log;

import static org.lwjgl.glfw.GLFW.*;

public class GameInputHandler extends InputHandler {

	public static KeyBind CLOSE;

	public static KeyBind FORWARD;
	private boolean moveForward;
	public static KeyBind BACKWARDS;
	private boolean moveBackward;
	public static KeyBind LEFT;
	private boolean moveLeft;
	public static KeyBind RIGHT;
	private boolean moveRight;
	public static KeyBind UP;
	private boolean moveUp;
	public static KeyBind DOWN;
	private boolean moveDown;

	public static KeyBind RELOAD_TEXTURE;
	private boolean reloadTexture;
	public static KeyBind FINISH_LOOPING;

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
		FINISH_LOOPING = new KeyBind(GLFW_KEY_L);
		RELOAD_TEXTURE = new KeyBind(GLFW_KEY_R, KeyBind.ANY, GLFW_RELEASE, KeyBind.NONE);
	}

	@Override
	public void processInput(KeyBind keyBind) {

		//Escape closes the program by telling glfw that it should close
		if (keyBind.equalsKeyAndAction(CLOSE)) {
			setFocus(false);
			glfwSetWindowShouldClose(keyBind.getWindow(), true);
			return;
		}

		if (keyBind.equalsKeyAndAction(RELOAD_TEXTURE)) {
			reloadTexture = true;
			Log.info("Reloading Texture...");
		}

		//Process movement inputs
		moveForward = FORWARD.isKeyPressed();
		moveBackward = BACKWARDS.isKeyPressed();
		moveLeft = LEFT.isKeyPressed();
		moveRight = RIGHT.isKeyPressed();
		moveDown = DOWN.isKeyPressed();
		moveUp = UP.isKeyPressed();
	}

	public boolean moveForward() {
		return moveForward;
	}

	public boolean moveBackward() {
		return moveBackward;
	}

	public boolean moveLeft() {
		return moveLeft;
	}

	public boolean moveRight() {
		return moveRight;
	}

	public boolean moveUp() {
		return moveUp;
	}

	public boolean moveDown() {
		return moveDown;
	}

	public boolean reloadTexture() {
		return reloadTexture;
	}

	public void markReloaded() {
		reloadTexture = false;
	}
}
