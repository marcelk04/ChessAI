package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class UIUtils {
	private static final FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);

	public static void drawString(Graphics g, String text, int xPos, int yPos, boolean centered, Color c, Font font) {
		g.setColor(c);
		g.setFont(font);

		int x = xPos;
		int y = yPos;

		if (centered) {

			FontMetrics fm = g.getFontMetrics(font);

			x = xPos - fm.stringWidth(text) / 2;
			y = (yPos - fm.getHeight() / 2) + fm.getAscent();
		}

		g.drawString(text, x, y);
	}

	public static double getStringWidth(String text, Font font) {
		return font.getStringBounds(text, frc).getWidth();
	}

	public static double getStringHeight(String text, Font font) {
		return font.getStringBounds(text, frc).getHeight();
	}

	public static boolean contains(int x, int y, int width, int height, int pointX, int pointY) {
		if (x <= pointX && pointX <= x + width && y <= pointY && pointY <= y + height)
			return true;
		return false;
	}

	public static String shortenText(String text, double length, Font font) {
		double currentTextWidth = getStringWidth(text, font);
		if (currentTextWidth <= length) {
			return text;
		} else {
			while (getStringWidth(text, font) > length && text != "") {
				text = text.substring(0, text.length() - 2);
			}
			return text;
		}
	}
}