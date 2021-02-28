package com.gui.objects;

import java.awt.Color;
import java.util.List;

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
		showInformationDialog(display, text, 320, 120);
	}

	public static void showInformationDialog(Display display, String text, int taWidth, int taHeight) {
		taWidth = Math.max(160, taWidth);
		taHeight = Math.max(80, taHeight);

		UIDialog dialog = new UIDialog();
		dialog.setSize(taWidth + 20, taHeight + 55);
		dialog.setPositionRelativeTo(display.getObjects());
		dialog.setBorder(Color.black);

		UITextArea taText = new UITextArea(text, 10, 0);
		taText.setBounds(10, 10, taWidth, taHeight);
		taText.setBorder(Color.black);
		dialog.addRelative(taText);

		UITextButton btnOk = new UITextButton("Ok");
		btnOk.setBounds((taWidth - 160) / 2 + 10, taHeight + 20, 160, 25);
		btnOk.setTextColor(Color.white);
		btnOk.setBackground(Color.black);
		btnOk.setClickListener(e -> display.removeLastDialog());
		dialog.addRelative(btnOk);

		display.showDialog(dialog);
	}

	public static void showGraphDialog(Display display, List<Float> values) {
		showGraphDialog(display, values, 500, 500);
	}

	public static void showGraphDialog(Display display, List<Float> values, int graphWidth, int graphHeight) {
		graphWidth = Math.max(160, graphWidth);
		graphHeight = Math.max(80, graphHeight);

		UIDialog dialog = new UIDialog();
		dialog.setSize(graphWidth + 20, graphHeight + 55);
		dialog.setPositionRelativeTo(display.getObjects());
		dialog.setBorder(Color.black);

		UIGraphPanel graph = new UIGraphPanel(values);
		graph.setBounds(10, 10, graphWidth, graphHeight);
		dialog.addRelative(graph);

		UITextButton btnOk = new UITextButton("Ok");
		btnOk.setBounds((graphWidth - 160) / 2 + 10, graphHeight + 20, 160, 25);
		btnOk.setTextColor(Color.white);
		btnOk.setBackground(Color.black);
		btnOk.setClickListener(e -> display.removeLastDialog());
		dialog.addRelative(btnOk);

		display.showDialog(dialog);

	}
}