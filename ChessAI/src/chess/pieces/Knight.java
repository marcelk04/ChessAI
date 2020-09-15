package chess.pieces;

import java.util.HashSet;
import java.util.Set;

import algorithm.Move;
import chess.board.ChessBoard;
import gfx.Assets;
import main.Utils;

public class Knight extends Piece {
	public Knight(int x, int y, Team team, ChessBoard board) {
		super(x, y, 30, team, board);

		if (team == Team.white)
			this.texture = Assets.white_knight;
		else
			this.texture = Assets.black_knight;
	}

	private Knight(int x, int y, Team team, boolean movedAtLeastOnce, ChessBoard board) {
		super(x, y, 30, team, board);
		this.movedAtLeastOnce = movedAtLeastOnce;
	}

	@Override
	public Piece clone(ChessBoard board) {
		return new Knight(x, y, team, movedAtLeastOnce, board);
	}

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<Move>();

		Move currentMove;
		Piece currentPiece;

		for (int y = -1; y <= 1; y += 2) {
			if (!Utils.inRange(this.y + y, 0, 7))
				continue;

			for (int x = -2; x <= 2; x += 4) {
				if (!Utils.inRange(this.x + x, 0, 7))
					continue;

				currentMove = new Move(this, this.x + x, this.y + y);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null || currentPiece.getTeam() != team)
					moves.add(currentMove);
			}
		}

		for (int y = -2; y <= 2; y += 4) {
			if (!Utils.inRange(this.y + y, 0, 7))
				continue;

			for (int x = -1; x <= 1; x += 2) {
				if (!Utils.inRange(this.x + x, 0, 7))
					continue;

				currentMove = new Move(this, this.x + x, this.y + y);
				currentPiece = board.getPiece(currentMove);

				if (currentPiece == null || currentPiece.getTeam() != team)
					moves.add(currentMove);
			}
		}

		return moves;
	}

	@Override
	public String getName() {
		return "Knight";
	}
}