package com.gui.objects;

import java.awt.event.MouseEvent;

import com.gui.interfaces.Clickable;
import com.gui.listeners.ClickListener;


/**
 * The class UIButton serves as a framework for every button that should be
 * added to the GUI, for example a text button or an image button.
 * 
 * @author DefensivLord
 */
public abstract class UIButton extends UIObject implements Clickable {
	/**
	 * The ClickListener whose method is executed on click.
	 */
	protected ClickListener clickListener;

	/**
	 * Indicates whether the mouse is currently hovering over the button.
	 */
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