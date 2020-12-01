package ui.objects;

import java.awt.Graphics;

import chess.move.Position;
import ui.UIUtils;

public class UITextArea extends UIObject {
	private String text;
	private String[] displayText;
	private Position[] displayPositions;
	private int hgap, vgap;

	public UITextArea() {
		this("");
	}

	public UITextArea(String text) {
		this(text, 10, 10);
	}

	public UITextArea(String text, int hgap, int vgap) {
		this.text = text;
		this.hgap = hgap;
		this.vgap = vgap;

		setup();
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			if (background != null) {
				g.setColor(background);
				g.fillRect(x, y, width, height);
			}

			for (int i = 0; i < displayText.length; i++) {
				if (i < displayText.length && displayText[i] != null) {
					UIUtils.drawString(g, displayText[i], displayPositions[i].x, displayPositions[i].y, false,
							textColor, font);
				}
			}

			if (border != null) {
				g.setColor(border);
				g.drawRect(x, y, width, height);
			}
		}
	}

	private void setup() {
		if (text != null && !text.equals("")) {
			double textHeight = UIUtils.getStringHeight(text, font);
			int maxLines = (int) (height / textHeight);
			int maxTextWidth = width - hgap * 2;

			String[] splitText = text.split("\n");
			displayText = new String[Math.min(splitText.length, maxLines)];

			for (int i = 0; i < displayText.length; i++) {
				displayText[i] = UIUtils.shortenText(splitText[i], maxTextWidth, font);
			}

			double totalHeight = (textHeight + vgap) * displayText.length - vgap;

			int yBegin = 0;

			switch (verticalAlignment) {
			case TOP:
				yBegin = y + vgap;
				break;
			case CENTER:
				yBegin = y + (int) Math.round((height - totalHeight) / 2);
				break;
			case BOTTOM:
				yBegin = y + height - (int) Math.round(totalHeight) - vgap;
				break;
			}

			displayPositions = new Position[displayText.length];
			for (int i = 0; i < displayText.length; i++) {
				int y = (int) (yBegin + textHeight + (textHeight + vgap) * i);

				switch (horizontalAlignment) {
				case LEFT:
					displayPositions[i] = new Position(x + hgap, y);
					break;
				case CENTER:
					displayPositions[i] = new Position(
							(int) (x + (width + UIUtils.getStringWidth(displayText[i], font)) / 2), y);
					break;
				case RIGHT:
					displayPositions[i] = new Position(
							x + width - hgap - (int) UIUtils.getStringWidth(displayText[i], font), y);
					break;
				}
			}
		} else {
			displayText = new String[0];
			displayPositions = new Position[0];
		}
	}

	public void clear() {
		text = "";
		setup();
	}

	// ===== Getters ===== \\
	public String getText() {
		return text;
	}

	public int getHgap() {
		return hgap;
	}

	public int getVgap() {
		return vgap;
	}

	// ===== Setters ===== \\
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		setup();
	}

	public void setHgap(int hgap) {
		this.hgap = hgap;
	}

	public void setVgap(int vgap) {
		this.vgap = vgap;
	}

	public void setText(String text) {
		this.text = text;
		setup();
	}
}