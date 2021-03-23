package com.gui.objects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import com.gui.UIUtils;

/**
 * The UIImageButton is a button, which displays an image instead of text. The
 * image can either be stretched on the whole size of the button, or its size
 * can be changed to fit within the bounds of the button without changing scale.
 * You can also set the bounds of the image yourself.f
 * 
 * @author DefensivLord
 */
public class UIImageButton extends UIButton {
	/**
	 * The image of the button.
	 */
	private Image buttonImage;

	/**
	 * The position of the image.
	 */
	private int imageX, imageY;

	/**
	 * The dimension of the image.
	 */
	private int imageWidth, imageHeight;

	/**
	 * Indicates whether the image should be stretched or not.
	 */
	private boolean stretchingImage;

	/**
	 * The constructor for instances of the class UIImageButton.
	 * 
	 * @param buttonImage     the image used for the button.
	 * @param stretchingImage indicates whether the image should be stretched or
	 *                        not.
	 */
	public UIImageButton(Image buttonImage, boolean stretchingImage) {
		this.buttonImage = buttonImage;
		this.stretchingImage = stretchingImage;
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			drawBackground();
			g.drawImage(buttonImage, imageX, imageY, imageWidth, imageHeight, null);
			drawBorder();
		}
	}

	/**
	 * Calculates the bounds of the images depending whether it should be stretched
	 * or not.
	 */
	private void calculateImageBounds() {
		Rectangle imagePos = UIUtils.fitImage(buttonImage, x, y, width, height, stretchingImage);
		imageX = imagePos.x;
		imageY = imagePos.y;
		imageWidth = imagePos.width;
		imageHeight = imagePos.height;
	}

	@Override
	protected void propertyChanged() {
		calculateImageBounds();
		repaint();
	}

	// ===== Getters ===== \\
	public Image getButtonImage() {
		return buttonImage;
	}

	public int getImageX() {
		return imageX;
	}

	public int getImageY() {
		return imageY;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public boolean isStretchingImage() {
		return stretchingImage;
	}

	// ===== Setters ===== \\
	public void setButtonImage(Image buttonImage) {
		if (this.buttonImage == null || !this.buttonImage.equals(buttonImage)) {
			this.buttonImage = buttonImage;
			propertyChanged();
		}
	}

	public void setImageBounds(int imageX, int imageY, int imageWidth, int imageHeight) {
		this.imageX = imageX;
		this.imageY = imageY;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		repaint();
	}

	public void setStretchingImage(boolean stretchingImage) {
		if (this.stretchingImage != stretchingImage) {
			this.stretchingImage = stretchingImage;
			propertyChanged();
		}
	}
}