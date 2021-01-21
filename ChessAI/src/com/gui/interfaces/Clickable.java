package com.gui.interfaces;

import java.awt.event.MouseEvent;

/**
 * This interface can be implemented in UIObjects that need mouse input. When
 * doing so the object can be added as normal objects to any display or UIPanel,
 * however the move and release method are still executed whenever these events
 * happen.
 * 
 * @author DefensivLord
 */
public interface Clickable {
	/**
	 * Executed whenever the mouse is moved.
	 * 
	 * @param e
	 */
	void onMouseMove(MouseEvent e);

	/**
	 * Executed whenever a mouse button is released.
	 * 
	 * @param e
	 */
	void onMouseRelease(MouseEvent e);
}