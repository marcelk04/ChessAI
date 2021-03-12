package com.gui.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.chess.move.Position;
import com.chess.pieces.Piece;
import com.chess.pieces.Team;
import com.gfx.Assets;
import com.gui.UIUtils;

public class UIPiecesPanel extends UIObject {
	private int hgap, vgap;
	private int cols;

	private int imageWidth, imageHeight;

	private final List<Piece> whitePieces, blackPieces;
	private final List<Position> whitePositions, blackPositions;

	public UIPiecesPanel(int cols) {
		this(10, 10, cols);
	}

	public UIPiecesPanel(int hgap, int vgap, int cols) {
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
		whitePositions = new ArrayList<Position>();
		blackPositions = new ArrayList<Position>();

		this.hgap = hgap;
		this.vgap = vgap;
		this.cols = Math.max(cols, 1);

		propertyChanged();
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			drawBackground();

			for (int i = 0; i < Math.min(whitePieces.size(), whitePositions.size()); i++) {
				Position pos = whitePositions.get(i);
				whitePieces.get(i).render(g, pos.x, pos.y, imageWidth, imageHeight);
			}

			for (int i = 0; i < Math.min(blackPieces.size(), blackPositions.size()); i++) {
				Position pos = blackPositions.get(i);
				blackPieces.get(i).render(g, pos.x, pos.y, imageWidth, imageHeight);
			}

			drawBorder();
		}
	}

	private void calculateImageDimensions() {
		whitePositions.clear();
		blackPositions.clear();

		final float maxImageWidth = (float) (width - hgap * (cols + 1)) / (float) cols;
		final int maxRows = (int) Math.ceil(16 / (float) cols);
		final float maxImageHeight = (float) (height - vgap * (maxRows + 1)) / (float) maxRows / 2f;
		final Rectangle offset = UIUtils.fitImage(Assets.white_king, 0, 0, Math.round(maxImageWidth),
				Math.round(maxImageHeight), false);

		imageWidth = offset.width;
		imageHeight = offset.height;

		for (int y = 0; y < maxRows; y++) {
			for (int x = 0; x < cols; x++) {
				int imageX = this.x + hgap + Math.round((maxImageWidth + hgap) * x) + offset.x;
				int imageY = this.y + vgap + Math.round((maxImageHeight + vgap) * y) + offset.y;
				whitePositions.add(new Position(imageX, imageY));
				blackPositions.add(new Position(imageX, imageY + height / 2));
			}
		}
	}

	@Override
	protected void propertyChanged() {
		calculateImageDimensions();
		repaint();
	}

	public Piece addPiece(Piece piece) {
		if (piece.getTeam() == Team.WHITE) {
			whitePieces.add(piece);
		} else {
			blackPieces.add(piece);
		}
		repaint();
		return piece;
	}

	public void removePiece(Piece piece) {
		if (piece.getTeam() == Team.WHITE) {
			whitePieces.remove(piece);
		} else {
			blackPieces.remove(piece);
		}
		repaint();
	}

	public void clear() {
		whitePieces.clear();
		blackPieces.clear();
		repaint();
	}

	// ===== Getters ===== \\
	public int getHgap() {
		return hgap;
	}

	public int getVgap() {
		return vgap;
	}

	public int getCols() {
		return cols;
	}

	// ===== Setters ===== \\
	public void setHgap(int hgap) {
		if (this.hgap != hgap) {
			this.hgap = hgap;
			propertyChanged();
		}
	}

	public void setVgap(int vgap) {
		if (this.vgap != vgap) {
			this.vgap = vgap;
			propertyChanged();
		}
	}

	public void setCols(int cols) {
		if (this.cols != cols) {
			this.cols = Math.max(cols, 1);
			propertyChanged();
		}
	}
}