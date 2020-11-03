package algorithm;

import chess.pieces.Piece;

public class Move {
	private Piece piece;
	private int oldX, oldY;
	private int newX, newY;

	/**
	 * Creates a new move.
	 * 
	 * @param piece the piece that will be moved.
	 * @param newX  the x coordinate the piece will be moved to.
	 * @param newY  the y coordinate the piece will be moved to.
	 */
	public Move(Piece piece, int newX, int newY) {
		this.piece = piece;
		this.oldX = piece.getX();
		this.oldY = piece.getY();
		this.newX = newX;
		this.newY = newY;
	}

	/**
	 * Moves the piece to its new position.
	 */
	public void updatePiecePosition() {
		piece.setPosition(newX, newY);
	}

	/**
	 * Returns what piece is moved and to which tile.
	 * <p>
	 * For example the bottom left rook moved to the top left tile would be
	 * <p>
	 * Ra1-a8.
	 * 
	 * @return the important data about the move.
	 */
	public String getData() {
		String[] columns = { "a", "b", "c", "d", "e", "f", "g", "h" };
		String text = columns[oldX] + (8 - oldY) + "-" + columns[newX] + (8 - newY);
		if (!piece.getName().equals("Pawn"))
			text = piece.getName().charAt(0) + text;
		text += "|" + piece.getTeam();
		return text;
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof Move))
			return false;
		Move move = (Move) o;
		if (this.oldX != move.getOldX() || this.oldY != move.getOldY() || this.newX != move.getNewX()
				|| this.newY != move.getNewY() || this.piece != move.getPiece())
			return false;
		return true;
	}

	public Piece getPiece() {
		return piece;
	}

	public int getOldX() {
		return oldX;
	}

	public int getOldY() {
		return oldY;
	}

	public int getNewX() {
		return newX;
	}

	public int getNewY() {
		return newY;
	}
}