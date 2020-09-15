package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Utils;

public class UISelectionBox extends UIObject {
	private String[] text;
	private Color c;
	private Font font;
	private ClickListener clicker;
	private int index;

	public UISelectionBox(float x, float y, int width, int height, String[] text, Color c, Font font,
			ClickListener clicker) {
		super(x, y, width, height);
		this.text = text;
		this.c = c;
		this.font = font;
		this.clicker = clicker;
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		if (visible)
			Utils.drawString(g, text[index], (int) x + width / 2, (int) y + height / 2, true, c, font);
	}

	@Override
	public void onClick() {
		index++;
		if (index >= text.length)
			index = 0;
		clicker.onClick();
	}

	public int getSelectedIndex() {
		return index;
	}

	public void setClickListener(ClickListener clicker) {
		this.clicker = clicker;
	}
}