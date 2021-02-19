package com.gui.objects;

import java.awt.Color;

import com.gui.display.Display;

public class UIDialog extends UIPanel {
	public static final int NO_OPTION = 0, YES_OPTION = 1, CANCEL_OPTION = 2;

	private boolean closed = false;

	public void activate() {
		visible = true;
		enabled = true;
	}

	public void deactivate() {
		closed = true;
		objects.forEach(o -> o.setVisible(false));
		visible = false;
		enabled = false;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setPositionRelativeTo(UIObject object) {
		int centerX = object.getX() + object.getWidth() / 2;
		int centerY = object.getY() + object.getHeight() / 2;

		setPosition(centerX - width / 2, centerY - height / 2);
	}

	public static void showInformationDialog(Display display, String text) {
		UIDialog dialog = new UIDialog();
		dialog.setSize(340, 135);
		dialog.setPositionRelativeTo(display.getObjects());
		dialog.setBorder(Color.black);

		UITextArea taText = new UITextArea(text);
		taText.setBounds(10, 10, 320, 80);
		taText.setBorder(Color.black);
		dialog.addRelative(taText);

		UITextButton btnOk = new UITextButton("Ok");
		btnOk.setBounds(90, 100, 160, 25);
		btnOk.setTextColor(Color.white);
		btnOk.setBackground(Color.black);
		btnOk.setClickListener(e -> display.removeLastDialog());
		dialog.addRelative(btnOk);

		display.showDialog(dialog);
	}
}