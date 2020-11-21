package ui.objects;

import java.awt.Graphics;
import java.awt.Image;

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

	public UIImageButton(Image buttonImage, boolean stretchingImage) {
		this.buttonImage = buttonImage;
		this.stretchingImage = stretchingImage;
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			if (background != null) {
				g.setColor(background);
				g.fillRect(x, y, width, height);
			}
			if (border != null) {
				g.setColor(border);
				g.drawRect(x, y, width, height);
			}
			g.drawImage(buttonImage, imageX, imageY, imageWidth, imageHeight, null);
		}
	}

	private void calculateImageBounds() {
		if (stretchingImage) {
			imageX = x;
			imageY = y;
			imageWidth = width;
			imageHeight = height;
		} else {
			double originalImageWidth = buttonImage.getWidth(null);
			double originalImageHeight = buttonImage.getHeight(null);

			double widthDiff = width - originalImageWidth;
			double heightDiff = height - originalImageHeight;

			double factor;

			if (widthDiff < heightDiff) {
				factor = widthDiff / originalImageWidth + 1;
			} else {
				factor = heightDiff / originalImageHeight + 1;
			}

			imageWidth = (int) (originalImageWidth * factor);
			imageHeight = (int) (originalImageHeight * factor);

			imageX = x + (width - imageWidth) / 2;
			imageY = y + (height - imageHeight) / 2;
		}
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
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		calculateImageBounds();
	}

	public void setButtonImage(Image buttonImage) {
		this.buttonImage = buttonImage;
		calculateImageBounds();
	}

	public void setImageBounds(int imageX, int imageY, int imageWidth, int imageHeight) {
		this.imageX = imageX;
		this.imageY = imageY;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}

	public void setStretchingImage(boolean stretchingImage) {
		this.stretchingImage = stretchingImage;
		calculateImageBounds();
	}
}