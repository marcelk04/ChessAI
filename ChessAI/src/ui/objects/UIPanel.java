package ui.objects;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ui.UIManager;
import ui.interfaces.Clickable;
import ui.interfaces.Typeable;

public class UIPanel extends UIObject implements Clickable, Typeable {
	private UIManager uiManager;

	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		uiManager.render(g);
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		uiManager.onKeyPress(e);
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		uiManager.onKeyRelease(e);
	}

	@Override
	public void onMouseMove(MouseEvent e) {
		uiManager.onMouseMove(e);
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		uiManager.onMouseRelease(e);
	}

	public UIObject add(UIObject o) {
		uiManager.addObject(o);
		return o;
	}
	
	public UIObject remove(UIObject o) {
		uiManager.removeObject(o);
		return o;
	}
}