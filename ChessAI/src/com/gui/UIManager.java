package com.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.gui.interfaces.Clickable;
import com.gui.interfaces.Typeable;
import com.gui.objects.UIObject;

/**
 * A UIManager is basically a list for UIObjects. It can be added to a Key-
 * and/or MouseManager to get input.
 * 
 * @author DefensivLord
 */
public class UIManager implements Clickable, Typeable {
	/**
	 * The list that holds the objects.
	 */
	private List<UIObject> objects;

	private Graphics g;

	/**
	 * The constructor for instances of the class UIManager.
	 */
	public UIManager(Graphics g) {
		objects = new CopyOnWriteArrayList<UIObject>();
		this.g = g;
	}

	public void repaint() {

		for (UIObject o : objects) {
			o.repaint();
		}
	}

	@Override
	public void onMouseMove(MouseEvent e) {
		for (UIObject o : objects) {
			if (o instanceof Clickable)
				((Clickable) o).onMouseMove(e);
		}
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		for (UIObject o : objects) {
			if (o instanceof Clickable)
				((Clickable) o).onMouseRelease(e);
		}
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		for (UIObject o : objects) {
			if (o instanceof Typeable)
				((Typeable) o).onKeyPress(e);
		}
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		for (UIObject o : objects) {
			if (o instanceof Typeable)
				((Typeable) o).onKeyRelease(e);
		}
	}

	public void addObject(UIObject o) {
		objects.add(o);
		o.setGraphics(g);
		o.repaint();
	}

	public void removeObject(UIObject o) {
		objects.remove(o);
	}

	// ===== Getters ===== \\
	public List<UIObject> getObjects() {
		return objects;
	}
}