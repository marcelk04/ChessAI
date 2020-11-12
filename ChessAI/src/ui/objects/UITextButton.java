package ui.objects;

import java.awt.Graphics;

import ui.UIUtils;

public class UITextButton extends UIButton {
	protected String text;

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