package ui.objects;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import ui.UIUtils;
import ui.interfaces.Clickable;

public class UIButton extends UIObject implements Clickable {
	private String text;
	private boolean hovering = false;

	public UIButton(String text) {
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
			if (clicker != null)
				clicker.onClick();
		}
	}
	
	// ===== Getter ===== \\
	public String getText() {
		return text;
	}
	
	// ===== Setter ===== \\
	public void setText(String text) {
		this.text = text;
	}
}