package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Board;
import chess.move.Move;
import chess.move.Move.AttackMove;
import chess.move.Move.NormalMove;
import gfx.Assets;

public class Rook extends Piece {
	public Rook(int x, int y, Team team) {
		this(x, y, team, false);
	}

	public Rook(int x, int y, Team team, boolean movedAtLeastOnce) {
		super(x, y, 50, team, PieceType.ROOK);
		this.movedAtLeastOnce = movedAtLeastOnce;
		this.texture = team == Team.WHITE ? Assets.white_rook : Assets.black_rook;
	}

	@Override
	public List<Move> getMoves(Board board) {
		List<Move> moves = new ArrayList<Move>();

		Piece currentPiece;

		boolean nDone = false, sDone = false, wDone = false, eDone = false;

		for (int i = 1; i < 8; i++) {
			if (!nDone && this.y - i >= 0) { // north
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

			if (!sDone && this.y + i < 8) { // south
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

			if (!wDone && this.x - i >= 0) { // east
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

			if (!eDone && this.x + i < 8) { // south
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