package com.davidalmarinho.game.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.davidalmarinho.game.Constants;
import com.davidalmarinho.game.Drop;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drop";
		config.width = Constants.WIDTH;
		config.height = Constants.HEIGHT;
		config.x = -1;
		config.y = -1;
		new LwjglApplication(new Drop(), config);
	}
}
