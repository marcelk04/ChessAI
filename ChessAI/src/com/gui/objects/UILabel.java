package com.gui.objects;

import java.awt.Graphics;

import com.gui.UIUtils;

/**
 * The UILabel is intended to be used for displaying a single line of text on
 * the screen, for example as a title. It has no way of getting user input.
 * 
 * @author DefensivLord
 */
public class UILabel extends UIObject {
	/**
	 * The displayed text.
	 */
	private String text;

	/**
	 * The position of the text used for displaying it.
	 */
	private int textX, textY;

	/**
	 * The constructor for instances of the class UILabel.
	 * 
	 * @param text the displayed text.
	 */
	public UILabel(String text) {
		setText(text);
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			drawBackground();
			UIUtils.drawString(g, text, textX, textY, true, textColor, font);
			drawBorder();
		}
	}

	/**
	 * Calculates the correct position of the text by taking in the text, font,
	 * position and horizontal alignment.
	 */
	private void calculateTextPos() {
		double textWidth = UIUtils.getStringWidth(text, font);

		switch (horizontalAlignment) {
		case LEFT:
			textX = (int) Math.round(x + textWidth / 2);
			break;

		case CENTER:
			textX = x + width / 2;
			break;

		case RIGHT:
			textX = (int) Math.round(x + width - textWidth / 2);
			break;
		}

		textY = y + height / 2;
	}

	@Override
	protected void propertyChanged() {
		calculateTextPos();
		repaint();
	}

	// ===== Getter ===== \\
	public String getText() {
		return text;
	}

	// ===== Setter ===== \\
	public void setText(String text) {
		if (this.text == null || !this.text.equals(text)) {
			this.text = text != null ? text : "";
			propertyChanged();
		}
	}
}