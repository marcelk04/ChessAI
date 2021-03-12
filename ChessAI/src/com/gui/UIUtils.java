package com.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
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

	public static int getStringWidth(String text, Font font, Graphics g) {
		return g.getFontMetrics(font).stringWidth(text);
	}

	public static int getStringHeight(Font font, Graphics g) {
		return g.getFontMetrics(font).getHeight();
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
				text = text.substring(0, text.length() - 1);
			}
			return text;
		}
	}

	public static String shortenText(String text, double length, Font font, Graphics g) {
		double currentTextWidth = getStringWidth(text, font, g);
		if (currentTextWidth <= length) {
			return text;
		} else {
			while (getStringWidth(text, font, g) > length && text != "") {
				text = text.substring(0, text.length() - 1);
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

	public static Color mixColors(Color a, Color b) {
		int red = (a.getRed() + b.getRed()) / 2;
		int green = (a.getGreen() + b.getGreen()) / 2;
		int blue = (a.getBlue() + b.getBlue()) / 2;
		int alpha = (a.getAlpha() + b.getAlpha()) / 2;
		return new Color(red, green, blue, alpha);
	}

	public static boolean inRange(int var, int min, int max) {
		return var >= min && var <= max;
	}

	public static String[] splitStringWhole(String text, double maxLength, String regex, Font font, Graphics g) {
		if (text == null || text.equals("") || maxLength <= 0 || regex == null || font == null || g == null)
			return new String[0]; // return an empty String array

		String[] splitText = text.split("\n");
		String currentLine;
		StringBuilder sb = new StringBuilder();

		for (String part : splitText) {
			String[] splitPart = part.split(regex);
			currentLine = "";

			for (String word : splitPart) {
				if (currentLine.equals("")) {
					sb.append(word);
					currentLine = word;
				} else if (getStringWidth(word, font, g) >= maxLength
						|| getStringWidth(currentLine + regex + word, font, g) > maxLength) {
					// if the word is to long for the width or the word wouldn't fit with the text
					// in the currentLine
					sb.append("\n").append(word);
					currentLine = word;
				} else {
					sb.append(regex + word);
					currentLine += regex + word;
				}
			}

			sb.append("\n");

		}

		return sb.toString().split("\n");
	}

	public static Rectangle fitImage(Image image, int x, int y, int width, int height, boolean stretchImage) {
		if (stretchImage)
			return new Rectangle(x, y, width, height);

		float originalImageWidth = image.getWidth(null);
		float originalImageHeight = image.getHeight(null);

		float widthDiff = width - originalImageWidth;
		float heightDiff = height - originalImageHeight;

		float factor;

		if (widthDiff < heightDiff)
			factor = widthDiff / originalImageWidth + 1;
		else
			factor = heightDiff / originalImageHeight + 1;

		int imageWidth = Math.round(originalImageWidth * factor);
		int imageHeight = Math.round(originalImageHeight * factor);
		int imageX = Math.round(x + (width - imageWidth) / 2f);
		int imageY = Math.round(y + (height - imageHeight) / 2f);

		return new Rectangle(imageX, imageY, imageWidth, imageHeight);
	}
}