package com.gui.objects;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.gui.interfaces.Clickable;
import com.gui.interfaces.Scrollable;
import com.gui.interfaces.Typeable;

/**
 * The purpose of the UIPanel class is to hold multiple objects within one, so
 * you have a way to group various objects and set their visibility or enability
 * quickly.
 * 
 * @author DefensivLord
 */
public class UIPanel extends UIObject implements Clickable, Typeable, Scrollable {
	protected List<UIObject> objects;

	/**
	 * The constructor for instances of the class UIPanel.
	 */
	public UIPanel() {
		objects = new CopyOnWriteArrayList<>();
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			drawBackground();
			objects.forEach(o -> o.repaint());
			drawBorder();
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

	@Override
	public void onMouseScroll(MouseWheelEvent e) {
		if (enabled) {
			for (UIObject o : objects) {
				if (o instanceof Scrollable)
					((Scrollable) o).onMouseScroll(e);
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

	public UIObject addRelative(UIObject o) {
		o.setPosition(this.x + o.getX(), this.y + o.getY());
		return add(o);
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

	// ===== Setters ===== \\
	public void setGraphics(Graphics g) {
		objects.forEach(o -> {
			o.setGraphics(g);
		});
		super.setGraphics(g);
	}
}