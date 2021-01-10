package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import chess.move.Move;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Queen;
import chess.pieces.Rook;
import chess.pieces.Team;
import chess.player.BlackPlayer;
import chess.player.Player;
import chess.player.WhitePlayer;

public class Board {
	private final Map<Integer, Piece> boardConfig;
	private final Pawn enPassantPawn;
	private final List<Piece> whitePieces, blackPieces;
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;

	private Board(Builder builder) {
		this.boardConfig = builder.boardConfig;
		this.enPassantPawn = builder.enPassantPawn;
		this.whitePieces = getActivePieces(builder, Team.WHITE);
		this.blackPieces = getActivePieces(builder, Team.BLACK);
		List<Move> whiteMoves = getLegalMoves(whitePieces);
		List<Move> blackMoves = getLegalMoves(blackPieces);
		this.whitePlayer = new WhitePlayer(this, whiteMoves, blackMoves);
		this.blackPlayer = new BlackPlayer(this, blackMoves, whiteMoves);
		this.currentPlayer = builder.nextMoveMaker == Team.WHITE ? whitePlayer : blackPlayer;
	}

	@Override
	public String toString() {
		String result = "";
		result += "White:" + whitePieces.size();
		result += ";Black:" + blackPieces.size();
		result += "; Current Player:" + currentPlayer.getTeam();
		return result;
	}

	public static Board create() {
		Builder b = new Builder();

		b.setPiece(new Rook(0, Team.BLACK));
		b.setPiece(new Knight(1, Team.BLACK));
		b.setPiece(new Bishop(2, Team.BLACK));
		b.setPiece(new Queen(3, Team.BLACK));
		b.setPiece(new King(4, Team.BLACK, true));
		b.setPiece(new Bishop(5, Team.BLACK));
		b.setPiece(new Knight(6, Team.BLACK));
		b.setPiece(new Rook(7, Team.BLACK));
		b.setPiece(new Pawn(8, Team.BLACK));
		b.setPiece(new Pawn(9, Team.BLACK));
		b.setPiece(new Pawn(10, Team.BLACK));
		b.setPiece(new Pawn(11, Team.BLACK));
		b.setPiece(new Pawn(12, Team.BLACK));
		b.setPiece(new Pawn(13, Team.BLACK));
		b.setPiece(new Pawn(14, Team.BLACK));
		b.setPiece(new Pawn(15, Team.BLACK));

		b.setPiece(new Pawn(48, Team.WHITE));
		b.setPiece(new Pawn(49, Team.WHITE));
		b.setPiece(new Pawn(50, Team.WHITE));
		b.setPiece(new Pawn(51, Team.WHITE));
		b.setPiece(new Pawn(52, Team.WHITE));
		b.setPiece(new Pawn(53, Team.WHITE));
		b.setPiece(new Pawn(54, Team.WHITE));
		b.setPiece(new Pawn(55, Team.WHITE));
		b.setPiece(new Rook(56, Team.WHITE));
		b.setPiece(new Knight(57, Team.WHITE));
		b.setPiece(new Bishop(58, Team.WHITE));
		b.setPiece(new Queen(59, Team.WHITE));
		b.setPiece(new King(60, Team.WHITE, true));
		b.setPiece(new Bishop(61, Team.WHITE));
		b.setPiece(new Knight(62, Team.WHITE));
		b.setPiece(new Rook(63, Team.WHITE));

		b.setMoveMaker(Team.WHITE);

		return b.build();
	}

	public Move findMove(int currentPiecePosition, int pieceDestination) {
		for (Move m : getAllLegalMoves()) {
			if (m.getCurrentPiecePosition() == currentPiecePosition && m.getPieceDestination() == pieceDestination) {
				return m;
			}
		}
		return Move.NULL_MOVE;
	}

	public boolean hasGameEnded() {
		return currentPlayer.isInCheckMate() || currentPlayer.isInStaleMate();
	}

	// ===== Getters ===== \\
	private static List<Piece> getActivePieces(Builder builder, Team team) {
		final List<Piece> activePieces = new ArrayList<Piece>();

		for (int i = 0; i < 64; i++) {
			Piece p = builder.boardConfig.get(i);
			if (p != null && p.getTeam() == team) {
				activePieces.add(p);
			}
		}

		return activePieces;
	}

	private List<Move> getLegalMoves(List<Piece> pieces) {
		return pieces.stream().flatMap(p -> p.getMoves(this).stream()).collect(Collectors.toList());
	}

	public Piece getPiece(int position) {
		return boardConfig.get(position);
	}

	public Pawn getEnPassantPawn() {
		return enPassantPawn;
	}

	public List<Piece> getWhitePieces() {
		return this.whitePieces;
	}

	public List<Piece> getBlackPieces() {
		return this.blackPieces;
	}

	public WhitePlayer getWhitePlayer() {
		return this.whitePlayer;
	}

	public BlackPlayer getBlackPlayer() {
		return this.blackPlayer;
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public List<Piece> getAllPieces() {
		return Stream.concat(whitePieces.stream(), blackPieces.stream()).collect(Collectors.toList());
	}

	public List<Move> getPossibleMoves(Piece piece) {
		return (piece.getTeam() == Team.WHITE ? whitePlayer.getLegalMoves() : blackPlayer.getLegalMoves()).stream()
				.filter(m -> piece.equals(m.getMovedPiece())).collect(Collectors.toList());
	}

	public List<Move> getAllLegalMoves() {
		return Stream.concat(whitePlayer.getLegalMoves().stream(), blackPlayer.getLegalMoves().stream())
				.collect(Collectors.toList());
	}

	// ===== Inner classes ===== \\
	public static class Builder {
		Map<Integer, Piece> boardConfig;
		Team nextMoveMaker;
		Pawn enPassantPawn;

		public Builder() {
			this.boardConfig = new HashMap<Integer, Piece>();
		}

		public Builder setPiece(Piece piece) {
			boardConfig.put(piece.getPosition(), piece);
			return this;
		}

		public Builder setMoveMaker(Team nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}

		public Builder setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
			return this;
		}

		public Board build() {
			return new Board(this);
		}
	}
}