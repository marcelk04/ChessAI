package com.chess.pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.main.Utils;

public abstract class Piece {
	public static final int PIECE_WIDTH = 80, PIECE_HEIGHT = 80;

	protected final int position;
	protected final int x, y;
	protected final Team team;
	protected final boolean movedAtLeastOnce;
	protected final PieceType type;
	protected BufferedImage texture;

	public Piece(int position, Team team, PieceType type, boolean movedAtLeastOnce) {
		this.position = position;
		this.x = Utils.getX(position);
		this.y = Utils.getY(position);
		this.team = team;
		this.type = type;
		this.movedAtLeastOnce = movedAtLeastOnce;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof Piece))
			return false;

		Piece otherPiece = (Piece) other;

		return this.position == otherPiece.getPosition() && this.team == otherPiece.getTeam();
	}

	@Override
	public int hashCode() {
		int result = team.hashCode();
		result = 10 * result + position;
		result = 10 * result + type.hashCode();
		result = 10 * result + team.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return type.toString() + ";pos=" + position + ";" + team.toString() + ";movedAtLeastOnce=" + movedAtLeastOnce;
	}

	public void render(Graphics g, int x, int y, int width, int height) {
		g.drawImage(texture, x, y, width, height, null);
	}

	public abstract List<Move> getMoves(Board board);

	public abstract Piece movePiece(Move move);

	public abstract int positionBonus();

	// ===== Getters ===== \\
	public Team getTeam() {
		return team;
	}

	public int getPosition() {
		return position;
	}

	public int getValue() {
		return type.getValue();
	}

	public boolean gotMovedAtLeastOnce() {
		return movedAtLeastOnce;
	}

	public PieceType getType() {
		return type;
	}

	// ===== Inner Classes ===== \\
	public enum PieceType {
		KING('K', "King", 9000), QUEEN('Q', "Queen", 900), ROOK('R', "Rook", 460), BISHOP('B', "Bishop", 320),
		KNIGHT('N', "Knight", 310), PAWN('P', "Pawn", 100);

		private final char letter;
		private final String name;
		private final int value;

		private PieceType(final char letter, final String name, final int value) {
			this.letter = letter;
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {
			return name;
		}

		// ===== Getters ===== \\
		public char getLetter() {
			return letter;
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}
	}
}