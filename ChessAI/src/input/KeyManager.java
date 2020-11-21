package input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import ui.UIManager;

public class KeyManager extends KeyAdapter {
	private final UIManager uiManager;

	public KeyManager(final UIManager uiManager) {
		this.uiManager = uiManager;
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