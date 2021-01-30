package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.resources.ClassLoaderResourceManager;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;

public class GameResourceHandler {
	public static final ResourceManager MODEL = new ClassLoaderResourceManager(ClassLoader.getSystemClassLoader(), "assets", "models");
	public static final ResourceManager ANIMATION = new ClassLoaderResourceManager(ClassLoader.getSystemClassLoader(), "assets", "animations");
	public static final ResourceManager TEXTURE = new ClassLoaderResourceManager(ClassLoader.getSystemClassLoader(), "assets", "textures");
	public static final ResourceManager SHADER = new ClassLoaderResourceManager(ClassLoader.getSystemClassLoader(), "assets", "shaders");
}
