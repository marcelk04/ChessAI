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

	public static String[] shortenArray(String[] arr, double length, double height, double vgap, Font font) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = shortenText(arr[i], length, font);
		}

		double currentArrayHeight = calculateArrayHeight(arr, vgap, font);

		if (currentArrayHeight <= height) {
			return arr;
		} else {
			int i;
			for (i = arr.length - 1; i >= 0 && calculateArrayHeight(arr, vgap, font) > height; i--) {
				arr[i] = "";
			}

			if (i > 0) {
				String[] newArr = new String[i + 1];
				for (int j = 0; j < newArr.length; j++) {
					newArr[j] = arr[j];
				}

				return newArr;
			} else {
				return new String[0];
			}
		}
	}

	public static double calculateArrayHeight(String[] arr, double vgap, Font font) {
		double totalHeight = 0;
		boolean ranAtLeastOnce = false;

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null && !arr[i].equals("")) {
				totalHeight += getStringHeight(arr[i], font) + vgap;
				ranAtLeastOnce = true;
			}
		}

		if (ranAtLeastOnce)
			totalHeight -= vgap;

		return totalHeight;
	}
}