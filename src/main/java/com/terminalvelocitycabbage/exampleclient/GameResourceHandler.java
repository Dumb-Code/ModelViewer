package com.terminalvelocitycabbage.exampleclient;

import com.terminalvelocitycabbage.engine.client.resources.ClassLoaderResourceManager;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;

public class GameResourceHandler {
	public static final ResourceManager SHADER = new ClassLoaderResourceManager(ClassLoader.getSystemClassLoader(), "assets", "shaders");
}
