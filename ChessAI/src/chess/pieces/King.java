package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Board;
import chess.move.Move;
import chess.move.Move.AttackMove;
import chess.move.Move.NormalMove;
import gfx.Assets;
import main.Utils;

public class King extends Piece {
	private boolean isCastled;
	private final boolean canKingSideCastle, canQueenSideCastle;

	public King(int x, int y, Team team, boolean canKingSideCastle, boolean canQueenSideCastle) {
		this(x, y, team, false, canKingSideCastle, canQueenSideCastle, false);
	}

	private King(int x, int y, Team team, boolean movedAtLeastOnce, boolean canKingSideCastle,
			boolean canQueenSideCastle, boolean isCastled) {
		super(x, y, 900, team, PieceType.KING);
		this.isCastled = isCastled;
		this.canKingSideCastle = canKingSideCastle;
		this.canQueenSideCastle = canQueenSideCastle;
		this.movedAtLeastOnce = movedAtLeastOnce;
		this.texture = team == Team.WHITE ? Assets.white_king : Assets.black_king;
	}

	@Override
	public List<Move> getMoves(Board board) {
		List<Move> moves = new ArrayList<Move>();

		Piece currentPiece;

		int destinationX, destinationY;

		for (int y = -1; y <= 1; y++) {
			destinationY = this.y + y;
			if (!Utils.inRange(destinationY, 0, 7))
				continue;

			for (int x = -1; x <= 1; x++) {
				destinationX = this.x + x;
				if (!Utils.inRange(destinationX, 0, 7))
					continue;

				currentPiece = board.getPiece(destinationX, destinationY);

				if (currentPiece == null)
					moves.add(new NormalMove(board, this, destinationX, destinationY));
				else if (currentPiece.getTeam() != team)
					moves.add(new AttackMove(board, this, destinationX, destinationY, currentPiece));
			}
		}

		return moves;
	}

	@Override
	public Piece movePiece(Move move) {
		return new King(move.getPieceDestinationX(), move.getPieceDestinationY(), team, true, false, false,
				move.isCastlingMove());
	}

	public boolean isCastled() {
		return isCastled;
	}

	public boolean canKingSideCastle() {
		return canKingSideCastle;
	}

	public boolean canQueenSideCastle() {
		return canQueenSideCastle;
	}
}