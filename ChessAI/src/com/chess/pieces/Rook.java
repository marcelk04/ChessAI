package com.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.chess.move.Move.AttackMove;
import com.chess.move.Move.NormalMove;
import com.gfx.Assets;
import com.main.Utils;

public class Rook extends Piece {
	private static final int[] CANDIDATE_MOVE_COORDINATES = { -8, -1, 8, 1 };

	public Rook(int position, Team team) {
		this(position, team, false);
	}

	public Rook(int position, Team team, boolean movedAtLeastOnce) {
		super(position, team, PieceType.ROOK);
		this.movedAtLeastOnce = movedAtLeastOnce;
		this.texture = team == Team.WHITE ? Assets.white_rook : Assets.black_rook;
	}

	@Override
	public List<Move> getMoves(Board board) {
		List<Move> moves = new ArrayList<Move>();

		for (int currentOffset : CANDIDATE_MOVE_COORDINATES) {
			int currentDestination = position;

			while (Utils.inRange(currentDestination, 0, 63)) {
				if ((Utils.getX(currentDestination) == 0 && currentOffset == -1)
						|| (Utils.getX(currentDestination) == 7 && currentOffset == 1)) {
					break;
				}

				currentDestination += currentOffset;

				if (Utils.inRange(currentDestination, 0, 63)) {
					Piece pieceAtDestination = board.getPiece(currentDestination);

					if (pieceAtDestination == null) {
						moves.add(new NormalMove(board, this, currentDestination));
					} else {
						if (team != pieceAtDestination.getTeam())
							moves.add(new AttackMove(board, this, currentDestination, pieceAtDestination));
						break;
					}
				}
			}
		}

		return moves;
	}

	@Override
	public Piece movePiece(Move move) {
		return Utils.getMovedRook(team, move.getPieceDestination());
	}

	@Override
	public int positionBonus() {
		return team.rookBonus(position);
	}
}