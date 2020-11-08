package ui.objects;

import java.awt.Font;
import java.awt.Graphics;

import main.Utils;
import ui.UIUtils;

public class UILabel extends UIObject {
	private String text;
	private int textX, textY;

	public UILabel(String text) {
		super();
		this.text = text;
	}

	@Override
	public void tick() {
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
			Utils.drawString(g, text, textX, textY, true, textColor, font);
		}
	}

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
		this.text = text;
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