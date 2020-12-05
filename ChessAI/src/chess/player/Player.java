package chess.player;

import java.util.ArrayList;
import java.util.List;

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
	protected final List<Move> legalMoves;
	protected final boolean kingInCheck;

	Player(final Board board, final List<Move> playerLegals, final List<Move> opponentLegals) {
		this.board = board;
		this.playerKing = findKing();
		playerLegals.addAll(calculateKingCastles(opponentLegals));
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

	public static List<Move> calculateAttacksOnTile(final int x, final int y, final List<Move> moves) {
		final List<Move> attacks = new ArrayList<Move>();
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

	public abstract List<Piece> getActivePieces();

	public abstract Team getTeam();

	public abstract Player getOpponent();

	protected abstract List<Move> calculateKingCastles(List<Move> opponentLegals);

	protected boolean canCastle() {
		return !kingInCheck && !playerKing.isCastled()
				&& (playerKing.canKingSideCastle() || playerKing.canQueenSideCastle());
	}

	public List<Move> getLegalMoves() {
		return legalMoves;
	}
}