package com.gui.objects;

import java.awt.Graphics;

public class UILine extends UIObject {
	@Override
	public void render(Graphics g) {
		g.setColor(background);
		if (height == 0)
			g.drawLine(x, y, x + width, y);
		else if (width == 0)
			g.drawLine(x, y, x, y + height);
	}
}