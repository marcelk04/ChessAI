package ui.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import ui.listeners.ClickListener;

public abstract class UIObject {
	public static final int LEFT = 0, CENTER = 1, RIGHT = 2;
	protected static final Color standard_text_color = Color.black;
	protected static final Font standard_font = new Font("Sans Serif", Font.PLAIN, 13);

	protected int x, y;
	protected int width, height;

	protected boolean visible = true;
	protected boolean enabled = true;

	protected int horizontalAlignment = LEFT;

	protected Color background = null;
	protected Color border = null;
	protected Color textColor = standard_text_color;
	protected Font font = standard_font;

	protected ClickListener clicker;

	public UIObject(int x, int y, int width, int height) {
		this();
		setBounds(x, y, width, height);
	}

	public UIObject(Rectangle bounds) {
		this();
		setBounds(bounds);
	}

	public UIObject() {
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	protected boolean boundsContain(int x, int y) {
		return this.x <= x && x <= this.x + this.width && this.y <= y && y <= this.y + this.height;
	}

	// ===== Getter ===== \\
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public Color getBackground() {
		return background;
	}

	public Color getBorder() {
		return border;
	}

	public Color getTextColor() {
		return textColor;
	}

	public Font getFont() {
		return font;
	}

	public ClickListener getClickListener() {
		return clicker;
	}

	// ===== Setter ===== \\
	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setBounds(Rectangle bounds) {
		setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public void setBorder(Color border) {
		this.border = border;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void setClickListener(ClickListener clicker) {
		this.clicker = clicker;
	}
}