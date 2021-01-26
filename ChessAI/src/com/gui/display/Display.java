package com.gui.display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

import com.gui.input.KeyManager;
import com.gui.input.MouseManager;
import com.gui.objects.UIObject;
import com.gui.objects.UIPanel;

public class Display {
	private static final int xOffset = 16, yOffset = 39;

	private int width, height;
	private String title;

	private JFrame frame;
	private Canvas canvas;

	private Graphics g;
	private UIPanel objects;

	private MouseManager mouseManager;
	private KeyManager keyManager;

	public Display(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;

		create();
	}

	private void create() {
		int frameWidth = width + xOffset, frameHeight = height + yOffset;

		frame = new JFrame(title);
		frame.setSize(frameWidth, frameHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(frameWidth, frameHeight));
		canvas.setMaximumSize(new Dimension(frameWidth, frameHeight));
		canvas.setMinimumSize(new Dimension(frameWidth, frameHeight));
		canvas.setFocusable(false);

		frame.add(canvas);
		frame.setVisible(true);
		g = canvas.getGraphics();

		objects = new UIPanel();
		objects.setBounds(0, 0, frameWidth, frameHeight);
		objects.setGraphics(g);

		mouseManager = new MouseManager(objects);
		frame.addMouseListener(mouseManager);
		frame.addMouseMotionListener(mouseManager);
		canvas.addMouseListener(mouseManager);
		canvas.addMouseMotionListener(mouseManager);

		keyManager = new KeyManager(objects);
		frame.addKeyListener(keyManager);
	}

	public UIObject add(UIObject o) {
		return objects.add(o);
	}

	public UIObject remove(UIObject o) {
		return objects.remove(o);
	}

	public void close() {
		frame.dispose();
	}

	// ===== Getters ===== \\
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public JFrame getFrame() {
		return frame;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public Graphics getGraphics() {
		return g;
	}

	public MouseManager getMouseManager() {
		return mouseManager;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public UIPanel getObjects() {
		return objects;
	}

	// ===== Setters ===== \\
	public void setBackground(Color background) {
		frame.setBackground(background);
	}

	public void setTitle(String title) {
		frame.setTitle(this.title = title);
	}
}