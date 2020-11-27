package chess.pieces;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.move.Move;
import chess.move.Move.AttackMove;
import chess.move.Move.NormalMove;
import gfx.Assets;

public class Rook extends Piece {
	public Rook(int x, int y, Team team) {
		super(x, y, 50, team, PieceType.ROOK);

		if (team == Team.white)
			this.texture = Assets.white_rook;
		else
			this.texture = Assets.black_rook;
	}

	private Rook(int x, int y, Team team, boolean movedAtLeastOnce) {
		this(x, y, team);
		this.movedAtLeastOnce = movedAtLeastOnce;
	}

	@Override
	public Set<Move> getMoves(Board board) {
		Set<Move> moves = new HashSet<Move>();

		Piece currentPiece;

		boolean nDone = false, sDone = false, wDone = false, eDone = false;

		for (int i = 1; i < 8; i++) {
			if (!nDone && this.y - i >= 0) { // up
				currentPiece = board.getPiece(this.x, this.y - i);

				if (currentPiece == null) {
					moves.add(new NormalMove(board, this, this.x, this.y - i));
				} else if (currentPiece.getTeam() != team) {
					moves.add(new AttackMove(board, this, this.x, this.y - i, currentPiece));
					nDone = true;
				} else {
					nDone = true;
				}
			}

			if (!sDone && this.y + i < 8) { // down
				currentPiece = board.getPiece(this.x, this.y + i);

				if (currentPiece == null) {
					moves.add(new NormalMove(board, this, this.x, this.y + i));
				} else if (currentPiece.getTeam() != team) {
					moves.add(new AttackMove(board, this, this.x, this.y + i, currentPiece));
					sDone = true;
				} else {
					sDone = true;
				}
			}

			if (!wDone && this.x - i >= 0) { // left
				currentPiece = board.getPiece(this.x - i, this.y);

				if (currentPiece == null) {
					moves.add(new NormalMove(board, this, this.x - i, this.y));
				} else if (currentPiece.getTeam() != team) {
					moves.add(new AttackMove(board, this, this.x - i, this.y, currentPiece));
					wDone = true;
				} else {
					wDone = true;
				}
			}

			if (!eDone && this.x + i < 8) { // right
				currentPiece = board.getPiece(this.x + i, this.y);

				if (currentPiece == null) {
					moves.add(new NormalMove(board, this, this.x + i, this.y));
				} else if (currentPiece.getTeam() != team) {
					moves.add(new AttackMove(board, this, this.x + i, this.y, currentPiece));
					eDone = true;
				} else {
					eDone = true;
				}
			}
		}

		return moves;
	}

	@Override
	public Piece movePiece(Move move) {
		return new Rook(move.getPieceDestinationX(), move.getPieceDestinationY(), team, true);
	}
}