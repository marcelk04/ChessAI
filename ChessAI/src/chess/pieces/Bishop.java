package chess.pieces;

import java.util.HashSet;
import java.util.Set;

import algorithm.Move;
import chess.board.ChessBoard;
import gfx.Assets;

public class Bishop extends Piece {
	public Bishop(int x, int y, Team team, ChessBoard board) {
		super(x, y, 30, team, board);

		if (team == Team.white)
			this.texture = Assets.white_bishop;
		else
			this.texture = Assets.black_bishop;
	}

	private Bishop(int x, int y, Team team, boolean movedAtLeastOnce, ChessBoard board) {
		super(x, y, 30, team, board);
		this.movedAtLeastOnce = movedAtLeastOnce;
	}

	@Override
	public Piece clone(ChessBoard board) {
		return new Bishop(x, y, team, movedAtLeastOnce, board);
	}

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<Move>();

		Move currentMove;
		Piece currentPiece;

		boolean nwDone = false, neDone = false, seDone = false, swDone = false;

		for (int i = 1; i < 8; i++) {
			if (!nwDone && this.x - i >= 0 && this.y - i >= 0) { // northwest / left up
				currentMove = new Move(this, this.x - i, this.y - i);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null) {
					moves.add(currentMove);
				} else if (currentPiece.getTeam() != team) {
					moves.add(currentMove);
					nwDone = true;
				} else {
					nwDone = true;
				}
			}

			if (!seDone && this.x + i < 8 && this.y + i < 8) { // southeast / right down
				currentMove = new Move(this, this.x + i, this.y + i);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null) {
					moves.add(currentMove);
				} else if (currentPiece.getTeam() != team) {
					moves.add(currentMove);
					seDone = true;
				} else {
					seDone = true;
				}
			}

			if (!swDone && this.x - i >= 0 && this.y + i < 8) { // southwest / left down
				currentMove = new Move(this, this.x - i, this.y + i);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null) {
					moves.add(currentMove);
				} else if (currentPiece.getTeam() != team) {
					moves.add(currentMove);
					swDone = true;
				} else {
					swDone = true;
				}
			}

			if (!neDone && this.x + i < 8 && this.y - i >= 0) { // northeast / right up
				currentMove = new Move(this, this.x + i, this.y - i);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null) {
					moves.add(currentMove);
				} else if (currentPiece.getTeam() != team) {
					moves.add(currentMove);
					neDone = true;
				} else {
					neDone = true;
				}
			}
		}

		return moves;
	}

	@Override
	public String getName() {
		return "Bishop";
	}
}