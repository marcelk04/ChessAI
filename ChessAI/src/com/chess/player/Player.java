package com.chess.player;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.chess.move.MoveStatus;
import com.chess.move.MoveTransition;
import com.chess.pieces.King;
import com.chess.pieces.Piece;
import com.chess.pieces.Piece.PieceType;
import com.chess.pieces.Team;

public abstract class Player {
	protected final Board board;
	protected final King playerKing;
	protected final List<Move> legalMoves;
	protected final boolean kingInCheck;
	protected final boolean canKingSideCastle, canQueenSideCastle;

	public Player(final Board board, final List<Move> playerLegals, final List<Move> opponentLegals,
			final boolean canKingSideCastle, final boolean canQueenSideCastle) {
		this.board = board;
		this.playerKing = findKing();
		this.canKingSideCastle = canKingSideCastle;
		this.canQueenSideCastle = canQueenSideCastle;
		playerLegals.addAll(calculateCastleMoves(opponentLegals));
		this.legalMoves = playerLegals;
		this.kingInCheck = !calculateAttacksOnTile(playerKing.getPosition(), opponentLegals).isEmpty();
	}

	public static List<Move> calculateAttacksOnTile(int position, List<Move> moves) {
		final List<Move> attackMoves = new ArrayList<Move>();
		for (Move m : moves) {
			if (position == m.getPieceDestination())
				attackMoves.add(m);
		}
		return attackMoves;
	}

	public MoveTransition makeMove(Move move) {
		if (!legalMoves.contains(move) || move.isAttackMove() && move.getAttackedPiece().getType() == PieceType.KING) {
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
		return !kingInCheck && !playerKing.isCastled() && playerKing.canCastle();
	}

	public King getKing() {
		return playerKing;
	}

	public boolean canKingSideCastle() {
		return canKingSideCastle;
	}

	public boolean canQueenSideCastle() {
		return canQueenSideCastle;
	}
}