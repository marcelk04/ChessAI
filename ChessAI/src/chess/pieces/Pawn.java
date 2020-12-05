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
	private boolean jumped = false;

	public Pawn(int x, int y, Team team) {
		this(x, y, team, false, false);
	}

	private Pawn(int x, int y, Team team, boolean movedAtLeastOnce, boolean jumped) {
		super(x, y, 10, team, PieceType.PAWN);
		this.movedAtLeastOnce = movedAtLeastOnce;
		this.jumped = jumped;
		this.texture = team == Team.WHITE ? Assets.white_pawn : Assets.black_pawn;
	}

	@Override
	public List<Move> getMoves(Board board) {
		List<Move> moves = new ArrayList<Move>();

		Piece currentPiece = null;

		for (int i = -1; i < 2 && Utils.inRange(this.x + i, 0, 7); i += 2) { // check for pieces to hit
			if (team == Team.WHITE && this.y - 1 >= 0) // white team
				currentPiece = board.getPiece(this.x + i, this.y - 1);
			else if (team == Team.BLACK && this.y + 1 < 8) // black team
				currentPiece = board.getPiece(this.x + i, this.y + 1);

			if (currentPiece != null && currentPiece.getTeam() != team)
				moves.add(new PawnAttackMove(board, this, currentPiece.getX(), currentPiece.getY(), currentPiece));

			Piece attackedPiece = board.getPiece(this.x + i, this.y); // en passant
			if (attackedPiece != null && attackedPiece.getTeam() != team && attackedPiece instanceof Pawn) {
				if (((Pawn) attackedPiece).wasMovedTwoSpaces()) {
					if (board.getPiece(this.x + i, this.y + team.moveDirection()) == null)
						moves.add(new PawnEnPassantAttackMove(board, this, this.x + i, this.y + team.moveDirection(),
								attackedPiece));
				}
			}
		}

		if (team == Team.WHITE) { // check for moves one field away
			if (board.getPiece(this.x, this.y - 1) == null)
				moves.add(new PawnMove(board, this, this.x, this.y - 1));
		} else { // black team
			if (board.getPiece(this.x, this.y + 1) == null)
				moves.add(new PawnMove(board, this, this.x, this.y + 1));
		}

		if (!movedAtLeastOnce) { // check for moves two fields away
			if (board.getPiece(this.x, this.y + team.moveDirection()) == null
					&& board.getPiece(this.x, this.y + 2 * team.moveDirection()) == null) {
				moves.add(new PawnJump(board, this, this.x, this.y + 2 * team.moveDirection()));
			}
		}

		return moves;
	}

	public boolean wasMovedTwoSpaces() {
		return jumped;
	}

	@Override
	public Piece movePiece(Move move) {
		return new Pawn(move.getPieceDestinationX(), move.getPieceDestinationY(), team, true,
				move instanceof PawnJump ? true : jumped);
	}
}