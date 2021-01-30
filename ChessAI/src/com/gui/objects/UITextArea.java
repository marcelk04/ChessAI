package com.gui.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.chess.move.Position;
import com.gui.UIUtils;
import com.gui.interfaces.Clickable;
import com.gui.interfaces.Scrollable;
import com.main.Utils;

public class UITextArea extends UIObject implements Clickable, Scrollable {
	protected String text;
	private String[] displayText;
	private Position[] displayPositions;
	private int beginIndex, endIndex;
	private int scrollBarY, scrollBarWidth, scrollBarHeight;
	protected int hgap, vgap;
	protected boolean hovering = false;
	protected Color scrollBarColor;

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
		this.beginIndex = 0;
		this.endIndex = -1;
		this.scrollBarWidth = 5;
		scrollBarColor = new Color(63, 63, 63, 171);

		calculateTextPositionsUpdated();
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			if (background != null) {
				g.setColor(background);
				g.fillRect(x, y, width, height);
			}

			try {
				for (int i = 0; i <= endIndex - beginIndex; i++) {
					UIUtils.drawString(g, displayText[i + beginIndex], displayPositions[i].x, displayPositions[i].y,
							false, textColor, font);
				}
			} catch (Exception e) {
			}

			g.setColor(scrollBarColor);
			g.fillRect(this.x + this.width - scrollBarWidth, scrollBarY, scrollBarWidth, scrollBarHeight);

			if (border != null) {
				g.setColor(border);
				g.drawRect(x, y, width, height);
			}
		}
	}

	private void calculateTextPositionsUpdated() {
		if (text != null && !text.equals("")) {
			float textHeight = (float) UIUtils.getStringHeight(text, font);
			int maxLines = (int) (height / (textHeight + vgap));
			int maxTextWidth = width - hgap * 2;

			displayText = text.split("\n");

			beginIndex = Utils.clamp(beginIndex, 0, displayText.length - 1);

			if (displayText.length <= maxLines) {
				beginIndex = 0;
				endIndex = displayText.length - 1;

				scrollBarY = this.y;
				scrollBarHeight = this.height;
			} else {
				if (beginIndex + maxLines >= displayText.length) {
					beginIndex = displayText.length - maxLines;
					endIndex = displayText.length - 1;
				} else {
					endIndex = beginIndex + maxLines - 1;
				}

				scrollBarHeight = Math.round((float) maxLines / (float) displayText.length * (float) this.height);
				scrollBarY = Math.round((float) beginIndex / (float) displayText.length * (float) this.height) + this.y;
			}

			for (int i = 0; i < displayText.length; i++) {
				displayText[i] = UIUtils.shortenText(displayText[i], maxTextWidth, font);
			}

			displayPositions = new Position[endIndex - beginIndex + 1];

			for (int i = 0; i <= endIndex - beginIndex; i++) {
				int y = Math.round(this.y + (textHeight + vgap) * (i + 1));

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

			beginIndex = 0;
			endIndex = -1;
		}

		repaint();
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

		repaint();
	}

	@Override
	public void onMouseMove(MouseEvent e) {
		if (boundsContain(e.getX(), e.getY()) && enabled && visible)
			hovering = true;
		else
			hovering = false;
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
	}

	@Override
	public void onMouseScroll(MouseWheelEvent e) {
		if (hovering && enabled && visible) {
			beginIndex += e.getWheelRotation();
			calculateTextPositionsUpdated();
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

	public int getScrollBarWidth() {
		return scrollBarWidth;
	}

	public Color getScrollBarColor() {
		return scrollBarColor;
	}

	// ===== Setters ===== \\
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		calculateTextPositionsUpdated();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		calculateTextPositionsUpdated();
	}

	public void setHgap(int hgap) {
		this.hgap = hgap;
		calculateTextPositionsUpdated();
	}

	public void setVgap(int vgap) {
		this.vgap = vgap;
		calculateTextPositionsUpdated();
	}

	public void setText(String text) {
		this.text = text;
		calculateTextPositionsUpdated();
	}

	public void setScrollBarWidth(int scrollBarWidth) {
		this.scrollBarWidth = scrollBarWidth;
	}

	public void setScrollBarColor(Color scrollBarColor) {
		this.scrollBarColor = scrollBarColor;
	}
}