package chess.pieces;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.move.Move;
import chess.move.Move.AttackMove;
import chess.move.Move.NormalMove;
import gfx.Assets;
import main.Utils;

public class King extends Piece {
	public King(int x, int y, Team team) {
		super(x, y, 900, team, PieceType.KING);

		if (team == Team.white)
			this.texture = Assets.white_king;
		else
			this.texture = Assets.black_king;
	}

	private King(int x, int y, Team team, boolean movedAtLeastOnce) {
		this(x, y, team);
		this.movedAtLeastOnce = movedAtLeastOnce;
	}

	@Override
	public Set<Move> getMoves(Board board) {
		Set<Move> moves = new HashSet<Move>();

		Piece currentPiece;

		for (int y = -1; y <= 1; y++) {
			if (!Utils.inRange(this.y + y, 0, 7))
				continue;

			for (int x = -1; x <= 1; x++) {
				if (!Utils.inRange(this.x + x, 0, 7))
					continue;

				currentPiece = board.getPiece(this.x + x, this.y + y);

				if (currentPiece == null)
					moves.add(new NormalMove(board, this, this.x + x, this.y + y));
				else if (currentPiece.getTeam() != team)
					moves.add(new AttackMove(board, this, this.x + x, this.y + y, currentPiece));

			}
		}

		return moves;
	}

	@Override
	public String getName() {
		return "King";
	}

	@Override
	public Piece movePiece(Move move) {
		return new King(move.getPieceDestinationX(), move.getPieceDestinationY(), team, true);
	}
}