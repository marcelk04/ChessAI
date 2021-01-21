package com.gui.listeners;

import java.awt.event.MouseEvent;

/**
 * An instance of this class can be added to clickable UIObject to detect
 * whenever they are clicked.
 * 
 * @author DefensivLord
 */
public interface ClickListener {
	/**
	 * Executed when the UIObject is clicked.
	 * 
	 * @param e
	 */
	void onClick(MouseEvent e);
}