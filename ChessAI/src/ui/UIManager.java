package ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ui.interfaces.Clickable;
import ui.interfaces.Typeable;
import ui.objects.UIObject;

public class UIManager {
	private List<UIObject> objects;

	public UIManager() {
		objects = new CopyOnWriteArrayList<UIObject>();
	}

	public void tick() {
		for (UIObject o : objects) {
			o.tick();
		}
	}

	public void render(Graphics g) {
		for (UIObject o : objects) {
			o.render(g);
		}
	}

	public void onMouseMove(MouseEvent e) {
		for (UIObject o : objects) {
			if (o instanceof Clickable)
				((Clickable) o).onMouseMove(e);
		}
	}

	public void onMouseRelease(MouseEvent e) {
		for (UIObject o : objects) {
			if (o instanceof Clickable)
				((Clickable) o).onMouseRelease(e);
		}
	}

	public void onKeyPress(KeyEvent e) {
		for (UIObject o : objects) {
			if (o instanceof Typeable)
				((Typeable) o).onKeyPress(e);
		}
	}

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

	public List<UIObject> getObjects() {
		return objects;
	}
}