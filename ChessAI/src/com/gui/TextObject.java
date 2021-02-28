package com.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.chess.move.Position;

public class TextObject {
	private String text;
	private Position pos;

	public TextObject() {
		this(null, null);
	}

	public TextObject(String text, Position pos) {
		this.text = text;
		this.pos = pos;
	}

	public void render(Graphics g, Color c, Font f) {
		if (text != null)
			UIUtils.drawString(g, text, pos.x, pos.y, false, c, f);
	}

	// ===== Getters ===== \\
	public String getText() {
		return text;
	}

	public Position getPosition() {
		return pos;
	}

	// ===== Setters ===== \\
	public void setText(String text) {
		this.text = text;
	}

	public void setPosition(Position pos) {
		this.pos = pos;
	}
}