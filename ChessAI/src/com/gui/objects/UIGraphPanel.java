package com.gui.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.chess.move.Position;
import com.gui.TextObject;
import com.gui.UIUtils;

public class UIGraphPanel extends UIObject {
	private List<Float> values;
	private float horizontalStepSize, verticalStepSize;
	private float minValue, maxValue;
	private int leftGap, bottomGap;
	private int xAxis;
	private List<TextObject> text1;
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
			g.setColor(lineColor);

			if (xAxis >= 0)
				g.drawLine(this.x + leftGap, xAxis, this.x + this.width, xAxis);

			g.drawRect(this.x + leftGap, this.y, width - leftGap, height - bottomGap);

			for (TextObject o : text1) {
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

		if (text1 == null)
			text1 = new ArrayList<TextObject>();
		else
			text1.clear();

		text1.add(new TextObject(maxValue + "",
				new Position(this.x + leftGap - UIUtils.getStringWidth(maxValue + "", font, g), this.y + height)));
		text1.add(new TextObject(minValue + "", new Position(
				this.x + leftGap - UIUtils.getStringWidth(minValue + "", font, g), this.y + this.height - bottomGap)));
		text1.add(new TextObject("1", new Position(this.x + leftGap, this.y + this.height)));
		text1.add(new TextObject(values.size() + "", new Position(
				this.x + this.width - UIUtils.getStringWidth(values.size() + "", font, g), this.y + this.height)));

		if (minValue < 0 && maxValue > 0) {
			xAxis = Math.round(this.y + maxValue * verticalStepSize);
			text1.add(new TextObject("0", new Position(this.x + leftGap - UIUtils.getStringWidth("0", font, g),
					xAxis + UIUtils.getStringHeight(font, g) / 2)));
		} else {
			xAxis = -1;
		}
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