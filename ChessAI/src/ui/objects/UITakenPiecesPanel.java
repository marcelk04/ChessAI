package ui.objects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import chess.pieces.Piece;
import chess.pieces.Team;

public class UITakenPiecesPanel extends UIObject {
	private List<Piece> whitePieces, blackPieces;
	private double maxPieceWidth, maxPieceHeight;
	private double pieceWidth, pieceHeight;
	private double xOffset, yOffset;
	private int blackBegin;
	private int hgap, vgap;

	public UITakenPiecesPanel() {
		this(10, 10);
	}

	public UITakenPiecesPanel(int hgap, int vgap) {
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
		setHgap(hgap);
		setVgap(vgap);
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			if (background != null) {
				g.setColor(background);
				g.fillRect(x, y, width, height);
			}

			int pieceX, pieceY;

			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 8; y++) {
					int pieceNumber = x * 8 + y;

					if (pieceNumber < whitePieces.size()) {
						pieceX = (int) Math.round(this.x + hgap + (hgap + maxPieceWidth) * x + xOffset);
						pieceY = (int) Math.round(this.y + vgap + (vgap + maxPieceHeight) * y + yOffset);

						Piece p = whitePieces.get(pieceNumber);

						p.render(g, pieceX, pieceY, (int) Math.round(pieceWidth), (int) Math.round(pieceHeight));
					}

					if (pieceNumber < blackPieces.size()) {
						pieceX = (int) Math.round(this.x + hgap + (hgap + maxPieceWidth) * x + xOffset);
						pieceY = (int) Math.round(this.y + vgap + (vgap + maxPieceHeight) * y + yOffset + blackBegin);

						Piece p = blackPieces.get(pieceNumber);

						p.render(g, pieceX, pieceY, (int) Math.round(pieceWidth), (int) Math.round(pieceHeight));
					}
				}
			}

			if (border != null) {
				g.setColor(border);
				g.drawRect(x, y, width, height);
			}
		}
	}

	private void calculatePieceDimensions() {
		maxPieceWidth = (double) (width - hgap * 3) / 2d;
		maxPieceHeight = (double) (height - vgap * 19) / 16d;

		double originalPieceWidth = Piece.PIECE_WIDTH;
		double originalPieceHeight = Piece.PIECE_HEIGHT;

		double widthDiff = maxPieceWidth - originalPieceWidth;
		double heightDiff = maxPieceHeight - originalPieceHeight;

		double factor;

		if (widthDiff < heightDiff) {
			factor = widthDiff / originalPieceWidth + 1;
		} else {
			factor = heightDiff / originalPieceHeight + 1;
		}

		pieceWidth = originalPieceWidth * factor;
		pieceHeight = originalPieceHeight * factor;

		xOffset = (maxPieceWidth - pieceWidth) / 2;
		yOffset = (maxPieceHeight - pieceHeight) / 2;

		blackBegin = (int) Math.round(vgap + (vgap + maxPieceHeight) * 8);
	}

	public void addPiece(Piece p) {
		if (p != null) {
			(p.getTeam() == Team.WHITE ? whitePieces : blackPieces).add(p);
		}
	}

	public void removePiece(Piece p) {
		whitePieces.remove(p);
		blackPieces.remove(p);
	}

	// ===== Getters ===== \\
	public List<Piece> getWhitePieces() {
		return whitePieces;
	}

	public List<Piece> getBlackPieces() {
		return blackPieces;
	}

	public int getHgap() {
		return hgap;
	}

	public int getVgap() {
		return vgap;
	}

	// ===== Setters ===== \\
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		calculatePieceDimensions();
	}

	public void setWhitePieces(List<Piece> whitePieces) {
		this.whitePieces = whitePieces;
	}

	public void setBlackPieces(List<Piece> blackPieces) {
		this.blackPieces = blackPieces;
	}

	public void setHgap(int hgap) {
		if (hgap < 0)
			hgap *= -1;
		this.hgap = hgap;
		calculatePieceDimensions();
	}

	public void setVgap(int vgap) {
		if (vgap < 0)
			vgap *= -1;
		this.vgap = vgap;
		calculatePieceDimensions();
	}
}