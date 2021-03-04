package com.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.ai.hashing.ZobristHashing;
import com.chess.move.Move;
import com.chess.move.MoveTransition;
import com.chess.pieces.Bishop;
import com.chess.pieces.King;
import com.chess.pieces.Knight;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Queen;
import com.chess.pieces.Rook;
import com.chess.pieces.Team;
import com.chess.player.BlackPlayer;
import com.chess.player.Player;
import com.chess.player.WhitePlayer;
import com.main.Utils;

public class Board {
	private final Map<Integer, Piece> boardConfig;
	private final Pawn enPassantPawn;
	private final List<Piece> whitePieces, blackPieces;
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	private final long zobristHash;
	private final int halfmoveClock, halfmoveCounter;
	private final List<Move> executedMoves;
	private MoveTransition lastMoveTransition = null;

	private Board(Builder builder) {
		this.boardConfig = builder.boardConfig;
		this.enPassantPawn = builder.enPassantPawn;
		this.halfmoveClock = builder.halfmoveClock;
		this.halfmoveCounter = builder.halfmoveCounter;

		this.whitePieces = getActivePieces(this, Team.WHITE, 16);
		this.blackPieces = getActivePieces(this, Team.BLACK, 16);

		List<Move> whiteMoves = getLegalMoves(whitePieces);
		List<Move> blackMoves = getLegalMoves(blackPieces);

		this.whitePlayer = new WhitePlayer(this, whiteMoves, blackMoves,
				builder.castlingConfiguration.canWhiteKingSideCastle,
				builder.castlingConfiguration.canWhiteQueenSideCastle);
		this.blackPlayer = new BlackPlayer(this, blackMoves, whiteMoves,
				builder.castlingConfiguration.canBlackKingSideCastle,
				builder.castlingConfiguration.canBlackQueenSideCastle);

		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this);
		this.zobristHash = ZobristHashing.getZobristHash(this);
		this.executedMoves = new ArrayList<Move>();
		this.executedMoves.addAll(builder.executedMoves);
	}

	@Override
	public String toString() {
		String result = "";
		result += "White:" + whitePieces.size();
		result += ";Black:" + blackPieces.size();
		result += ";Current:" + currentPlayer.getTeam();
		return result;
	}

	public static Board create() {
		Builder b = new Builder();

		b.setPiece(new Rook(0, Team.BLACK));
		b.setPiece(new Knight(1, Team.BLACK));
		b.setPiece(new Bishop(2, Team.BLACK));
		b.setPiece(new Queen(3, Team.BLACK));
		b.setPiece(new King(4, Team.BLACK));
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
		b.setPiece(new King(60, Team.WHITE));
		b.setPiece(new Bishop(61, Team.WHITE));
		b.setPiece(new Knight(62, Team.WHITE));
		b.setPiece(new Rook(63, Team.WHITE));

		b.setMoveMaker(Team.WHITE);
		b.setEnPassantPawn(null);
		b.setCastlingConfiguration(CastlingConfiguration.ALL_TRUE);
		b.setHalfmoveClock(0);
		b.setHalfmoveCounter(1);
		b.setExecutedMoves(new ArrayList<Move>());

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

	public List<Move> findMoves(int currentPiecePosition, int pieceDestination) {
		List<Move> moves = new ArrayList<Move>();
		for (Move m : getAllLegalMoves()) { // search through all moves
			if (m.getCurrentPiecePosition() != currentPiecePosition || m.getPieceDestination() != pieceDestination)
				continue; // if not equal, skip
			moves.add(m); // else, add to moves
		}
		return moves;
	}

	public List<Move> findMoves(Piece movedPiece, int pieceDestination) {
		return findMoves(movedPiece.getPosition(), pieceDestination);
	}

	public boolean hasGameEnded() {
		return currentPlayer.isInCheckMate() || currentPlayer.isInStaleMate();
	}

	public String convertToFEN() {
		StringBuilder sb = new StringBuilder();

		// append piece positions
		for (int y = 0; y < 8; y++) {
			int squaresSincePiece = 0;

			for (int x = 0; x < 8; x++) {
				Piece p = getPiece(Utils.getIndex(x, y));
				if (p != null) {
					if (squaresSincePiece != 0)
						sb.append(squaresSincePiece);

					sb.append(p.getTeam() == Team.WHITE ? p.getType().getLetter()
							: (p.getType().getLetter() + "").toLowerCase());

					squaresSincePiece = 0;
				} else {
					squaresSincePiece++;

					if (x == 7)
						sb.append(squaresSincePiece);
				}
			}

			if (y != 7)
				sb.append("/");
		}

		sb.append(" ");

		// append current side to move
		if (currentPlayer.getTeam() == Team.WHITE)
			sb.append("w");
		else
			sb.append("b");

		sb.append(" ");

		// append castling rights
		int castlingRights = 0;

		if (whitePlayer.canKingSideCastle()) {
			sb.append("K");
			castlingRights++;
		}

		if (whitePlayer.canQueenSideCastle()) {
			sb.append("Q");
			castlingRights++;
		}

		if (blackPlayer.canKingSideCastle()) {
			sb.append("k");
			castlingRights++;
		}

		if (blackPlayer.canQueenSideCastle()) {
			sb.append("q");
			castlingRights++;
		}

		if (castlingRights == 0) {
			sb.append("-");
		}

		sb.append(" ");

		// en passant
		if (enPassantPawn != null) {
			sb.append(Utils.columns[Utils.getX(enPassantPawn.getPosition())]);

			if (enPassantPawn.getTeam() == Team.WHITE) {
				sb.append(7 - Utils.getY(enPassantPawn.getPosition()));
			} else {
				sb.append(9 - Utils.getY(enPassantPawn.getPosition()));
			}
		} else {
			sb.append("-");
		}

		sb.append(" ");

		// halfmove clock
		sb.append(halfmoveClock).append(" ");

		// fullmove counter
		sb.append(Math.round(halfmoveCounter / 2f));

		return sb.toString();
	}

	// ===== Getters ===== \\
	private static List<Piece> getActivePieces(Board board, Team team, int maxPieces) {
		final List<Piece> activePieces = new ArrayList<Piece>();

		for (int i = 0; i < 64 && activePieces.size() < maxPieces; i++) {
			Piece p = board.getPiece(i);
			if (p != null && p.getTeam() == team) {
				activePieces.add(p);
			}
		}

		return activePieces;
	}

	private List<Move> getLegalMoves(List<Piece> pieces) {
		final List<Move> legalMoves = new ArrayList<Move>();
		pieces.forEach(p -> legalMoves.addAll(p.getMoves(this)));
		return legalMoves;
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
		final List<Piece> allPieces = new ArrayList<Piece>();
		allPieces.addAll(whitePieces);
		allPieces.addAll(blackPieces);
		return allPieces;
	}

	public List<Move> getPossibleMoves(Piece piece) {
		final List<Move> movesToSearch = piece.getTeam().choosePlayer(this).getLegalMoves();
		final List<Move> possibleMoves = new ArrayList<Move>();

		for (Move m : movesToSearch) {
			if (m.getMovedPiece().equals(piece)) {
				possibleMoves.add(m);
			}
		}

		return possibleMoves;
	}

	public List<Move> getAllLegalMoves() {
		final List<Move> allLegalMoves = new ArrayList<Move>();
		allLegalMoves.addAll(whitePlayer.getLegalMoves());
		allLegalMoves.addAll(blackPlayer.getLegalMoves());
		return allLegalMoves;
	}

	public Team getWinner() {
		if (whitePlayer.isInCheckMate())
			return Team.BLACK;
		else if (blackPlayer.isInCheckMate())
			return Team.WHITE;
		return null;
	}

	public MoveTransition getLastMoveTransition() {
		return lastMoveTransition;
	}

	public long getZobristHash() {
		return zobristHash;
	}

	public int getHalfmoveClock() {
		return halfmoveClock;
	}

	public int getHalfmoveCounter() {
		return halfmoveCounter;
	}

	public List<Move> getExecutedMoves() {
		return executedMoves;
	}

	public String getResult() {
		if (!hasGameEnded())
			return "*"; // game has not yet ended

		Team winner = getWinner();

		if (winner == null) // no winner
			return "1/2-1/2";

		if (winner == Team.BLACK)
			return "0-1"; // black has won

		return "1-0"; // white has won
	}

	// ===== Setters ===== \\
	public void setLastMoveTransition(MoveTransition lastMoveTransition) {
		this.lastMoveTransition = lastMoveTransition;

		executedMoves.add(lastMoveTransition.getExecutedMove());
	}

	// ===== Inner classes ===== \\
	public static class Builder {
		Map<Integer, Piece> boardConfig;
		Team nextMoveMaker;
		Pawn enPassantPawn;
		CastlingConfiguration castlingConfiguration;
		int halfmoveClock;
		int halfmoveCounter;
		List<Move> executedMoves;

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

		public Builder setCastlingConfiguration(CastlingConfiguration castlingConfiguration) {
			this.castlingConfiguration = castlingConfiguration;
			return this;
		}

		public Builder setHalfmoveClock(int halfmoveClock) {
			this.halfmoveClock = halfmoveClock;
			return this;
		}

		public Builder setHalfmoveCounter(int halfmoveCounter) {
			this.halfmoveCounter = halfmoveCounter;
			return this;
		}

		public Builder setExecutedMoves(List<Move> executedMoves) {
			this.executedMoves = executedMoves;
			return this;
		}

		public Board build() {
			return new Board(this);
		}
	}
}