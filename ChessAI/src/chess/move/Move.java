package chess.move;

import chess.Board;
import chess.Board.Builder;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Rook;
import main.Utils;

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
		int result = movedPiece.hashCode();
		result = 10 * result + pieceDestination;
		return result;
	}

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

		return b.build();
	}

	public abstract String getNotation();

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
			super(null, null, 0);
		}

		@Override
		public String getNotation() {
			return "NULL_MOVE";
		}
	}

	public static class NormalMove extends Move {
		public NormalMove(final Board board, final Piece movedPiece, final int pieceDestination) {
			super(board, movedPiece, pieceDestination);
		}

		@Override
		public String getNotation() {
			String notation = "";
			notation += movedPiece.getType().getLetter();
			notation += Utils.columns[Utils.getX(pieceDestination)];
			notation += 8 - Utils.getY(pieceDestination);
			return notation;
		}
	}

	public static class AttackMove extends Move {
		protected final Piece attackedPiece;

		public AttackMove(final Board board, final Piece movedPiece, final int pieceDestination,
				final Piece attackedPiece) {
			super(board, movedPiece, pieceDestination);
			this.attackedPiece = attackedPiece;
		}

		public Piece getAttackedPiece() {
			return attackedPiece;
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

			return b.build();
		}

		@Override
		public String getNotation() {
			String notation = "";
			notation += movedPiece.getType().getLetter() + "x";
			notation += Utils.columns[Utils.getX(pieceDestination)];
			notation += 8 - Utils.getY(pieceDestination);
			return notation;
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
			String notation = "";
			notation += Utils.columns[Utils.getX(pieceDestination)];
			notation += 8 - Utils.getY(pieceDestination);
			return notation;
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
			notation += Utils.columns[Utils.getX(pieceDestination)];
			notation += 8 - Utils.getY(pieceDestination);
			return notation;
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
	}

	public static abstract class CastleMove extends Move {
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