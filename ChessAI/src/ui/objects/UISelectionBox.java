package ui.objects;

import java.awt.event.MouseEvent;

import main.Utils;

public class UISelectionBox<E> extends UITextButton {
	private Object[] elements;
	private int index;

	public UISelectionBox(Object[] elements) {
		super(elements != null && elements.length > 0 ? elements[0].toString() : "");

		this.elements = elements;
		this.index = 0;
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		if (hovering)
			displayNext();

		super.onMouseRelease(e);
	}

	private void displayNext() {
		if (elements == null)
			return;

		if (index == elements.length - 1)
			index = 0;
		else
			index++;

		setText(elements[index].toString());
	}

	// ===== Getters ===== \\
	public Object[] getElements() {
		return elements;
	}

	public int getSelectedIndex() {
		return index;
	}

	@SuppressWarnings("unchecked")
	public E getSelectedElement() {
		return (E) elements[index];
	}

	// ===== Setters ===== \\
	public void setElements(Object[] elements) {
		this.elements = elements;
		if (elements != null && elements.length > 0)
			setText(elements[0].toString());
		else
			setText("");

		index = 0;
	}

	public void setSelectedIndex(int index) {
		if (elements != null && Utils.inRange(index, 0, elements.length)) {
			this.index = index;
			setText(elements[index].toString());
		}
	}
}