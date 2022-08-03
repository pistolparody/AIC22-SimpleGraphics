package com.yolo.simplegraphics;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.yolo.simplegraphics.SimpleGraphics;

import static com.yolo.simplegraphics.SimpleGraphics.sourceFileAddress;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {

		System.out.println("Use right and left arrow keys to change the turn\n"+
							"Use NUM keys to change the agents moving speed");

		int i=0;
		for (i=0;i!=arg.length;i++)
		{
			sourceFileAddress=arg[i];
		}

		if (i==0)
		{
			System.out.println("No server.log files were detected, I am using the default one.");
		}

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("AIC22-SimpleGraphics");
		config.setDecorated(false);
		new Lwjgl3Application(new SimpleGraphics(), config);
	}
}
