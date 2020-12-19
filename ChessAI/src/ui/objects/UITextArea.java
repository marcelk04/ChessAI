package ui.objects;

import java.awt.Font;
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

		calculateTextPositions();
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

	private void calculateTextPositions() {
		if (text != null && !text.equals("")) {
			float textHeight = (float) UIUtils.getStringHeight(text, font);
			int maxLines = (int) (height / textHeight);
			int maxTextWidth = width - hgap * 2;

			String[] splitText = text.split("\n");
			displayText = new String[Math.min(splitText.length, maxLines)];

			for (int i = 0; i < displayText.length; i++) {
				displayText[i] = UIUtils.shortenText(splitText[i], maxTextWidth, font);
			}

			float totalHeight = (textHeight + vgap) * displayText.length - vgap;

			displayText = UIUtils.shortenArray(displayText, maxTextWidth, height, vgap, font);

			int yBegin;

			switch (verticalAlignment) {
			case TOP:
				yBegin = y + vgap;
				break;
			case CENTER:
				yBegin = y + Math.round((height - totalHeight) / 2);
				break;
			case BOTTOM:
				yBegin = y + height - Math.round(totalHeight) - vgap;
				break;
			default:
				yBegin = y + vgap;
			}

			displayPositions = new Position[displayText.length];

			for (int i = 0; i < displayText.length; i++) {
				int y = Math.round(yBegin + textHeight + (textHeight + vgap) * i);

				switch (horizontalAlignment) {
				case LEFT:
					displayPositions[i] = new Position(x + hgap, y);
					break;
				case CENTER:
					displayPositions[i] = new Position(
							x + Math.round((width - (float) UIUtils.getStringWidth(displayText[i], font)) / 2f), y);
					break;
				case RIGHT:
					displayPositions[i] = new Position(
							x + width - hgap - Math.round((float) UIUtils.getStringWidth(displayText[i], font)), y);
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
		calculateTextPositions();
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
		calculateTextPositions();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		calculateTextPositions();
	}

	public void setHgap(int hgap) {
		this.hgap = hgap;
	}

	public void setVgap(int vgap) {
		this.vgap = vgap;
	}

	public void setText(String text) {
		this.text = text;
		calculateTextPositions();
	}
}