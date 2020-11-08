package display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import input.KeyManager;
import input.MouseManager;
import ui.UIManager;
import ui.objects.UIObject;

public class Display implements Runnable {
	private Thread thread;
	private boolean running = false;

	private Graphics g;
	private BufferStrategy bs;

	private JFrame frame;
	private Canvas canvas;

	private WindowHandler wh;

	private String title;
	private int width, height;

	private MouseManager mouseManager;
	private KeyManager keyManager;
	private UIManager uiManager;

	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;

		create();
		init();
		start();
	}

	private void tick() {
		uiManager.tick();
	}

	private void render() {
		bs = canvas.getBufferStrategy();

		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}

		g = bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);

		{ // draw here
			uiManager.render(g);
		}

		bs.show();
		g.dispose();
	}

	public UIObject add(UIObject o) {
		uiManager.addObject(o);
		return o;
	}

	public UIObject remove(UIObject o) {
		uiManager.removeObject(o);
		return o;
	}

	private void create() {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setFocusable(false);

		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}

	private void init() {
		wh = new WindowHandler(this);
		frame.addWindowListener(wh);

		uiManager = new UIManager();

		mouseManager = new MouseManager(uiManager);
		frame.addMouseListener(mouseManager);
		frame.addMouseMotionListener(mouseManager);
		canvas.addMouseListener(mouseManager);
		canvas.addMouseMotionListener(mouseManager);

		keyManager = new KeyManager(uiManager);
		frame.addKeyListener(keyManager);
	}

	@Override
	public void run() {
		int tps = 60;
		double timePerTick = 1000000000 / tps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		int frames = 0;

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			// tick
			if (delta >= 1) {
				tick();
				ticks++;
				delta--;
			}

			// render
			render();
			frames++;

			// show tps & fps
			if (timer >= 1000000000) {
				System.out.println(frames + "|" + ticks);
				frames = 0;
				ticks = 0;
				timer -= 1000000000;
			}
		}

		stop();
	}

	public synchronized void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;

		running = false;
	}

	public JFrame getFrame() {
		return frame;
	}

	public Canvas getCanvas() {
		return canvas;
	}
}