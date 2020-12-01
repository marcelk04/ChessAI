package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chess.move.Move;
import chess.move.Position;
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
	private final Map<Position, Piece> boardConfig;
	private final List<Piece> whitePieces, blackPieces;
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;

	private Board(Builder builder) {
		this.boardConfig = builder.boardConfig;
		this.whitePieces = getActivePieces(builder, Team.WHITE);
		this.blackPieces = getActivePieces(builder, Team.BLACK);
		final Set<Move> whiteMoves = getLegalMoves(whitePieces);
		final Set<Move> blackMoves = getLegalMoves(blackPieces);
		this.whitePlayer = new WhitePlayer(this, whiteMoves, blackMoves);
		this.blackPlayer = new BlackPlayer(this, blackMoves, whiteMoves);
		this.currentPlayer = builder.nextMoveMaker == Team.WHITE ? whitePlayer : blackPlayer;
	}

	private static List<Piece> getActivePieces(Builder builder, Team team) {
		final List<Piece> activePieces = new ArrayList<Piece>();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Piece p = builder.boardConfig.get(new Position(x, y));
				if (p != null && p.getTeam() == team) {
					activePieces.add(p);
				}
			}
		}

		return activePieces;
	}

	private Set<Move> getLegalMoves(List<Piece> pieces) {
		final Set<Move> legalMoves = new HashSet<Move>();
		for (Piece p : pieces) {
			legalMoves.addAll(p.getMoves(this));
		}
		return legalMoves;
	}

	public static Board create() {
		Builder b = new Builder();

		b.setPiece(new Rook(0, 0, Team.BLACK));
		b.setPiece(new Knight(1, 0, Team.BLACK));
		b.setPiece(new Bishop(2, 0, Team.BLACK));
		b.setPiece(new Queen(3, 0, Team.BLACK));
		b.setPiece(new King(4, 0, Team.BLACK, true, true));
		b.setPiece(new Bishop(5, 0, Team.BLACK));
		b.setPiece(new Knight(6, 0, Team.BLACK));
		b.setPiece(new Rook(7, 0, Team.BLACK));
		b.setPiece(new Pawn(0, 1, Team.BLACK));
		b.setPiece(new Pawn(1, 1, Team.BLACK));
		b.setPiece(new Pawn(2, 1, Team.BLACK));
		b.setPiece(new Pawn(3, 1, Team.BLACK));
		b.setPiece(new Pawn(4, 1, Team.BLACK));
		b.setPiece(new Pawn(5, 1, Team.BLACK));
		b.setPiece(new Pawn(6, 1, Team.BLACK));
		b.setPiece(new Pawn(7, 1, Team.BLACK));

		b.setPiece(new Pawn(0, 6, Team.WHITE));
		b.setPiece(new Pawn(1, 6, Team.WHITE));
		b.setPiece(new Pawn(2, 6, Team.WHITE));
		b.setPiece(new Pawn(3, 6, Team.WHITE));
		b.setPiece(new Pawn(4, 6, Team.WHITE));
		b.setPiece(new Pawn(5, 6, Team.WHITE));
		b.setPiece(new Pawn(6, 6, Team.WHITE));
		b.setPiece(new Pawn(7, 6, Team.WHITE));
		b.setPiece(new Rook(0, 7, Team.WHITE));
		b.setPiece(new Knight(1, 7, Team.WHITE));
		b.setPiece(new Bishop(2, 7, Team.WHITE));
		b.setPiece(new Queen(3, 7, Team.WHITE));
		b.setPiece(new King(4, 7, Team.WHITE, true, true));
		b.setPiece(new Bishop(5, 7, Team.WHITE));
		b.setPiece(new Knight(6, 7, Team.WHITE));
		b.setPiece(new Rook(7, 7, Team.WHITE));

		b.setMoveMaker(Team.WHITE);

		return b.build();
	}

	public Piece getPiece(int x, int y) {
		return this.boardConfig.get(new Position(x, y));
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
		List<Piece> pieces = whitePieces;
		whitePieces.addAll(blackPieces);
		return pieces;
	}

	public Set<Move> getPossibleMoves(Piece piece) {
		Set<Move> moves = new HashSet<Move>();
		Set<Move> allTeamMoves;

		if (piece.getTeam() == Team.WHITE) {
			allTeamMoves = whitePlayer.getLegalMoves();
		} else {
			allTeamMoves = blackPlayer.getLegalMoves();
		}

		for (Move m : allTeamMoves) {
			if (piece.equals(m.getMovedPiece())) {
				moves.add(m);
			}
		}

		return moves;
	}

	public static class Builder {
		Map<Position, Piece> boardConfig;
		Team nextMoveMaker;

		public Builder() {
			this.boardConfig = new HashMap<Position, Piece>();
		}

		public Builder setPiece(Piece piece) {
			this.boardConfig.put(new Position(piece.getX(), piece.getY()), piece);
			return this;
		}

		public Builder setMoveMaker(Team nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}

		public Board build() {
			return new Board(this);
		}
	}
}