package chess.move;

import chess.Board;
import chess.Board.Builder;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Rook;

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

		return b.build();
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

	public static class NormalMove extends Move {
		public NormalMove(final Board board, final Piece movedPiece, final int pieceDestinationX,
				final int pieceDestinationY) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY);
		}
	}

	public static class AttackMove extends Move {
		protected final Piece attackedPiece;

		public AttackMove(final Board board, final Piece movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Piece attackPiece) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY);
			this.attackedPiece = attackPiece;
		}

		public Piece getAttackPiece() {
			return attackedPiece;
		}
	}

	public static class PawnMove extends NormalMove {
		public PawnMove(final Board board, final Pawn movedPiece, final int pieceDestinationX,
				final int pieceDestinationY) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY);
		}
	}

	public static class PawnAttackMove extends AttackMove {
		public PawnAttackMove(final Board board, final Pawn movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Piece attackPiece) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY, attackPiece);
		}
	}

	public static class PawnEnPassantAttackMove extends PawnAttackMove {
		public PawnEnPassantAttackMove(final Board board, final Pawn movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Piece attackedPiece) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY, attackedPiece);
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

		public CastleMove(final Board board, final King movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Rook castleRook) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY);
			this.castleRook = castleRook;
		}

		public Rook getCastleRook() {
			return castleRook;
		}
	}

	public static class KingSideCastleMove extends CastleMove {
		public KingSideCastleMove(final Board board, final King movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Rook castleRook) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY, castleRook);
		}
	}

	public static class QueenSideCastleMove extends CastleMove {
		public QueenSideCastleMove(final Board board, final King movedPiece, final int pieceDestinationX,
				final int pieceDestinationY, final Rook castleRook) {
			super(board, movedPiece, pieceDestinationX, pieceDestinationY, castleRook);
		}
	}
}