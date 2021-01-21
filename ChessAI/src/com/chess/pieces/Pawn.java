package com.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.chess.move.Move.PawnAttackMove;
import com.chess.move.Move.PawnEnPassantAttackMove;
import com.chess.move.Move.PawnJump;
import com.chess.move.Move.PawnMove;
import com.gfx.Assets;
import com.main.Utils;

public class Pawn extends Piece {
	private static final int[] CANDIDATE_MOVE_COORDINATES = { 8, 16, 7, 9 };

	public Pawn(int position, Team team) {
		this(position, team, false);
	}

	public Pawn(int position, Team team, boolean movedAtLeastOnce) {
		super(position, team, PieceType.PAWN);
		this.movedAtLeastOnce = movedAtLeastOnce;
		this.texture = team == Team.WHITE ? Assets.white_pawn : Assets.black_pawn;
	}

	@Override
	public List<Move> getMoves(Board board) {
		List<Move> moves = new ArrayList<Move>();

		for (int currentOffset : CANDIDATE_MOVE_COORDINATES) {
			int currentDestination = position + currentOffset * team.moveDirection();
			if (!Utils.inRange(currentDestination, 0, 63))
				continue;

			if (currentOffset == 8 && board.getPiece(currentDestination) == null) {
				moves.add(new PawnMove(board, this, currentDestination));
			} else if (currentOffset == 16 && !movedAtLeastOnce && ((Utils.getY(position) == 1 && team == Team.BLACK)
					|| (Utils.getY(position) == 6 && team == Team.WHITE))) {
				if (board.getPiece(currentDestination) == null
						&& board.getPiece(position + 8 * team.moveDirection()) == null) {
					moves.add(new PawnJump(board, this, currentDestination));
				}
			} else if (currentOffset == 7 || currentOffset == 9) {
				if (!((currentOffset == 9 && Utils.getX(position) == 0
						|| currentOffset == 7 && Utils.getX(position) == 7) && team == Team.WHITE
						|| (currentOffset == 7 && Utils.getX(position) == 0
								|| currentOffset == 9 && Utils.getX(position) == 7) && team == Team.BLACK)
						&& !(Utils.getY(position) == 0 && team == Team.WHITE
								|| Utils.getY(position) == 7 && team == Team.BLACK)) {
					Piece pieceAtDestination = board.getPiece(currentDestination);
					if (pieceAtDestination != null && team != pieceAtDestination.getTeam()) {
						moves.add(new PawnAttackMove(board, this, currentDestination, pieceAtDestination));
					}

					int sidePiecePosition = Utils.getIndex(Utils.getX(currentDestination),
							Utils.getY(currentDestination) - team.moveDirection());

					Piece sidePiece = board.getPiece(sidePiecePosition);
					Pawn enPassantPawn = board.getEnPassantPawn();

					if (pieceAtDestination == null && sidePiece != null && enPassantPawn != null
							&& sidePiece.equals(enPassantPawn)) {
						moves.add(new PawnEnPassantAttackMove(board, this, currentDestination, enPassantPawn));

					}
				}
			}
		}

		return moves;
	}

	@Override
	public Piece movePiece(Move move) {
		return Utils.getMovePawn(team, move.getPieceDestination());
	}
}