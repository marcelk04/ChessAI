package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Utils {
	public static boolean inRange(int var, int min, int max) {
		return var >= min && var <= max;
	}

	public static int clamp(int var, int min, int max) {
		if (var <= min)
			return min;
		else if (var >= max)
			return max;
		else
			return var;
	}

	public static void drawString(Graphics g, String text, int xPos, int yPos, boolean centered, Color c, Font font) {
		g.setColor(c);
		g.setFont(font);

		int x = xPos;
		int y = yPos;

		if (centered) {
			FontMetrics fm = g.getFontMetrics(font);

			x = xPos - fm.stringWidth(text) / 2;
			y = (yPos - fm.getHeight() / 2) + fm.getAscent();

//			System.out.println("Width of " + text + ": " + fm.stringWidth(text));
//			System.out.println("Height of " + text + ": " + fm.getHeight());
		}

		g.drawString(text, x, y);
	}
}