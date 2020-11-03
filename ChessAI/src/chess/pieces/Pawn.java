package chess.pieces;

import java.util.HashSet;
import java.util.Set;

import algorithm.Move;
import chess.ChessBoard;
import gfx.Assets;
import main.Utils;

public class Pawn extends Piece {
	private boolean movedTwoSpaces = false;

	public Pawn(int x, int y, Team team, ChessBoard board) {
		super(x, y, 10, team, board);

		if (team == Team.white)
			this.texture = Assets.white_pawn;
		else
			this.texture = Assets.black_pawn;
	}

	/**
	 * Only used for cloning the piece.
	 * 
	 * @param x
	 * @param y
	 * @param team
	 * @param movedAtLeastOnce
	 * @param board
	 */
	private Pawn(int x, int y, Team team, boolean movedAtLeastOnce, ChessBoard board) {
		super(x, y, 10, team, board);
		this.movedAtLeastOnce = movedAtLeastOnce;
	}

	@Override
	public Piece clone(ChessBoard board) {
		return new Pawn(x, y, team, movedAtLeastOnce, board);
	}

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<Move>();

		Move currentMove = null;
		Piece currentPiece;

		for (int i = -1; i < 2 && Utils.inRange(this.x + i, 0, 7); i += 2) { // check for pieces to hit
			if (team == Team.white && this.y - 1 >= 0)
				currentMove = new Move(this, this.x + i, this.y - 1);
			else if (team == Team.black && this.y + 1 < 8) // black team
				currentMove = new Move(this, this.x + i, this.y + 1);

			currentPiece = board.getPiece(currentMove);
			if (currentPiece != null && currentPiece.getTeam() != team)
				moves.add(currentMove);

			currentMove = new Move(this, this.x + i, this.y); // en passant
			currentPiece = board.getPiece(currentMove);
			if (currentPiece != null && currentPiece.getTeam() != team && currentPiece instanceof Pawn) {
				if (((Pawn) currentPiece).wasMovedTwoSpaces()) {
					if (team == Team.white)
						currentMove = new Move(this, this.x + i, this.y - 1);
					else if (team == Team.black)
						currentMove = new Move(this, this.x + i, this.y + 1);

					if (board.getPiece(currentMove) == null)
						moves.add(currentMove);
				}
			}
		}

		if (team == Team.white) // check for moves one field away
			currentMove = new Move(this, this.x, this.y - 1);
		else // black team
			currentMove = new Move(this, this.x, this.y + 1);

		if (board.getPiece(currentMove) == null)
			moves.add(currentMove);

		if (!movedAtLeastOnce) { // check for moves two fields away
			if (team == Team.white && board.getPiece(this.x, this.y - 1) == null)
				currentMove = new Move(this, this.x, this.y - 2);
			else if (team == Team.black && board.getPiece(this.x, this.y + 1) == null) // black team
				currentMove = new Move(this, this.x, this.y + 2);

			if (board.getPiece(currentMove) == null)
				moves.add(currentMove);
		}

		return moves;
	}

	@Override
	public String getName() {
		return "Pawn";
	}

	@Override
	public void setPosition(int newX, int newY) {
		if (this.y - newY == -2 || this.y - newY == 2) {
			movedTwoSpaces = true;
		} else {
			movedTwoSpaces = false;
		}

		if (this.x - newX != 0 && this.y - newY != 0 && board.getPiece(newX, newY) == null) {
			// en passant
			board.removePiece(newX, this.y);
		}
		System.out.println(newX + "|" + newY + "|" + (board.getPiece(newX, newY) == null));

		super.setPosition(newX, newY);
	}

	public boolean wasMovedTwoSpaces() {
		return movedTwoSpaces;
	}
}