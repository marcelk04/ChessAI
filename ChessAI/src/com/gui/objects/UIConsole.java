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
		if (super.text != null && !super.text.equals(""))
			super.setText(super.text + "\n" + text);
		else
			super.setText(text);
	}

	@Override
	public void setText(String text) {
	}
}