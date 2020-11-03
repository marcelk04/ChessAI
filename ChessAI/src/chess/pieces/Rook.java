package chess.pieces;

import java.util.HashSet;
import java.util.Set;

import algorithm.Move;
import chess.ChessBoard;
import gfx.Assets;

public class Rook extends Piece {
	public Rook(int x, int y, Team team, ChessBoard board) {
		super(x, y, 50, team, board);

		if (team == Team.white)
			this.texture = Assets.white_rook;
		else
			this.texture = Assets.black_rook;
	}

	private Rook(int x, int y, Team team, boolean movedAtLeastOnce, ChessBoard board) {
		super(x, y, 50, team, board);
		this.movedAtLeastOnce = movedAtLeastOnce;
	}

	@Override
	public Piece clone(ChessBoard board) {
		return new Rook(x, y, team, board);
	}

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<Move>();

		Move currentMove;
		Piece currentPiece;

		boolean nDone = false, sDone = false, wDone = false, eDone = false;

		for (int i = 1; i < 8; i++) {
			if (!nDone && this.y - i >= 0) { // up
				currentMove = new Move(this, this.x, this.y - i);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null) {
					moves.add(currentMove);
				} else if (currentPiece.getTeam() != team) {
					moves.add(currentMove);
					nDone = true;
				} else {
					nDone = true;
				}
			}

			if (!sDone && this.y + i < 8) { // down
				currentMove = new Move(this, this.x, this.y + i);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null) {
					moves.add(currentMove);
				} else if (currentPiece.getTeam() != team) {
					moves.add(currentMove);
					sDone = true;
				} else {
					sDone = true;
				}
			}

			if (!wDone && this.x - i >= 0) { // left
				currentMove = new Move(this, this.x - i, this.y);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null) {
					moves.add(currentMove);
				} else if (currentPiece.getTeam() != team) {
					moves.add(currentMove);
					wDone = true;
				} else {
					wDone = true;
				}
			}
			
			if (!eDone && this.x + i < 8) { // right
				currentMove = new Move(this, this.x + i, this.y);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null) {
					moves.add(currentMove);
				} else if (currentPiece.getTeam() != team) {
					moves.add(currentMove);
					eDone = true;
				} else {
					eDone = true;
				}
			}
		}

		return moves;
	}

	@Override
	public String getName() {
		return "Rook";
	}
}