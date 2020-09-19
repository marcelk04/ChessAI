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

	/**
	 * Returns all the moves this piece can possibly do in its position.
	 * 
	 * @return all possible moves.
	 */
	public abstract Set<Move> getMoves();

	/**
	 * Creates a piece that has exactly the same attributes while still being a
	 * different object. This method includes a ChessBoard in the parameters since
	 * the board used for the constructor also has to be the cloned version,
	 * otherwise there would be to pieces on the same board.
	 * 
	 * @param board the board the piece should be on.
	 * @return the cloned piece.
	 */
	public abstract Piece clone(ChessBoard board);

	/**
	 * Returns the name of the piece. Used for example for displaying the moves.
	 * 
	 * @return the name of the piece.
	 */
	public abstract String getName();

	/**
	 * Moves the piece to a new position.
	 * 
	 * @param newX the new x coordinate of the piece.
	 * @param newY the new y coordinate of the piece.
	 */
	public void setPosition(int newX, int newY) {
		this.x = newX;
		this.y = newY;
		movedAtLeastOnce = true;
	}

	/**
	 * Checks if the possible moves of this piece contain a move. Also see
	 * {@link Piece#getMoves}.
	 * 
	 * @param move the move to be checked.
	 * @return {@code true} if the move can be executed by this piece.
	 */
	public boolean movesContain(Move move) {
		Set<Move> moves = this.getMoves();

		for (Move m : moves) {
			if (move.equals(m))
				return true;
		}

		return false;
	}

	/**
	 * See {@link Piece#movesContain(Move)}.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean movesContain(int x, int y) {
		return movesContain(new Move(this, x, y));
	}
	
	// ===== Getters & Setters ===== \\
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
}