package ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ui.interfaces.Clickable;
import ui.interfaces.Typeable;
import ui.objects.UIObject;

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

	/**
	 * The constructor for instances of the class UIManager.
	 */
	public UIManager() {
		objects = new CopyOnWriteArrayList<UIObject>();
	}

	public void render(Graphics g) {
		for (UIObject o : objects) {
			o.render(g);
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
	}

	public void removeObject(UIObject o) {
		objects.remove(o);
	}

	// ===== Getters ===== \\
	public List<UIObject> getObjects() {
		return objects;
	}
}