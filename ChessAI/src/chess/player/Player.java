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

	public Player(final Board board, final List<Move> playerLegals, final List<Move> opponentLegals) {
		this.board = board;
		this.playerKing = findKing();
		playerLegals.addAll(calculateCastleMoves(opponentLegals));
		this.legalMoves = playerLegals;
		this.kingInCheck = !calculateAttacksOnTile(playerKing.getX(), playerKing.getY(), opponentLegals).isEmpty();
	}

	public static List<Move> calculateAttacksOnTile(int x, int y, List<Move> moves) {
		List<Move> attacks = new ArrayList<Move>();
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

		if (newBoard.getCurrentPlayer().getOpponent().isKingInCheck()) {
			return new MoveTransition(board, board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}

		return new MoveTransition(board, newBoard, move, MoveStatus.DONE);

	}

	private King findKing() {
		for (Piece p : getActivePieces()) {
			if (p.getType() == PieceType.KING) {
				return (King) p;
			}
		}
		throw new RuntimeException("Board must contain a King!");
	}

	private boolean hasExecutableMoves() {
		for (Move m : legalMoves) {
			if (makeMove(m).getMoveStatus() == MoveStatus.DONE) {
				return true;
			}
		}
		return false;
	}

	public abstract List<Piece> getActivePieces();

	public abstract Team getTeam();

	public abstract Player getOpponent();

	protected abstract List<Move> calculateCastleMoves(List<Move> opponentLegals);

	// ===== Getters ===== \\
	public boolean isKingInCheck() {
		return kingInCheck;
	}

	public boolean isInCheckMate() {
		return kingInCheck && !hasExecutableMoves();
	}

	public boolean isInStaleMate() {
		return !kingInCheck && !hasExecutableMoves();
	}

	public List<Move> getLegalMoves() {
		return legalMoves;
	}

	public boolean canCastle() {
		return !kingInCheck && !playerKing.isCastled()
				&& (playerKing.canKingSideCastle() || playerKing.canQueenSideCastle());
	}
}