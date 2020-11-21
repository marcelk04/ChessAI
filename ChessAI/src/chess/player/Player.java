package chess.player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.move.Move;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import chess.pieces.King;
import chess.pieces.Piece;
import chess.pieces.PieceType;
import chess.pieces.Team;

public abstract class Player {
	protected final Board board;
	protected final King playerKing;
	protected final Set<Move> legalMoves;
	protected final boolean kingInCheck;

	Player(final Board board, final Set<Move> playerLegals, final Set<Move> opponentLegals) {
		this.board = board;
		this.playerKing = findKing();
		this.legalMoves = playerLegals;
		this.kingInCheck = !calculateAttacksOnTile(this.playerKing.getX(), this.playerKing.getY(), opponentLegals)
				.isEmpty();
	}

	private King findKing() {
		for (Piece p : getActivePieces()) {
			if (p.getType() == PieceType.KING) {
				return (King) p;
			}
		}
		throw new RuntimeException("Illegal Board");
	}

	public static Set<Move> calculateAttacksOnTile(final int x, final int y, final Collection<Move> moves) {
		final Set<Move> attacks = new HashSet<Move>();
		for (Move m : moves) {
			if (m.getPieceDestinationX() == x && m.getPieceDestinationY() == y) {
				attacks.add(m);
			}
		}
		return attacks;
	}

	public MoveTransition makeMove(Move move) {
		if (!legalMoves.contains(move)) {
			return new MoveTransition(board, board, move, MoveStatus.ILLEGAL_MOVE);
		}

		Board newBoard = move.execute();

		return new MoveTransition(this.board, newBoard, move, MoveStatus.DONE);

	}

	public abstract Collection<Piece> getActivePieces();

	public abstract Team getTeam();

	public abstract Player getOpponent();
}