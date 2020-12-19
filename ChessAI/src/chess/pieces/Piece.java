package chess.pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import chess.Board;
import chess.move.Move;

public abstract class Piece {
	public static final int PIECE_WIDTH = 80, PIECE_HEIGHT = 80;

	protected int x, y;
	protected final int value;
	protected Team team;
	protected BufferedImage texture;
	protected boolean movedAtLeastOnce = false;
	protected PieceType type;

	public Piece(int x, int y, int value, Team team, PieceType type) {
		this.x = x;
		this.y = y;
		this.value = value;
		this.team = team;
		this.type = type;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof Piece))
			return false;

		Piece otherPiece = (Piece) other;

		return this.x == otherPiece.getX() && this.y == otherPiece.getY() && this.team == otherPiece.getTeam();
	}

	@Override
	public int hashCode() {
		int result = team.hashCode();
		result = 10 * result + x;
		result = 10 * result + y;
		result = 10 * result + type.hashCode();
		result = 10 * result + team.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return type.toString() + ";x=" + x + ";y=" + y + ";" + team.toString() + ";movedAtLeastOnce="
				+ movedAtLeastOnce;
	}

	public void render(Graphics g, int x, int y, int width, int height) {
		g.drawImage(texture, x, y, width, height, null);
	}

	public abstract List<Move> getMoves(Board board);

	public abstract Piece movePiece(Move move);

	// ===== Getters ===== \\
	public Team getTeam() {
		return team;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getValue() {
		return value;
	}

	public boolean gotMovedAtLeastOnce() {
		return movedAtLeastOnce;
	}

	public PieceType getType() {
		return type;
	}
}