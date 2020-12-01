package chess.pieces;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.move.Move;
import chess.move.Move.AttackMove;
import chess.move.Move.NormalMove;
import gfx.Assets;

public class Bishop extends Piece {
	public Bishop(int x, int y, Team team) {
		this(x, y, team, false);
	}

	private Bishop(int x, int y, Team team, boolean movedAtLeastOnce) {
		super(x, y, 30, team, PieceType.BISHOP);
		this.movedAtLeastOnce = movedAtLeastOnce;
		this.texture = team == Team.WHITE ? Assets.white_bishop : Assets.black_bishop;
	}

	@Override
	public Set<Move> getMoves(Board board) {
		final Set<Move> moves = new HashSet<Move>();

		Piece currentPiece;

		boolean nwDone = false, neDone = false, seDone = false, swDone = false;

		for (int i = 1; i < 7; i++) {
			if (!nwDone && this.x - i >= 0 && this.y - i >= 0) { // northwest / left up
				currentPiece = board.getPiece(this.x - i, this.y - i);

				if (currentPiece == null) {
					moves.add(new NormalMove(board, this, this.x - i, this.y - i));
				} else if (currentPiece.getTeam() != team) {
					moves.add(new AttackMove(board, this, this.x - i, this.y - i, currentPiece));
					nwDone = true;
				} else {
					nwDone = true;
				}
			}

			if (!seDone && this.x + i < 8 && this.y + i < 8) { // southeast / right down
				currentPiece = board.getPiece(this.x + i, this.y + i);

				if (currentPiece == null) {
					moves.add(new NormalMove(board, this, this.x + i, this.y + i));
				} else if (currentPiece.getTeam() != team) {
					moves.add(new AttackMove(board, this, this.x + i, this.y + i, currentPiece));
					seDone = true;
				} else {
					seDone = true;
				}
			}

			if (!swDone && this.x - i >= 0 && this.y + i < 8) { // southwest / left down
				currentPiece = board.getPiece(this.x - i, this.y + i);

				if (currentPiece == null) {
					moves.add(new NormalMove(board, this, this.x - i, this.y + i));
				} else if (currentPiece.getTeam() != team) {
					moves.add(new AttackMove(board, this, this.x - i, this.y + i, currentPiece));
					swDone = true;
				} else {
					swDone = true;
				}
			}

			if (!neDone && this.x + i < 8 && this.y - i >= 0) { // northeast / right up
				currentPiece = board.getPiece(this.x + i, this.y - i);

				if (currentPiece == null) {
					moves.add(new NormalMove(board, this, this.x + i, this.y - i));
				} else if (currentPiece.getTeam() != team) {
					moves.add(new AttackMove(board, this, this.x + i, this.y - i, currentPiece));
					neDone = true;
				} else {
					neDone = true;
				}
			}
		}

		return moves;
	}

	@Override
	public Piece movePiece(Move move) {
		return new Bishop(move.getPieceDestinationX(), move.getPieceDestinationY(), team, true);
	}
}