package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ui.UIManager;

public class KeyManager implements KeyListener {
	private final UIManager uiManager;

	public KeyManager(final UIManager uiManager) {
		this.uiManager = uiManager;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		uiManager.onKeyPress(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		uiManager.onKeyRelease(e);
	}
}