package com.gui.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.gui.objects.UIPanel;

public class KeyManager extends KeyAdapter {
	private final UIPanel panel;

	public KeyManager(final UIPanel uiManager) {
		this.panel = uiManager;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		panel.onKeyPress(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		panel.onKeyRelease(e);
	}
}