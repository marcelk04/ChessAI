package com.chess.move;

import java.util.List;
import java.util.stream.Collectors;

import com.chess.Board;
import com.chess.Board.Builder;
import com.chess.pieces.King;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Rook;
import com.chess.player.Player;
import com.main.Utils;

public abstract class Move {
	public static final Move NULL_MOVE = new NullMove();

	protected final Board board;
	protected final Piece movedPiece;
	protected final int pieceDestination;

	public Move(final Board board, final Piece movedPiece, final int pieceDestination) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.pieceDestination = pieceDestination;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof Move))
			return false;

		Move otherMove = (Move) other;
		return this.movedPiece.equals(otherMove.getMovedPiece())
				&& this.pieceDestination == otherMove.getPieceDestination();
	}

	@Override
	public int hashCode() {
		int result = board.hashCode();
		result = 10 * result + movedPiece.hashCode();
		result = 10 * result + pieceDestination;
		return result;
	}

	public abstract Board execute();

	public abstract String getNotation();

	public abstract Piece getAttackedPiece();

	public String standardNotation() {
		String notation = "";
		notation += Utils.columns[Utils.getX(pieceDestination)];
		notation += 8 - Utils.getY(pieceDestination);

		Player newPlayer = execute().getCurrentPlayer();
		if (newPlayer.isKingInCheck())
			notation += "+";

		if (newPlayer.isInCheckMate())
			notation += "+";

		return notation;
	}

	public Board getBoard() {
		return board;
	}

	public Piece getMovedPiece() {
		return movedPiece;
	}

	public int getCurrentPiecePosition() {
		return movedPiece.getPosition();
	}

	public int getPieceDestination() {
		return pieceDestination;
	}

	public boolean isCastlingMove() {
		return false;
	}

	public boolean isAttackMove() {
		return false;
	}

	public static class NullMove extends Move {
		public NullMove() {
			super(null, null, -1);
		}

		@Override
		public String getNotation() {
			return "NULL_MOVE";
		}

		@Override
		public Board execute() {
			return null;
		}

		@Override
		public Piece getAttackedPiece() {
			return null;
		}
	}

	public static class NormalMove extends Move {
		public NormalMove(final Board board, final Piece movedPiece, final int pieceDestination) {
			super(board, movedPiece, pieceDestination);
		}

		@Override
		public Board execute() {
			Builder b = new Builder();

			for (Piece p : board.getCurrentPlayer().getActivePieces()) {
				if (!movedPiece.equals(p)) {
					b.setPiece(p);
				}
			}

			for (Piece p : board.getCurrentPlayer().getOpponent().getActivePieces()) {
				b.setPiece(p);
			}

			b.setPiece(movedPiece.movePiece(this));
			b.setMoveMaker(board.getCurrentPlayer().getOpponent().getTeam());
			b.setEnPassantPawn(null);
			
			return b.build();
		}

		@Override
		public String getNotation() {
			String notation = "";
			notation += movedPiece.getType().getLetter();

			List<Move> samePieceAttacks = Player
					.calculateAttacksOnTile(pieceDestination, board.getCurrentPlayer().getLegalMoves()).stream()
					.filter(e -> e.getMovedPiece().getType() == movedPiece.getType() && !e.equals(this))
					.collect(Collectors.toList());
			if (samePieceAttacks.size() > 0) {
				int sameXAttacks = (int) samePieceAttacks.stream()
						.filter(e -> Utils.getX(e.getCurrentPiecePosition()) == Utils.getX(getCurrentPiecePosition()))
						.count();
				int sameYAttacks = (int) samePieceAttacks.stream()
						.filter(e -> Utils.getY(e.getCurrentPiecePosition()) == Utils.getY(getCurrentPiecePosition()))
						.count();

				if (sameXAttacks == 0) {
					notation += Utils.columns[Utils.getX(getCurrentPiecePosition())];
				}
				if (sameYAttacks == 0) {
					notation += Utils.getY(getCurrentPiecePosition());
				}
			}

			return notation + standardNotation();
		}

		@Override
		public Piece getAttackedPiece() {
			return null;
		}
	}

	public static class AttackMove extends Move {
		protected final Piece attackedPiece;

		public AttackMove(final Board board, final Piece movedPiece, final int pieceDestination,
				final Piece attackedPiece) {
			super(board, movedPiece, pieceDestination);
			this.attackedPiece = attackedPiece;
		}

		@Override
		public Board execute() {
			Builder b = new Builder();

			for (Piece p : board.getCurrentPlayer().getActivePieces()) {
				if (!movedPiece.equals(p)) {
					b.setPiece(p);
				}
			}

			for (Piece p : board.getCurrentPlayer().getOpponent().getActivePieces()) {
				if (!attackedPiece.equals(p)) {
					b.setPiece(p);
				}
			}

			b.setPiece(movedPiece.movePiece(this));
			b.setMoveMaker(board.getCurrentPlayer().getOpponent().getTeam());
			b.setEnPassantPawn(null);

			return b.build();
		}

		@Override
		public String getNotation() {
			String notation = "";
			notation += movedPiece.getType().getLetter();

			List<Move> samePieceAttacks = Player
					.calculateAttacksOnTile(pieceDestination, board.getCurrentPlayer().getLegalMoves()).stream()
					.filter(e -> e.getMovedPiece().getType() == movedPiece.getType() && !e.equals(this))
					.collect(Collectors.toList());
			if (samePieceAttacks.size() > 0) {
				int sameXAttacks = (int) samePieceAttacks.stream()
						.filter(e -> Utils.getX(e.getCurrentPiecePosition()) == Utils.getX(getCurrentPiecePosition()))
						.count();
				int sameYAttacks = (int) samePieceAttacks.stream()
						.filter(e -> Utils.getY(e.getCurrentPiecePosition()) == Utils.getY(getCurrentPiecePosition()))
						.count();

				if (sameXAttacks == 0) {
					notation += Utils.columns[Utils.getX(getCurrentPiecePosition())];
				}
				if (sameYAttacks == 0) {
					notation += Utils.getY(getCurrentPiecePosition());
				}
			}

			notation += "x";
			return notation + standardNotation();
		}

		@Override
		public Piece getAttackedPiece() {
			return attackedPiece;
		}

		@Override
		public boolean isAttackMove() {
			return true;
		}
	}

	public static class PawnMove extends NormalMove {
		public PawnMove(final Board board, final Pawn movedPiece, final int pieceDestination) {
			super(board, movedPiece, pieceDestination);
		}

		@Override
		public String getNotation() {
			return standardNotation();
		}
	}

	public static class PawnAttackMove extends AttackMove {
		public PawnAttackMove(final Board board, final Pawn movedPiece, final int pieceDestination,
				final Piece attackedPiece) {
			super(board, movedPiece, pieceDestination, attackedPiece);
		}

		@Override
		public String getNotation() {
			String notation = "";
			notation += Utils.columns[Utils.getX(movedPiece.getPosition())] + "x";
			return notation + standardNotation();
		}
	}

	public static class PawnEnPassantAttackMove extends PawnAttackMove {
		public PawnEnPassantAttackMove(final Board board, final Pawn movedPiece, final int pieceDestination,
				final Piece attackedPiece) {
			super(board, movedPiece, pieceDestination, attackedPiece);
		}

		@Override
		public String getNotation() {
			String notation = super.getNotation();
			notation += " e.p.";
			return notation;
		}
	}

	public static class PawnJump extends PawnMove {
		public PawnJump(final Board board, final Pawn movedPiece, final int pieceDestination) {
			super(board, movedPiece, pieceDestination);
		}

		@Override
		public Board execute() {
			Builder b = new Builder();

			for (Piece p : board.getAllPieces()) {
				if (!p.equals(movedPiece))
					b.setPiece(p);
			}

			Pawn movedPawn = (Pawn) movedPiece.movePiece(this);

			b.setPiece(movedPawn);
			b.setMoveMaker(board.getCurrentPlayer().getOpponent().getTeam());
			b.setEnPassantPawn(movedPawn);

			return b.build();
		}
	}

	public static class PawnPromotion extends PawnMove {
		protected Move executedMove;
		protected Pawn promotedPawn;
		protected Piece promotionPiece;

		public PawnPromotion(final Move executedMove, final Piece promotionPiece) {
			super(executedMove.getBoard(), (Pawn) executedMove.getMovedPiece(), executedMove.getPieceDestination());
			this.executedMove = executedMove;
			this.promotedPawn = (Pawn) executedMove.getMovedPiece();
			this.promotionPiece = promotionPiece;
		}

		@Override
		public Board execute() {
			Board pawnMovedBoard = executedMove.execute();
			Builder b = new Builder();

			for (Piece p : pawnMovedBoard.getAllPieces()) {
				if (!p.equals(promotedPawn)) {
					b.setPiece(p);
				}
			}

			b.setPiece(promotionPiece);
			b.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getTeam());

			return b.build();
		}
		
		@Override
		public String getNotation() {
			return executedMove.getNotation() + promotionPiece.getType().getLetter();
		}

		@Override
		public Piece getAttackedPiece() {
			return executedMove.getAttackedPiece();
		}

		@Override
		public boolean isAttackMove() {
			return executedMove.isAttackMove();
		}

		public Move getExecutedMove() {
			return executedMove;
		}

		public Pawn getPromotedPawn() {
			return promotedPawn;
		}

		public Piece getPromotionPiece() {
			return promotionPiece;
		}
	}

	public static abstract class CastleMove extends NormalMove {
		protected final Rook castleRook;
		protected final int castleRookDestination;

		public CastleMove(final Board board, final King movedPiece, final int pieceDestination, final Rook castleRook,
				final int castleRookDestination) {
			super(board, movedPiece, pieceDestination);
			this.castleRook = castleRook;
			this.castleRookDestination = castleRookDestination;
		}

		public Rook getCastleRook() {
			return castleRook;
		}

		public int getCastleRookDestinationX() {
			return castleRookDestination;
		}

		@Override
		public Board execute() {
			Builder b = new Builder();

			for (Piece p : board.getAllPieces()) {
				if (!p.equals(movedPiece) && !p.equals(castleRook)) {
					b.setPiece(p);
				}
			}

			b.setPiece(movedPiece.movePiece(this));
			b.setPiece(new Rook(castleRookDestination, castleRook.getTeam(), true));
			b.setMoveMaker(board.getCurrentPlayer().getOpponent().getTeam());
			b.setEnPassantPawn(null);

			return b.build();
		}

		@Override
		public boolean isCastlingMove() {
			return true;
		}
	}

	public static class KingSideCastleMove extends CastleMove {
		public KingSideCastleMove(final Board board, final King movedPiece, final int pieceDestination,
				final Rook castleRook, final int castleRookDestination) {
			super(board, movedPiece, pieceDestination, castleRook, castleRookDestination);
		}

		@Override
		public String getNotation() {
			return "O-O";
		}
	}

	public static class QueenSideCastleMove extends CastleMove {
		public QueenSideCastleMove(final Board board, final King movedPiece, final int pieceDestination,
				final Rook castleRook, final int castleRookDestination) {
			super(board, movedPiece, pieceDestination, castleRook, castleRookDestination);
		}

		@Override
		public String getNotation() {
			return "O-O-O";
		}
	}
}