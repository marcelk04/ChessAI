package ui.objects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chess.move.Position;
import chess.pieces.Piece;
import chess.pieces.Team;

public class UITakenPiecesPanel extends UIObject {
	private List<Piece> whitePieces, blackPieces;
	private Map<Piece, Position> positions;
	private int pieceWidth, pieceHeight;
	private int hgap, vgap;

	public UITakenPiecesPanel() {
		this(10, 10);
	}

	public UITakenPiecesPanel(int hgap, int vgap) {
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
		positions = new HashMap<Piece, Position>();
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

			Position pos;

			for (int i = 0; i < whitePieces.size(); i++) {
				pos = positions.get(whitePieces.get(i));

				whitePieces.get(i).render(g, pos.x, pos.y, pieceWidth, pieceHeight);
			}

			for (int i = 0; i < blackPieces.size(); i++) {
				pos = positions.get(blackPieces.get(i));

				blackPieces.get(i).render(g, pos.x, pos.y, pieceWidth, pieceHeight);
			}

			if (border != null) {
				g.setColor(border);
				g.drawRect(x, y, width, height);
			}
		}
	}

	private void calculatePieceDimensions() {
		float maxPieceWidth = (float) (width - hgap * 3) / 2f; // 2 pieces per row
		float maxPieceHeight = (float) (height - vgap * 19) / 16f; // 16 pieces per column

		float originalPieceWidth = Piece.PIECE_WIDTH;
		float originalPieceHeight = Piece.PIECE_HEIGHT;

		float widthDiff = maxPieceWidth - originalPieceWidth;
		float heightDiff = maxPieceHeight - originalPieceHeight;

		float factor;

		if (widthDiff < heightDiff) {
			factor = widthDiff / originalPieceWidth + 1f;
		} else {
			factor = heightDiff / originalPieceHeight + 1f;
		}

		float pieceWidth = originalPieceWidth * factor;
		float pieceHeight = originalPieceHeight * factor;

		this.pieceWidth = Math.round(pieceWidth);
		this.pieceHeight = Math.round(pieceHeight);

		float xOffset = (maxPieceWidth - pieceWidth) / 2f;
		float yOffset = (maxPieceHeight - pieceHeight) / 2f;

		int blackBegin = Math.round(vgap + (vgap + maxPieceHeight) * 8);

		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 8; y++) {
				int pieceNumber = x * 8 + y;

				if (pieceNumber >= Math.max(whitePieces.size(), blackPieces.size()))
					break;

				if (pieceNumber < whitePieces.size()) {
					int pieceX = Math.round(this.x + hgap + (hgap + maxPieceWidth) * x + xOffset);
					int pieceY = Math.round(this.y + vgap + (vgap + maxPieceHeight) * y + yOffset);

					Piece p = whitePieces.get(pieceNumber);

					positions.put(p, new Position(pieceX, pieceY));
				}

				if (pieceNumber < blackPieces.size()) {
					int pieceX = Math.round(this.x + hgap + (hgap + maxPieceWidth) * x + xOffset);
					int pieceY = Math.round(this.y + vgap + (vgap + maxPieceHeight) * y + yOffset + blackBegin);

					Piece p = blackPieces.get(pieceNumber);

					positions.put(p, new Position(pieceX, pieceY));
				}
			}
		}
	}

	public void addPiece(Piece p) {
		if (p != null) {
			(p.getTeam() == Team.WHITE ? whitePieces : blackPieces).add(p);
			calculatePieceDimensions();
		}
	}

	public void removePiece(Piece p) {
		whitePieces.remove(p);
		blackPieces.remove(p);
	}

	public void clear() {
		whitePieces.clear();
		blackPieces.clear();
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