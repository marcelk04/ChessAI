package com.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.chess.move.Move.AttackMove;
import com.chess.move.Move.NormalMove;
import com.gfx.Assets;
import com.main.Utils;

public class Knight extends Piece {
	private static final int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

	public Knight(int position, Team team) {
		this(position, team, false);
	}

	public Knight(int position, Team team, boolean movedAtLeastOnce) {
		super(position, team, PieceType.KNIGHT, movedAtLeastOnce);
		this.texture = team == Team.WHITE ? Assets.white_knight : Assets.black_knight;
	}

	@Override
	public List<Move> getMoves(Board board) {
		final List<Move> moves = new ArrayList<Move>();

		for (int currentOffset : CANDIDATE_MOVE_COORDINATES) {
			if ((x == 0 && (currentOffset == -17 || currentOffset == -10 || currentOffset == 6 || currentOffset == 15))
					|| (x == 1 && (currentOffset == -10 || currentOffset == 6))
					|| (x == 6 && (currentOffset == -6 || currentOffset == 10)) || (x == 7 && (currentOffset == -15
							|| currentOffset == -6 || currentOffset == 10 || currentOffset == 17))) {
				continue; // if knight is in 1st, 2nd, 7th or 8th column & would move further out
			}

			final int currentDestination = position + currentOffset;

			if (!Utils.inRange(currentDestination, 0, 63)) // if knight would move off the board
				continue;

			final Piece pieceAtDestination = board.getPiece(currentDestination);

			if (pieceAtDestination != null && team != pieceAtDestination.getTeam()) {
				moves.add(new AttackMove(board, this, currentDestination, pieceAtDestination));
				continue;
			}

			moves.add(new NormalMove(board, this, currentDestination));
		}

		return moves;
	}

	@Override
	public Piece movePiece(Move move) {
		return Utils.getMovedKnight(team, move.getPieceDestination());
	}

	@Override
	public int positionBonus() {
		return team.knightBonus(position);
	}
}