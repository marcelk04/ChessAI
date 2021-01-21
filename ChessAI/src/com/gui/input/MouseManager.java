package com.gui.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.gui.objects.UIPanel;

public class MouseManager implements MouseListener, MouseMotionListener {
	private final UIPanel panel;

	private int mouseX, mouseY;
	private boolean leftPressed, rightPressed;

	public MouseManager(final UIPanel uiManager) {
		this.panel = uiManager;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();

		panel.onMouseMove(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			leftPressed = true;
		else if (e.getButton() == MouseEvent.BUTTON3)
			rightPressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			leftPressed = false;
		else if (e.getButton() == MouseEvent.BUTTON3)
			rightPressed = false;

		panel.onMouseRelease(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}
}