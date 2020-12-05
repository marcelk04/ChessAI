package ui.objects;

import java.awt.Graphics;

import ui.UIUtils;

/**
 * The class UITextButton is a simple button which displays a String.
 * 
 * @author DefensivLord
 */
public class UITextButton extends UIButton {
	/**
	 * The text to be displayed.
	 */
	protected String text;

	/**
	 * The default constructor for instances of the class UITextButton.
	 * 
	 * @param text the text to be displayed.
	 */
	public UITextButton(String text) {
		this.text = text;
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
			UIUtils.drawString(g, text, x + width / 2, y + height / 2, true, textColor, font);
		}
	}

	// ===== Getters ===== \\
	public String getText() {
		return text;
	}

	// ===== Setters ===== \\
	public void setText(String text) {
		this.text = text;
	}
}