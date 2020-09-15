package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import main.Utils;

public class UITextButton extends UIObject {
	private String text;
	private Color c;
	private Font font;
	private ClickListener clicker;
	private boolean sizeSet = false;

	public UITextButton(float x, float y, int width, int height, String text, Color c, Font font,
			ClickListener clicker) {
		super(x, y, width, height);
		this.text = text;
		this.c = c;
		this.font = font;
		this.clicker = clicker;
		this.sizeSet = true;
	}

	public UITextButton(float x, float y, String text, Color c, Font font, ClickListener clicker) {
		super();
		this.x = x;
		this.y = y;
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
		if (!sizeSet) {
			FontMetrics fm = g.getFontMetrics(font);
			setSize(fm.stringWidth(text), fm.getHeight());
			setPosition(x - width / 2, (y - height / 2) + fm.getAscent());
			sizeSet = true;
		}
		if (visible) {
			Utils.drawString(g, text, (int) x + width / 2, (int) y + height / 2, true, c, font);
		}
	}

	@Override
	public void onClick() {
		if (clicker != null)
			clicker.onClick();
	}
}