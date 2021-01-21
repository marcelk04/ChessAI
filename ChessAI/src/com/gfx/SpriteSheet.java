package com.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	private BufferedImage sheet;

	/**
	 * Creates a new SpriteSheet with a single BufferedImage.
	 * 
	 * @param sheet the image of the sheet.
	 */
	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}

	/**
	 * Crops out a smaller part of the sheet and returns it as a BufferedImage.
	 * 
	 * @param x      the x coordinate of the sub image.
	 * @param y      the y coordinate of the sub image.
	 * @param width  the width of the sub image.
	 * @param height the height of the sub image.
	 * @return the cropped image.
	 */
	public BufferedImage crop(int x, int y, int width, int height) {
		return sheet.getSubimage(x, y, width, height);
	}
}