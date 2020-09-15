package chess.pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Set;

import algorithm.Move;
import chess.board.ChessBoard;

public abstract class Piece {
	public static final int PIECE_WIDTH = 80, PIECE_HEIGHT = 80;

	protected int x, y;
	protected final int value;
	protected Team team;
	protected BufferedImage texture;
	protected ChessBoard board;
	protected boolean movedAtLeastOnce = false;

	public Piece(int x, int y, int value, Team team, ChessBoard board) {
		this.x = x;
		this.y = y;
		this.value = value;
		this.team = team;
		this.board = board;
	}

	public void render(Graphics g) {
		g.drawImage(texture, x * PIECE_WIDTH, y * PIECE_HEIGHT, PIECE_WIDTH, PIECE_HEIGHT, null);
	}

	public abstract Set<Move> getMoves();

	public abstract Piece clone(ChessBoard board);

	public abstract String getName();

	public void setPosition(int newX, int newY) {
		this.x = newX;
		this.y = newY;
		movedAtLeastOnce = true;
	}

	public boolean movesContain(Move move) {
		Set<Move> moves = this.getMoves();

		for (Move m : moves) {
			if (move.isEqualTo(m))
				return true;
		}

		return false;
	}

	public boolean movesContain(int x, int y) {
		return movesContain(new Move(this, x, y));
	}

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

	public static enum Team {
		black, white;
	}
}