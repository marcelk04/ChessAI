package ui.interfaces;

import java.awt.event.KeyEvent;

public interface Typeable {
	void onKeyPress(KeyEvent e);
	void onKeyRelease(KeyEvent e);
}