package ui.objects;

import java.awt.Font;
import java.awt.Graphics;

import ui.UIUtils;

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
			if (background != null) {
				g.setColor(background);
				g.fillRect(x, y, width, height);
			}
			if (border != null) {
				g.setColor(border);
				g.drawRect(x, y, width, height);
			}
			UIUtils.drawString(g, text, textX, textY, true, textColor, font);
		}
	}

	/**
	 * Calculates the correct position of the text by taking in the text, font, position and
	 * horizontal alignment.
	 */
	private void calculateTextPos() {
		double textWidth = UIUtils.getStringWidth(text, font);

		switch (horizontalAlignment) {
		case LEFT:
			textX = (int) (x + textWidth / 2);
			break;

		case CENTER:
			textX = x + width / 2;
			break;

		case RIGHT:
			textX = (int) (x + width - textWidth / 2);
			break;
		}

		textY = y + height / 2;
	}

	// ===== Getter ===== \\
	public String getText() {
		return text;
	}

	// ===== Setter ===== \\
	public void setText(String text) {
		this.text = text != null ? text : "";
		calculateTextPos();
	}

	@Override
	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
		calculateTextPos();
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		calculateTextPos();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		calculateTextPos();
	}
}