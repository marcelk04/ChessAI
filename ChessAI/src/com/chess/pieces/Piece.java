package com.chess.pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.main.Utils;

/**
 * All valid pieces on the board must extends the Piece class. It can contain
 * almost all relevant data about all the pieces with getters and important
 * abstract methods already in place.
 * 
 * @author DefensivLord
 */
public abstract class Piece {
	protected final int position;
	protected final int x, y;
	protected final Team team;
	protected final boolean movedAtLeastOnce;
	protected final PieceType type;
	protected BufferedImage texture;

	/**
	 * The default constructor for instances of the class Piece.
	 * 
	 * @param position         the position of the piece.
	 * @param team             the team of the piece.
	 * @param type             the type of the piece.
	 * @param movedAtLeastOnce whether the piece has already been moved.
	 */
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

	/**
	 * Draws the piece.
	 * 
	 * @param g      the graphics object.
	 * @param x      the x coordinate.
	 * @param y      the y coordinate.
	 * @param width  the width.
	 * @param height the height.
	 */
	public void render(Graphics g, int x, int y, int width, int height) {
		g.drawImage(texture, x, y, width, height, null);
	}

	/**
	 * Generates a {@link List} of all possible moves for this specific piece.
	 * 
	 * @param board the board used for generating the moves.
	 * @return a {@link List} of all possible moves.
	 */
	public abstract List<Move> getMoves(Board board);

	/**
	 * Moves the piece to the location specified by the {@link Move}.
	 * 
	 * @param move the executed move.
	 * @return the moved piece.
	 */
	public abstract Piece movePiece(Move move);

	/**
	 * Returns the position bonus for this piece of this specific team on this
	 * specific coordinate.
	 * 
	 * @return the position bonus.
	 */
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
		KING('K', "King", 90000), QUEEN('Q', "Queen", 1000), ROOK('R', "Rook", 500), BISHOP('B', "Bishop", 325),
		KNIGHT('N', "Knight", 325), PAWN('P', "Pawn", 100);

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