package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Board;
import chess.move.Move;
import chess.move.Move.AttackMove;
import chess.move.Move.NormalMove;
import gfx.Assets;
import main.Utils;

public class Knight extends Piece {
	public Knight(int x, int y, Team team) {
		this(x, y, team, false);
	}

	private Knight(int x, int y, Team team, boolean movedAtLeastOnce) {
		super(x, y, 31, team, PieceType.KNIGHT);
		this.movedAtLeastOnce = movedAtLeastOnce;
		this.texture = team == Team.WHITE ? Assets.white_knight : Assets.black_knight;
	}

	@Override
	public List<Move> getMoves(Board board) {
		List<Move> moves = new ArrayList<Move>();

		Piece currentPiece;

		for (int y = -1; y <= 1; y += 2) {
			if (!Utils.inRange(this.y + y, 0, 7))
				continue;

			for (int x = -2; x <= 2; x += 4) {
				if (!Utils.inRange(this.x + x, 0, 7))
					continue;

				currentPiece = board.getPiece(this.x + x, this.y + y);

				if (currentPiece == null)
					moves.add(new NormalMove(board, this, this.x + x, this.y + y));
				else if (currentPiece.getTeam() != team)
					moves.add(new AttackMove(board, this, this.x + x, this.y + y, currentPiece));
			}
		}

		for (int y = -2; y <= 2; y += 4) {
			if (!Utils.inRange(this.y + y, 0, 7))
				continue;

			for (int x = -1; x <= 1; x += 2) {
				if (!Utils.inRange(this.x + x, 0, 7))
					continue;

				currentPiece = board.getPiece(this.x + x, this.y + y);

				if (currentPiece == null || currentPiece.getTeam() != team)
					moves.add(new NormalMove(board, this, this.x + x, this.y + y));
				else if (currentPiece.getTeam() != team)
					moves.add(new AttackMove(board, this, this.x + x, this.y + y, currentPiece));
			}
		}

		return moves;
	}

	@Override
	public Piece movePiece(Move move) {
		return new Knight(move.getPieceDestinationX(), move.getPieceDestinationY(), team, true);
	}
}