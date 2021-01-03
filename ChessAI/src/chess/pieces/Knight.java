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
	private static final int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

	public Knight(int position, Team team) {
		this(position, team, false);
	}

	public Knight(int position, Team team, boolean movedAtLeastOnce) {
		super(position, team, PieceType.KNIGHT);
		this.movedAtLeastOnce = movedAtLeastOnce;
		this.texture = team == Team.WHITE ? Assets.white_knight : Assets.black_knight;
	}

	@Override
	public List<Move> getMoves(Board board) {
		List<Move> moves = new ArrayList<Move>();

		for (int currentOffset : CANDIDATE_MOVE_COORDINATES) {
			int x = Utils.getX(position);
			if ((x == 0 && (currentOffset == -17 || currentOffset == -10 || currentOffset == 6 || currentOffset == 15))
					|| (x == 1 && (currentOffset == -10 || currentOffset == 6))
					|| (x == 6 && (currentOffset == -6 || currentOffset == 10)) || (x == 7 && (currentOffset == -15
							|| currentOffset == -6 || currentOffset == 10 || currentOffset == 17))) {
				// if knight is in 1st, 2nd, 7th or 8th column & would move further out
				continue;
			}

			int currentDestination = position + currentOffset;

			if (Utils.inRange(currentDestination, 0, 63)) {
				Piece pieceAtDestination = board.getPiece(currentDestination);

				if (pieceAtDestination == null) {
					moves.add(new NormalMove(board, this, currentDestination));
				} else {
					if (team != pieceAtDestination.getTeam())
						moves.add(new AttackMove(board, this, currentDestination, pieceAtDestination));
				}
			}
		}

		return moves;
	}

	@Override
	public Piece movePiece(Move move) {
		return Utils.getMovedKnight(team, move.getPieceDestination());
	}
}