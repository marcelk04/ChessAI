package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Board;
import chess.move.Move;
import chess.move.Move.PawnAttackMove;
import chess.move.Move.PawnEnPassantAttackMove;
import chess.move.Move.PawnJump;
import chess.move.Move.PawnMove;
import gfx.Assets;
import main.Utils;

public class Pawn extends Piece {
	private static final int[] CANDIDATE_MOVE_COORDINATES = { 8, 16, 7, 9 };

	private boolean jumped = false;

	public Pawn(int position, Team team) {
		this(position, team, false, false);
	}

	private Pawn(int position, Team team, boolean movedAtLeastOnce, boolean jumped) {
		super(position, team, PieceType.PAWN);
		this.movedAtLeastOnce = movedAtLeastOnce;
		this.jumped = jumped;
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
			} else if ((currentOffset == 7 || currentOffset == 9) && !((Utils.getY(position) == 0 && team == Team.BLACK)
					|| (Utils.getY(position) == 7 && team == Team.WHITE))) {
				Piece pieceAtDestination = board.getPiece(currentDestination);
				if (pieceAtDestination != null && team != pieceAtDestination.getTeam()) {
					moves.add(new PawnAttackMove(board, this, currentDestination, pieceAtDestination));
				}

				int sidePiecePosition = Utils.getIndex(Utils.getX(currentDestination),
						Utils.getY(currentDestination) - team.moveDirection());
				Piece sidePiece = board.getPiece(sidePiecePosition);

				if (pieceAtDestination == null && sidePiece != null && sidePiece.getType() == PieceType.PAWN
						&& ((Utils.getY(position) == 3 && team == Team.WHITE)
								|| (Utils.getY(position) == 4 && team == Team.BLACK))) {
					Pawn sidePawn = (Pawn) sidePiece;

					if (team != sidePiece.getTeam() && sidePawn.wasMovedTwoSpaces()) {
						moves.add(new PawnEnPassantAttackMove(board, this, currentDestination, sidePawn));
					}
				}
			}
		}

		return moves;
	}

	public boolean wasMovedTwoSpaces() {
		return jumped;
	}

	@Override
	public Piece movePiece(Move move) {
		return new Pawn(move.getPieceDestination(), team, true, move instanceof PawnJump ? true : jumped);
	}
}