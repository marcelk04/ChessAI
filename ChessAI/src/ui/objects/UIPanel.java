package ui.objects;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ui.UIManager;
import ui.interfaces.Clickable;
import ui.interfaces.Typeable;

public class UIPanel extends UIObject implements Clickable, Typeable {
	private UIManager uiManager;

	public UIPanel() {
		uiManager = new UIManager();
	}

	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			if (background != null) {
				g.setColor(background);
				g.fillRect(x, y, width, height);
			}
			if (border != null) {
				g.setColor(border);
				g.drawRect(x, y, width, height);
			}
			uiManager.render(g);
		}
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		if (enabled)
			uiManager.onKeyPress(e);
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		if (enabled)
			uiManager.onKeyRelease(e);
	}

	@Override
	public void onMouseMove(MouseEvent e) {
		if (enabled)
			uiManager.onMouseMove(e);
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		if (enabled)
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