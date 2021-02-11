package com.gui.objects;

public class UIDialog extends UIPanel {
	public static final int NO_OPTION = 0, YES_OPTION = 1, CANCEL_OPTION = 2;

	private boolean closed = false;

	public void activate() {
		visible = true;
		enabled = true;
	}

	public void deactivate() {
		closed = true;
		objects.forEach(o -> o.setVisible(false));
		visible = false;
		enabled = false;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setPositionRelativeTo(UIObject object) {
		int centerX = object.getX() + object.getWidth() / 2;
		int centerY = object.getY() + object.getHeight() / 2;

		setPosition(centerX - width / 2, centerY - height / 2);
	}
}