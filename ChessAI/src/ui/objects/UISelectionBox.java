package ui.objects;

import java.awt.event.MouseEvent;

import main.Utils;

/**
 * The class UISelectionBox is a type of textbutton that changes its text when
 * it is pressed. Since it is a generic class, it can hold any object, not just
 * Strings, however the toString() method is used to get the display text.
 *
 * @param <E> the type of the box.
 * 
 * @author DefensivLord
 */
public class UISelectionBox<E> extends UITextButton {
	/**
	 * An array of the elements the box is holding.
	 */
	private Object[] elements;

	/**
	 * The index of the object that is currently displayed.
	 */
	private int index;

	/**
	 * The default constructor for instances of the class UISelectionBox.
	 * 
	 * @param elements the elements the box should be holding.
	 */
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

	/**
	 * Sets the text of the button to the next element.
	 */
	private void displayNext() {
		if (elements == null)
			return;

		if (index == elements.length - 1)
			index = 0;
		else
			index++;

		text = elements[index].toString();
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
			text = elements[0].toString();
		else
			text = "";

		index = 0;
	}

	public void setSelectedIndex(int index) {
		if (elements != null && Utils.inRange(index, 0, elements.length - 1)) {
			text = elements[this.index = index].toString();
		}
	}
}