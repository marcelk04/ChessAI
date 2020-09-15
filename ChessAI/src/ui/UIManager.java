package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class UIManager {
	public List<UIObject> objects;

	public UIManager() {
		objects = new ArrayList<UIObject>();
	}

	public void onMouseMove(MouseEvent e) {
		for (UIObject o : objects)
			o.onMouseMove(e);
	}

	public void onMouseRelease(MouseEvent e) {
		for (UIObject o : objects)
			o.onMouseRelease(e);
	}

	public void tick() {
		for (UIObject o : objects)
			o.tick();
	}

	public void render(Graphics g) {
		for (UIObject o : objects)
			o.render(g);
	}

	public void addObject(UIObject object) {
		objects.add(object);
	}

	public void removeObject(UIObject object) {
		objects.add(object);
	}
}