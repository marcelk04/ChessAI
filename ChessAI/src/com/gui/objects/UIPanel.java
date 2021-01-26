package com.gui.objects;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.gui.interfaces.Clickable;
import com.gui.interfaces.Typeable;

/**
 * The purpose of the UIPanel class is to hold multiple objects within one, so
 * you have a way to group various objects and set their visibility or enability
 * quickly.
 * 
 * @author DefensivLord
 */
public class UIPanel extends UIObject implements Clickable, Typeable {
	private List<UIObject> objects;

	/**
	 * The constructor for instances of the class UIPanel.
	 */
	public UIPanel() {
		objects = new CopyOnWriteArrayList<>();
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

			objects.forEach(o -> o.repaint());
		}
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		if (enabled) {
			for (UIObject o : objects) {
				if (o instanceof Typeable)
					((Typeable) o).onKeyPress(e);
			}
		}
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		if (enabled) {
			for (UIObject o : objects) {
				if (o instanceof Typeable)
					((Typeable) o).onKeyRelease(e);
			}
		}
	}

	@Override
	public void onMouseMove(MouseEvent e) {
		if (enabled) {
			for (UIObject o : objects) {
				if (o instanceof Clickable)
					((Clickable) o).onMouseMove(e);
			}
		}
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		if (enabled) {
			for (UIObject o : objects) {
				if (o instanceof Clickable)
					((Clickable) o).onMouseRelease(e);
			}
		}
	}

	/**
	 * Adds a UIObject to the panel.
	 * 
	 * @param o the object to be added.
	 * @return the object.
	 */
	public UIObject add(UIObject o) {
		objects.add(o);
		o.setGraphics(g);
		return o;
	}

	/**
	 * Removes a UIObject from the panel.
	 * 
	 * @param o the object to be removed.
	 * @return the object.
	 */
	public UIObject remove(UIObject o) {
		objects.remove(o);
		repaint();
		return o;
	}

	// ===== Getters ===== \\
	public List<UIObject> getObjects() {
		return objects;
	}
}