package ui.objects;

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
	 * Constants used for the horizontal alignment of text.
	 */
	public static final int LEFT = 0, CENTER = 1, RIGHT = 2;

	/**
	 * The default text color.
	 */
	protected static final Color standard_text_color = Color.black;

	/**
	 * The default font used for text rendering.
	 */
	protected static final Font standard_font = new Font("Sans Serif", Font.PLAIN, 13);

	/**
	 * The position of the object.
	 */
	protected int x, y;

	/**
	 * The dimension of the object.
	 */
	protected int width, height;

	/**
	 * The visibiliy of the object.
	 */
	protected boolean visible = true;

	/**
	 * Whether the object is enabled or not.
	 */
	protected boolean enabled = true;

	/**
	 * The hoerizontal alignment of the text.
	 */
	protected int horizontalAlignment = LEFT;

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
	protected Color textColor = standard_text_color;

	/**
	 * The font used for text rendering.
	 */
	protected Font font = standard_font;

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

	// ===== Setters ===== \\
	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setBounds(Rectangle bounds) {
		if (bounds != null)
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
		if (textColor != null)
			this.textColor = textColor;
		else
			this.textColor = standard_text_color;
	}

	public void setFont(Font font) {
		if (font != null)
			this.font = font;
		else
			this.font = standard_font;
	}
}