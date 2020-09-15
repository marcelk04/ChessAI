package main;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import gfx.Assets;
import input.KeyManager;
import input.MouseManager;
import states.GameState;
import states.MenuState;
import states.State;

public class Game implements Runnable {
	private int width, height;
	private String title;

	private Thread thread;
	private boolean running = false;

	private BufferStrategy bs;
	private Graphics g;

	// states
	private State gameState;
	private State menuState;

	private Display display;
	private MouseManager mouseManager;
	private KeyManager keyManager;

	public Game(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	private void tick() {
		keyManager.tick();
		State.getState().tick();

		if (keyManager.keyJustPressed(KeyEvent.VK_ESCAPE)) {
			if (State.getState() == gameState) {
				mouseManager.setUIManager(((MenuState) menuState).getUIManager());
				State.setState(menuState);
			} else {
				mouseManager.setUIManager(null);
				State.setState(gameState);
			}
		}
	}

	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);

		{ // render here
			State.getState().render(g);
		}

		bs.show();
		g.dispose();
	}

	private void init() {
		Assets.init();

		keyManager = new KeyManager();
		mouseManager = new MouseManager();

		display = new Display(width, height, title);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);

		gameState = new GameState(this);
		menuState = new MenuState(this);
		State.setState(gameState);
	}

	@Override
	public void run() {
		init();

		double timePerTick = 1000000000 / 60;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		@SuppressWarnings("unused")
		int ticks = 0, frames = 0;

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

			if (timer >= 1000000000) {
//				System.out.println("TPS: " + ticks + " | FPS: " + frames);
				ticks = 0;
				frames = 0;

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
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Display getDisplay() {
		return display;
	}

	public MouseManager getMouseManager() {
		return mouseManager;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public MenuState getMenuState() {
		return (MenuState) menuState;
	}

	public GameState getGameState() {
		return (GameState) gameState;
	}

	public static void main(String[] args) {
		new Game(640, 640, "Chess").start();
	}
}