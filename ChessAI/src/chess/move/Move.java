package chess.move;

import chess.Board;
import chess.Board.Builder;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Rook;
import main.Utils;

public abstract class Move {
	protected final Board board;
	protected final Piece movedPiece;
	protected final int pieceDestinationX, pieceDestinationY;

	Move(final Board board, final Piece movedPiece, final int pieceDestinationX, final int pieceDestinationY) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.pieceDestinationX = pieceDestinationX;
		this.pieceDestinationY = pieceDestinationY;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof Move))
			return false;

		Move otherMove = (Move) other;
		return this.movedPiece.equals(otherMove.getMovedPiece())
				&& this.pieceDestinationX == otherMove.getPieceDestinationX()
				&& this.pieceDestinationY == otherMove.getPieceDestinationY();
	}

	@Override
	public int hashCode() {
		int result = movedPiece.hashCode();
		result = 10 * result + pieceDestinationX;
		result = 10 * result + pieceDestinationY;
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

		getMovedPiece();

		return b.build();
	}

	public String getNotation() {
		String notation = "";
		notation += movedPiece.getType().getLetter();
		notation += Utils.columns[pieceDestinationX];
		notation += pieceDestinationY;
		return notation;
	}

	public Piece getMovedPiece() {
		return movedPiece;
	}

	public int getPieceDestinationX() {
		return pieceDestinationX;
	}

	public int getPieceDestinationY() {
		return pieceDestinationY;
	}

	public boolean isCastlingMove() {
		return false;
	}

	public static class NormalMove extends Move {
		public NormalMove(final Board board, final Piece movedPiece, final int pieceDestinationX,
				final int pieceDestinationY) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY);
		}
	}

	public static class AttackMove extends Move {
		protected final Piece attackedPiece;

		public AttackMove(final Board board, final Piece movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Piece attackedPiece) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY);
			this.attackedPiece = attackedPiece;
		}

		public Piece getAttackPiece() {
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
			notation += Utils.columns[pieceDestinationX];
			notation += pieceDestinationY;
			return notation;
		}
	}

	public static class PawnMove extends NormalMove {
		public PawnMove(final Board board, final Pawn movedPiece, final int pieceDestinationX,
				final int pieceDestinationY) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY);
		}

		@Override
		public String getNotation() {
			String notation = "";
			notation += Utils.columns[pieceDestinationX];
			notation += pieceDestinationY;
			return notation;
		}
	}

	public static class PawnAttackMove extends AttackMove {
		public PawnAttackMove(final Board board, final Pawn movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Piece attackedPiece) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY, attackedPiece);
		}

		@Override
		public String getNotation() {
			String notation = "";
			notation += Utils.columns[movedPiece.getX()] + "x";
			notation += Utils.columns[pieceDestinationX];
			notation += pieceDestinationY;
			return notation;
		}
	}

	public static class PawnEnPassantAttackMove extends PawnAttackMove {
		public PawnEnPassantAttackMove(final Board board, final Pawn movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Piece attackedPiece) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY, attackedPiece);
		}

		@Override
		public String getNotation() {
			String notation = super.getNotation();
			notation += " e.p.";
			return notation;
		}
	}

	public static class PawnJump extends PawnMove {
		public PawnJump(final Board board, final Pawn movedPiece, final int pieceDestinationX,
				final int pieceDestinationY) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY);
		}
	}

	public static class CastleMove extends Move {
		protected final Rook castleRook;
		protected final int castleRookDestinationX, castleRookDestinationY;

		public CastleMove(final Board board, final King movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Rook castleRook, final int castleRookDestinationX,
				final int castleRookDestinationY) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY);
			this.castleRook = castleRook;
			this.castleRookDestinationX = castleRookDestinationX;
			this.castleRookDestinationY = castleRookDestinationY;
		}

		public Rook getCastleRook() {
			return castleRook;
		}

		public int getCastleRookDestinationX() {
			return castleRookDestinationX;
		}

		public int getCastleRookDestinationY() {
			return castleRookDestinationY;
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
			b.setPiece(new Rook(castleRookDestinationX, castleRookDestinationY, castleRook.getTeam(), true));
			b.setMoveMaker(board.getCurrentPlayer().getOpponent().getTeam());
			return b.build();
		}

		@Override
		public boolean isCastlingMove() {
			return true;
		}
	}

	public static class KingSideCastleMove extends CastleMove {
		public KingSideCastleMove(final Board board, final King movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Rook castleRook, final int castleRookDestinationX,
				final int castleRookDestinationY) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY, castleRook, castleRookDestinationX,
					castleRookDestinationY);
		}
	}

	public static class QueenSideCastleMove extends CastleMove {
		public QueenSideCastleMove(final Board board, final King movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Rook castleRook, final int castleRookDestinationX,
				final int castleRookDestinationY) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY, castleRook, castleRookDestinationX,
					castleRookDestinationY);
		}
	}
}