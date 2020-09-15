package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public abstract class UIObject {
	protected float x, y;
	protected int width, height;
	protected boolean hovering;
	protected Rectangle bounds;
	protected boolean visible = true;

	public UIObject(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bounds = new Rectangle((int) x, (int) y, width, height);
	}

	public UIObject() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.bounds = new Rectangle((int) x, (int) y, width, height);
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public abstract void onClick();

	public void onMouseMove(MouseEvent e) {
		if (bounds.contains(e.getX(), e.getY()))
			hovering = true;
		else
			hovering = false;
	}

	public void onMouseRelease(MouseEvent e) {
		if (hovering && visible)
			onClick();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isHovering() {
		return hovering;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;

		bounds.x = (int) x;
		bounds.y = (int) y;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;

		bounds.width = width;
		bounds.height = height;
	}

	public void setBounds(float x, float y, int width, int height) {
		setPosition(x, y);
		setSize(width, height);
	}

	public void setBounds(Rectangle rect) {
		setBounds((float) rect.getX(), (float) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}