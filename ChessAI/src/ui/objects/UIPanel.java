package ui.objects;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ui.UIManager;
import ui.interfaces.Clickable;
import ui.interfaces.Typeable;

/**
 * The purpose of the UIPanel class is to hold multiple objects within one, so
 * you have a way to group various objects and set their visibility or enability
 * quickly.
 * 
 * @author DefensivLord
 */
public class UIPanel extends UIObject implements Clickable, Typeable {
	/**
	 * The UIManager that holds all UIObjects added to the panel.
	 */
	private UIManager uiManager;

	/**
	 * The constructor for instances of the class UIPanel.
	 */
	public UIPanel() {
		uiManager = new UIManager();
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

	/**
	 * Adds a UIObject to the panel.
	 * 
	 * @param o the object to be added.
	 * @return the object.
	 */
	public UIObject add(UIObject o) {
		uiManager.addObject(o);
		return o;
	}

	/**
	 * Removes a UIObject from the panel.
	 * 
	 * @param o the object to be removed.
	 * @return the object.
	 */
	public UIObject remove(UIObject o) {
		uiManager.removeObject(o);
		return o;
	}
}