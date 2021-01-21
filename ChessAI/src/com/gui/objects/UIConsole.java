package com.gui.objects;

import java.util.ArrayList;
import java.util.List;

public class UIConsole extends UITextArea {
	private static List<UIConsole> consoles = new ArrayList<UIConsole>();

	public UIConsole() {
		super("", 5, 5);
		consoles.add(this);
	}

	public static void log(String text) {
		for (UIConsole console : consoles) {
			console.addText(text);
		}
	}

	private void addText(String text) {
		super.setText(text + "\n" + super.getText());
	}

	@Override
	public void setText(String text) {
	}
}