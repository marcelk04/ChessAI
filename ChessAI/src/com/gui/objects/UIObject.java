package com.gui.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The UIObject class is a template class for all objects that should be added
 * to the GUI. It provides a basic structure for the subclasses with the
 * dimension and some attributes for text rendering already in place. Also it
 * includes getters and setters for all the attributes, so they don't have to be
 * added in subclasses.
 * 
 * @author DefensivLord
 */
public abstract class UIObject {
	/**
	 * Constants used for the alignment of text.
	 */
	public static final int LEFT = 0, RIGHT = 1, CENTER = 2, TOP = 3, BOTTOM = 4;

	/**
	 * The default text color.
	 */
	public static final Color STANDARD_TEXT_COLOR = Color.black;

	/**
	 * The default font used for text rendering.
	 */
	public static final Font STANDARD_FONT = new Font("Sans Serif", Font.PLAIN, 13);

	/**
	 * The position of the object.
	 */
	protected int x, y;

	/**
	 * The dimension of the object.
	 */
	protected int width, height;

	protected int arcWidth, arcHeight;

	/**
	 * The visibiliy of the object.
	 */
	protected boolean visible = true;

	/**
	 * Whether the object is enabled or not.
	 */
	protected boolean enabled = true;

	/**
	 * The horizontal alignment of the text.
	 */
	protected int horizontalAlignment = LEFT;

	/**
	 * The vertical alignment of the text.
	 */
	protected int verticalAlignment = TOP;

	/**
	 * The color of the background.
	 */
	protected Color background = null;

	/**
	 * The color of the border.
	 */
	protected Color border = null;

	/**
	 * The color of the text.
	 */
	protected Color textColor = STANDARD_TEXT_COLOR;

	/**
	 * The font used for text rendering.
	 */
	protected Font font = STANDARD_FONT;

	protected Graphics g;

	/**
	 * Default constructor for instances of the class UIObject.
	 */
	public UIObject() {
	}

	/**
	 * Constructor with position and size already as parameters in place.
	 * 
	 * @param x      the x coordinate.
	 * @param y      the y coordinate.
	 * @param width  the width.
	 * @param height the height.
	 */
	public UIObject(int x, int y, int width, int height) {
		this();
		setBounds(x, y, width, height);
	}

	/**
	 * Constructor with position and size already as parameters in from of a
	 * rectangle in place.
	 * 
	 * @param bounds the bounds of the object.
	 */
	public UIObject(Rectangle bounds) {
		this();
		setBounds(bounds);
	}

	/**
	 * Used for rendering the object; Called every frame.
	 * 
	 * @param g the graphics object.
	 */
	public abstract void render(Graphics g);

	public void repaint() {
		if (g != null) {
			g.clearRect(x, y, width, height);
			render(g);
		}
	}

	protected void propertyChanged() {
		repaint();
	}

	protected void drawBackground() {
		if (background != null) {
			g.setColor(background);
			g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
		}
	}

	protected void drawBorder() {
		if (border != null) {
			g.setColor(border);
			g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
		}
	}

	/**
	 * A helper method used by the subclasses to check whether a specific point is
	 * within their bounds or not.
	 * 
	 * @param x the x coordinate of the point.
	 * @param y the y coordinate of the point.
	 * @return {@code true} if the point is within the bounds.
	 */
	protected boolean boundsContain(int x, int y) {
		return this.x <= x && x <= this.x + this.width && this.y <= y && y <= this.y + this.height;
	}

	// ===== Getters ===== \\
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

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public int getArcWidth() {
		return arcWidth;
	}

	public int getArcHeight() {
		return arcHeight;
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

	public int getVerticalAlignment() {
		return verticalAlignment;
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

	public Graphics getGraphics() {
		return g;
	}

	// ===== Setters ===== \\
	public void setPosition(int x, int y) {
		if (this.x != x || this.y != y) {
			this.x = x;
			this.y = y;
			propertyChanged();
		}
	}

	public void setSize(int width, int height) {
		if (this.width != width || this.height != height) {
			this.width = width;
			this.height = height;
			propertyChanged();
		}
	}

	public void setBounds(int x, int y, int width, int height) {
		setPosition(x, y);
		setSize(width, height);
	}

	public void setBounds(Rectangle bounds) {
		if (bounds != null)
			setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public void setArcBounds(int arcWidth, int arcHeight) {
		if (this.arcWidth != arcWidth || this.arcHeight != arcHeight) {
			this.arcWidth = arcWidth;
			this.arcHeight = arcHeight;
			propertyChanged();
		}
	}

	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			propertyChanged();
		}
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			propertyChanged();
		}
	}

	public void setHorizontalAlignment(int horizontalAlignment) {
		if (this.horizontalAlignment != horizontalAlignment) {
			this.horizontalAlignment = horizontalAlignment;
			propertyChanged();
		}
	}

	public void setVerticalAlignment(int verticalAlignment) {
		if (this.verticalAlignment != verticalAlignment) {
			this.verticalAlignment = verticalAlignment;
			propertyChanged();
		}
	}

	public void setBackground(Color background) {
		if (this.background == null || !this.background.equals(background)) {
			this.background = background;
			propertyChanged();
		}
	}

	public void setBorder(Color border) {
		if (this.border == null || !this.border.equals(border)) {
			this.border = border;
			propertyChanged();
		}
	}

	public void setTextColor(Color textColor) {
		if (!this.textColor.equals(textColor)) {
			if (textColor != null)
				this.textColor = textColor;
			else
				this.textColor = STANDARD_TEXT_COLOR;

			propertyChanged();
		}
	}

	public void setFont(Font font) {
		if (!this.font.equals(font)) {
			if (font != null)
				this.font = font;
			else
				this.font = STANDARD_FONT;

			propertyChanged();
		}
	}

	public void setGraphics(Graphics g) {
		if (this.g == null || !this.g.equals(g)) {
			this.g = g;
			propertyChanged();
		}
	}
}