package com.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	/**
	 * Loads a BufferedImage from a file path.
	 * 
	 * @param path the path of the file.
	 * @return the loaded BufferedImage.
	 */
	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(ImageLoader.class.getResource(path));
		} catch (IOException e) {
			System.err.println(
					"Images could not be loaded! Please check if the file \"sheet.png\" is intact! Exception:");
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
}