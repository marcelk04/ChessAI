package ui.objects;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import ui.UIUtils;
import ui.interfaces.Clickable;
import ui.listeners.ClickListener;

public class UIButton extends UIObject implements Clickable {
	protected String text;
	protected boolean hovering = false;
	protected ClickListener clickListener;

	public UIButton(String text) {
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

	@Override
	public void onMouseMove(MouseEvent e) {
		if (boundsContain(e.getX(), e.getY()))
			hovering = true;
		else
			hovering = false;
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		if (hovering && enabled) {
			if (clickListener != null)
				clickListener.onClick();
		}
	}

	// ===== Getters ===== \\
	public String getText() {
		return text;
	}

	public ClickListener getClickListener() {
		return clickListener;
	}

	// ===== Setters ===== \\
	public void setText(String text) {
		this.text = text;
	}

	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}
}