package com.gui.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.chess.move.Position;
import com.gui.TextObject;
import com.gui.UIUtils;
import com.gui.interfaces.Clickable;
import com.main.Utils;

public class UIGraphPanel extends UIObject implements Clickable {
	private List<Float> values;
	private float horizontalStepSize, verticalStepSize;
	private float minValue, maxValue;
	private int leftGap, bottomGap;
	private int xAxis;
	private int selectedValue = -1;
	private List<TextObject> text;
	private Color lineColor = STANDARD_TEXT_COLOR, graphColor = Color.blue;

	public UIGraphPanel() {
		this(null);
	}

	public UIGraphPanel(List<Float> values) {
		setValues(values);
	}

	@Override
	public void render(Graphics g) {
		drawBackground();

		if (!values.isEmpty()) {
			if (selectedValue >= 0) { // draw lines from currently selected value
				g.setColor(new Color(63, 63, 63, 63));

				int x = this.x + Math.round(selectedValue * horizontalStepSize) + leftGap;
				g.drawLine(x, this.y, x, this.y + this.height - bottomGap);

				int y = this.y + this.height
						- Math.round((float) (values.get(selectedValue) - minValue) * verticalStepSize) - bottomGap;
				g.drawLine(this.x + leftGap, y, this.x + this.width, y);
			}

			g.setColor(lineColor);

			if (xAxis >= 0) // draw x axis
				g.drawLine(this.x + leftGap, xAxis, this.x + this.width, xAxis);

			g.drawRect(this.x + leftGap, this.y, width - leftGap, height - bottomGap);

			for (TextObject o : text) {
				o.render(g, textColor, font);
			}

			g.setColor(graphColor);
			int size = values.size();
			for (int i = 0; i < size - 1; i++) {
				int x1 = this.x + Math.round(i * horizontalStepSize) + leftGap;
				int y1 = this.y + this.height - Math.round((float) (values.get(i) - minValue) * verticalStepSize)
						- bottomGap;
				int x2 = Math.round(x1 + horizontalStepSize);
				int y2 = this.y + this.height - Math.round((float) (values.get(i + 1) - minValue) * verticalStepSize)
						- bottomGap;
				g.drawLine(x1, y1, x2, y2);
			}
		} else {
			UIUtils.drawString(g, "No data to display!", this.x + this.width / 2, this.y + this.height / 2, true,
					textColor, font);
		}

		drawBorder();
	}

	@Override
	public void propertyChanged() {
		calculateGraphDimensions();
		repaint();
	}

	private void calculateGraphDimensions() {
		if (g == null || values == null)
			return;

		minValue = Float.MAX_VALUE;
		maxValue = Float.MIN_VALUE;

		for (float f : values) {
			minValue = Math.min(minValue, f);
			maxValue = Math.max(maxValue, f);
		}

		int height = UIUtils.getStringHeight(font, g);

		leftGap = (int) Math.round(Math.max(UIUtils.getStringWidth(minValue + "", font, g),
				UIUtils.getStringWidth(maxValue + "", font, g)));
		bottomGap = height;

		horizontalStepSize = (float) (width - leftGap) / (float) (values.size() - 1);

		verticalStepSize = (float) (this.height - bottomGap) / (maxValue - minValue);

		if (text == null)
			text = new ArrayList<TextObject>();
		else
			text.clear();

		text.add(new TextObject(maxValue + "",
				new Position(this.x + leftGap - UIUtils.getStringWidth(maxValue + "", font, g), this.y + height)));
		text.add(new TextObject(minValue + "", new Position(
				this.x + leftGap - UIUtils.getStringWidth(minValue + "", font, g), this.y + this.height - bottomGap)));
//		text.add(new TextObject("1", new Position(this.x + leftGap, this.y + this.height)));
//		text.add(new TextObject(values.size() + "", new Position(
//				this.x + this.width - UIUtils.getStringWidth(values.size() + "", font, g), this.y + this.height)));

		if (minValue < 0 && maxValue > 0) {
			xAxis = Math.round(this.y + maxValue * verticalStepSize);
			text.add(new TextObject("0", new Position(this.x + leftGap - UIUtils.getStringWidth("0", font, g),
					xAxis + UIUtils.getStringHeight(font, g) / 2)));
		} else {
			xAxis = -1;
		}
	}

	@Override
	public void onMouseMove(MouseEvent e) {
		if (!boundsContain(e.getX(), e.getY()) || !enabled || !visible || values == null || values.isEmpty())
			return;

		calculateGraphDimensions();
		int nearestValue = Utils.clamp(getNearestValue(e.getX()), 0, values.size() - 1);
		this.selectedValue = nearestValue;
		String text = (nearestValue + 1) + "|" + values.get(nearestValue);
		int x = Math.round(
				this.x + leftGap + nearestValue * horizontalStepSize - UIUtils.getStringWidth(text, font, g) / 2);
		x = Math.max(x, this.x + leftGap); // check left bound
		x = Math.min(x, this.x + this.width - UIUtils.getStringWidth(text, font, g)); // check right bound
		int y = Math.round(this.y + this.height - bottomGap + UIUtils.getStringHeight(font, g) - 4f);
		this.text.add(new TextObject(text, new Position(x, y)));
		repaint();
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
	}

	private int getNearestValue(int absoluteX) {
		int relativeX = absoluteX - this.x - leftGap;

		int nearestValue = Math.round(relativeX / horizontalStepSize);

		return nearestValue;
	}

	// ===== Getters ===== \\
	public List<Float> getValues() {
		return values;
	}

	public Color getLineColor() {
		return lineColor;
	}

	// ===== Setters ===== \\
	public void setValues(List<Float> values) {
		if (this.values == null || !this.values.equals(values)) {
			this.values = values;
			propertyChanged();
		}
	}

	public void setLineColor(Color lineColor) {
		if (this.lineColor == null || !this.lineColor.equals(lineColor)) {
			this.lineColor = lineColor;
			propertyChanged();
		}
	}
}