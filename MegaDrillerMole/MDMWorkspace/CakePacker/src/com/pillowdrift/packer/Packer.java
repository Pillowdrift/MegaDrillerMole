package com.pillowdrift.packer;

import com.badlogic.gdx.tools.imagepacker.TexturePacker;
import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;

public class Packer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String inputDir = "data/graphics";
		String outputDir01 = "../DrillerGame/data/graphics";
		String outputDir02 = "../DrillerGameAndroid/assets/data/graphics";
		
		
		// TODO Auto-generated method stub
		Settings packerSettings = new Settings();
		packerSettings.maxWidth = 1024;
		packerSettings.maxHeight = 1024;
		packerSettings.incremental = false;
		packerSettings.padding = 2; //Can probably disable this if I figure out how to do no texture filtering ^^
		packerSettings.edgePadding = true;
		packerSettings.stripWhitespace = false;
		TexturePacker.process(packerSettings, inputDir, outputDir01);
		TexturePacker.process(packerSettings, inputDir, outputDir02);
		
		System.out.print("Done..");
	}

}
