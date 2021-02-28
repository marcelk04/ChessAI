package com.file;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ColorSaver {
	private final String configPath;
	private static final int light_color = 0, dark_color = 1, move_color = 2, last_move_color = 3;
	private Color[] colors;

	public ColorSaver(final String configPath) {
		this.configPath = configPath;
		this.colors = new Color[4];

		loadColors();
	}

	public void loadColors() {
		try (BufferedReader br = new BufferedReader(new FileReader(configPath))) {
			int i = 0;
			String current;

			while ((current = br.readLine()) != null) {
				colors[i++] = new Color(Integer.parseInt(current));
			}
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
			System.err.println(
					"There was a problem loading the colors! Please make sure the res folder has not been changed manually.");
		}
	}

	public void saveColors() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(configPath))) {
			for (int i = 0; i < colors.length; i++) {
				bw.write(colors[i].getRGB() + "");

				if (i < colors.length - 1)
					bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(
					"There was a problem saving the colors! Please make sure the res folder has not been changed manually.");
		}
	}

	// ===== Getters ===== \\
	public Color getLightColor() {
		return colors[light_color];
	}

	public Color getDarkColor() {
		return colors[dark_color];
	}

	public Color getMoveColor() {
		return colors[move_color];
	}

	public Color getLastMoveColor() {
		return colors[last_move_color];
	}

	// ===== Setters ===== \\
	public void setLightColor(Color lightColor) {
		colors[light_color] = lightColor;
	}

	public void setDarkColor(Color darkColor) {
		colors[dark_color] = darkColor;
	}

	public void setMoveColor(Color moveColor) {
		colors[move_color] = moveColor;
	}

	public void setLastMoveColor(Color lastMoveColor) {
		colors[last_move_color] = lastMoveColor;
	}
}