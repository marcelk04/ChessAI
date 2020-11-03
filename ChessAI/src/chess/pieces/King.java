package chess.pieces;

import java.util.HashSet;
import java.util.Set;

import algorithm.Move;
import chess.ChessBoard;
import gfx.Assets;
import main.Utils;

public class King extends Piece {
	public King(int x, int y, Team team, ChessBoard board) {
		super(x, y, 900, team, board);

		if (team == Team.white)
			this.texture = Assets.white_king;
		else
			this.texture = Assets.black_king;
	}

	private King(int x, int y, Team team, boolean movedAtLeastOnce, ChessBoard board) {
		super(x, y, 900, team, board);
		this.movedAtLeastOnce = movedAtLeastOnce;
	}

	@Override
	public Piece clone(ChessBoard board) {
		return new King(x, y, team, movedAtLeastOnce, board);
	}

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<Move>();

		Move currentMove;
		Piece currentPiece;

		for (int y = -1; y <= 1; y++) {
			if (!Utils.inRange(this.y + y, 0, 7))
				continue;

			for (int x = -1; x <= 1; x++) {
				if (!Utils.inRange(this.x + x, 0, 7))
					continue;

				currentMove = new Move(this, this.x + x, this.y + y);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null || currentPiece.getTeam() != team)
					moves.add(currentMove);
			}
		}

		if (!movedAtLeastOnce) {
			if (board.getPiece(this.x + 1, this.y) == null && board.getPiece(this.x + 2, this.y) == null) {
				// check right
				currentPiece = board.getPiece(this.x + 3, this.y);
				if (currentPiece != null && currentPiece.getName().equals("Rook")
						&& !currentPiece.gotMovedAtLeastOnce()) {
					moves.add(new Move(this, this.x + 2, this.y));
				}
			} else if (board.getPiece(this.x - 1, this.y) == null && board.getPiece(this.x - 2, this.y) == null
					&& board.getPiece(this.x - 3, this.y) == null) {
				// check left
				currentPiece = board.getPiece(this.x - 4, this.y);
				if (currentPiece != null && currentPiece.getName().equals("Rook")
						&& !currentPiece.gotMovedAtLeastOnce()) {
					moves.add(new Move(this, this.x - 2, this.y));
				}
			}
		}

		return moves;
	}

	@Override
	public String getName() {
		return "King";
	}

	@Override
	public void setPosition(int newX, int newY) {
		if (this.x - newX == 2) { // castling left
			Piece rook = board.getPiece(0, this.y);
			if (rook != null) {
				board.makeMove(new Move(rook, 3, newY));
			}
		} else if (this.x - newX == -2) { // castling right
			Piece rook = board.getPiece(7, this.y);
			if (rook != null) {
				board.makeMove(new Move(rook, 5, newY));
			}
		}

		super.setPosition(newX, newY);
	}
}