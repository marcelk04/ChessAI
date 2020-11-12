package ui.objects;

import java.awt.event.MouseEvent;

import ui.interfaces.Clickable;
import ui.listeners.ClickListener;

public abstract class UIButton extends UIObject implements Clickable {
	protected ClickListener clickListener;
	protected boolean hovering = false;

	@Override
	public void onMouseMove(MouseEvent e) {
		if (boundsContain(e.getX(), e.getY()) && enabled && visible)
			hovering = true;
		else
			hovering = false;
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		if (hovering && enabled && visible) {
			if (clickListener != null)
				clickListener.onClick(e);
		}
	}

	// ===== Getters ===== \\
	public ClickListener getClickListener() {
		return clickListener;
	}

	// ===== Setters ===== \\
	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}
}