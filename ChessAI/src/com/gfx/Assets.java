package com.gfx;

import java.awt.image.BufferedImage;

public class Assets {
	static {
		Assets.init();
	}

	private static final int WIDTH = 80, HEIGHT = 80;

	// white pieces
	public static BufferedImage white_king, white_queen, white_rook, white_bishop, white_knight, white_pawn;

	// black pieces
	public static BufferedImage black_king, black_queen, black_rook, black_bishop, black_knight, black_pawn;
	
	public static BufferedImage pencil;

	/**
	 * Initializes all the images so they can be rendered by the pieces.
	 */
	public static void init() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/sheet.png"));

		white_king = sheet.crop(0, 0, WIDTH, HEIGHT);
		white_queen = sheet.crop(WIDTH, 0, WIDTH, HEIGHT);
		white_rook = sheet.crop(WIDTH * 2, 0, WIDTH, HEIGHT);
		white_bishop = sheet.crop(WIDTH * 3, 0, WIDTH, HEIGHT);
		white_knight = sheet.crop(WIDTH * 4, 0, WIDTH, HEIGHT);
		white_pawn = sheet.crop(WIDTH * 5, 0, WIDTH, HEIGHT);

		black_king = sheet.crop(0, HEIGHT, WIDTH, HEIGHT);
		black_queen = sheet.crop(WIDTH, HEIGHT, WIDTH, HEIGHT);
		black_rook = sheet.crop(WIDTH * 2, HEIGHT, WIDTH, HEIGHT);
		black_bishop = sheet.crop(WIDTH * 3, HEIGHT, WIDTH, HEIGHT);
		black_knight = sheet.crop(WIDTH * 4, HEIGHT, WIDTH, HEIGHT);
		black_pawn = sheet.crop(WIDTH * 5, HEIGHT, WIDTH, HEIGHT);
		
		pencil = ImageLoader.loadImage("/pencil.png");
	}
}