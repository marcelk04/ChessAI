package ui.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ui.UIUtils;
import ui.interfaces.Clickable;
import ui.interfaces.Typeable;

public class UITextField extends UIObject implements Clickable, Typeable {
	private String text = "";
	private String end = "";
	private int textX, textY;
	private boolean hovering = false;
	private boolean selected = false;

	public UITextField() {
		border = Color.black;
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
			UIUtils.drawString(g, text + end, textX, textY, true, textColor, font);
		}
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		if (selected) {
			if ((44 <= e.getKeyCode() && e.getKeyCode() <= 126) || e.getKeyCode() == KeyEvent.VK_SPACE) {
				text += e.getKeyChar();
				calculateTextPos();
			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !text.isEmpty()) {
				text = text.substring(0, text.length() - 1);
				calculateTextPos();
			}
		}
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
	}

	@Override
	public void onMouseMove(MouseEvent e) {
		if (visible && boundsContain(e.getX(), e.getY()))
			hovering = true;
		else
			hovering = false;
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		if (hovering && enabled) {
			selected = true;
			end = "_";
			calculateTextPos();
		} else {
			selected = false;
			end = "";
			calculateTextPos();
		}
	}

	protected void calculateTextPos() {
		double textWidth = UIUtils.getStringWidth(text + end, font);
		textX = (int) (x + textWidth / 2);
		textY = y + height / 2;
		if (border != null)
			textX += 2;
	}

	// ===== Getter ===== \\
	public String getText() {
		return text;
	}

	// ===== Setter ===== \\
	public void setText(String text) {
		this.text = text;
		calculateTextPos();
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		calculateTextPos();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		calculateTextPos();
	}
}