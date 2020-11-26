package ui.objects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ui.UIUtils;

public class UIList<E> extends UIObject {
	private List<E> elements;
	private List<String> displayElements;
	private int beginIndex, endIndex;
	private int xOffset = 2, yOffset = 2;
	private int stringLength;

	public UIList() {
		elements = new ArrayList<E>();
		displayElements = new ArrayList<String>();
	}

	public UIList(E[] elements) {
		this();
		double length = width - xOffset / 2;
		for (E e : elements) {
			this.elements.add(e);
			this.displayElements.add(UIUtils.shortenText(e.toString(), length, font));
		}
	}

	public UIList(Collection<? extends E> elements) {
		this();
		this.elements.addAll(elements);

		double length = width - xOffset / 2;
		for (E e : elements) {
			this.displayElements.add(UIUtils.shortenText(e.toString(), length, font));
		}
	}

	@Override
	public void render(Graphics g) {
	}

	public void add(E e) {
		elements.add(e);
	}

	private void calculateIndices() {
		double totalHeight = 0;
		for (String s : displayElements) {
			totalHeight += UIUtils.getStringHeight(s, font);
		}
		if (totalHeight < height - xOffset * 2) {

		}
	}
}