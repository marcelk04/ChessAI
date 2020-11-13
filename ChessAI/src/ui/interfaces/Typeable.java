package ui.interfaces;

import java.awt.event.KeyEvent;

/**
 * This interface can be implemented in UIObjects that need key input.When doing
 * so the object can be added as normal objects to any display or UIPanel,
 * however the press and release method are still executed whenever these events
 * happen.
 * 
 * @author DefensivLord
 */
public interface Typeable {
	/**
	 * Executed whenever a key is pressed.
	 * 
	 * @param e
	 */
	void onKeyPress(KeyEvent e);

	/**
	 * Executed whenever a key is released.
	 * 
	 * @param e
	 */
	void onKeyRelease(KeyEvent e);
}