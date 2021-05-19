package com.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.ai.hashing.ZobristHashing;
import com.chess.move.Move;
import com.chess.move.MoveTransition;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Piece.PieceType;
import com.chess.pieces.Team;
import com.chess.player.BlackPlayer;
import com.chess.player.Player;
import com.chess.player.WhitePlayer;
import com.file.FENUtilities;

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
	private final List<Long> previousBoards;
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

		boolean[] whiteAttackedSquares = calculateAttackedSquares(this, whiteMoves, whitePieces);
		boolean[] blackAttackedSquares = calculateAttackedSquares(this, blackMoves, blackPieces);

		this.whitePlayer = new WhitePlayer(this, whiteMoves, whiteAttackedSquares, blackAttackedSquares,
				builder.castlingConfiguration.canWhiteKingSideCastle,
				builder.castlingConfiguration.canWhiteQueenSideCastle);
		this.blackPlayer = new BlackPlayer(this, blackMoves, blackAttackedSquares, whiteAttackedSquares,
				builder.castlingConfiguration.canBlackKingSideCastle,
				builder.castlingConfiguration.canBlackQueenSideCastle);

		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this);
		this.zobristHash = ZobristHashing.getZobristHash(this);
		this.executedMoves = new ArrayList<Move>();
		if (builder.executedMoves != null)
			this.executedMoves.addAll(builder.executedMoves);
		this.previousBoards = builder.previousBoards;
	}

	@Override
	public String toString() {
		String result = "";
		result += "White:" + whitePieces.size();
		result += ";Black:" + blackPieces.size();
		result += ";Current:" + currentPlayer.getTeam();
		return result;
	}

	/**
	 * Creates a new chess board with the standard layout.
	 * 
	 * @return the standard board.
	 */
	public static Board create() {
//		Builder b = new Builder();
//
//		b.setPiece(new Rook(0, Team.BLACK));
//		b.setPiece(new Knight(1, Team.BLACK));
//		b.setPiece(new Bishop(2, Team.BLACK));
//		b.setPiece(new Queen(3, Team.BLACK));
//		b.setPiece(new King(4, Team.BLACK));
//		b.setPiece(new Bishop(5, Team.BLACK));
//		b.setPiece(new Knight(6, Team.BLACK));
//		b.setPiece(new Rook(7, Team.BLACK));
//		b.setPiece(new Pawn(8, Team.BLACK));
//		b.setPiece(new Pawn(9, Team.BLACK));
//		b.setPiece(new Pawn(10, Team.BLACK));
//		b.setPiece(new Pawn(11, Team.BLACK));
//		b.setPiece(new Pawn(12, Team.BLACK));
//		b.setPiece(new Pawn(13, Team.BLACK));
//		b.setPiece(new Pawn(14, Team.BLACK));
//		b.setPiece(new Pawn(15, Team.BLACK));
//
//		b.setPiece(new Pawn(48, Team.WHITE));
//		b.setPiece(new Pawn(49, Team.WHITE));
//		b.setPiece(new Pawn(50, Team.WHITE));
//		b.setPiece(new Pawn(51, Team.WHITE));
//		b.setPiece(new Pawn(52, Team.WHITE));
//		b.setPiece(new Pawn(53, Team.WHITE));
//		b.setPiece(new Pawn(54, Team.WHITE));
//		b.setPiece(new Pawn(55, Team.WHITE));
//		b.setPiece(new Rook(56, Team.WHITE));
//		b.setPiece(new Knight(57, Team.WHITE));
//		b.setPiece(new Bishop(58, Team.WHITE));
//		b.setPiece(new Queen(59, Team.WHITE));
//		b.setPiece(new King(60, Team.WHITE));
//		b.setPiece(new Bishop(61, Team.WHITE));
//		b.setPiece(new Knight(62, Team.WHITE));
//		b.setPiece(new Rook(63, Team.WHITE));
//
//		b.setMoveMaker(Team.WHITE);
//		b.setEnPassantPawn(null);
//		b.setCastlingConfiguration(CastlingConfiguration.ALL_TRUE);
//		b.setHalfmoveClock(0);
//		b.setHalfmoveCounter(1);
//		b.setExecutedMoves(new ArrayList<Move>());
//
//		return b.build();

		return FENUtilities.loadBoardFromFEN(FENUtilities.standardFEN);
	}

	/**
	 * Searches all moves and returns the first move with the specific current piece
	 * position and piece destination. Returns {@link Move#NULL_MOVE} if no move
	 * matching the parameters could be found.
	 * 
	 * @param currentPiecePosition the current position of the piece.
	 * @param pieceDestination     the destination of the piece.
	 * @return the first move matching the parameters.
	 */
	public Move findMove(int currentPiecePosition, int pieceDestination) {
		for (Move m : getAllLegalMoves()) {
			if (m.getCurrentPiecePosition() == currentPiecePosition && m.getPieceDestination() == pieceDestination) {
				return m;
			}
		}
		return Move.NULL_MOVE;
	}

	/**
	 * Searches all moves and returns a {@link List} of all moves with the specific
	 * current piece position and peice destination.
	 * 
	 * @param currentPiecePosition the current position of the piece.
	 * @param pieceDestination     the destination of the piece.
	 * @return a {@link List} of all moves matching the parameters.
	 */
	public List<Move> findMoves(int currentPiecePosition, int pieceDestination) {
		final List<Move> moves = new ArrayList<Move>();
		for (Move m : getAllLegalMoves()) { // search through all moves
			if (m.getCurrentPiecePosition() != currentPiecePosition || m.getPieceDestination() != pieceDestination)
				continue; // if not equal, skip
			moves.add(m); // else, add to moves
		}
		return moves;
	}

	/**
	 * Searches all moves and returns a {@link List} of all moves with the specific
	 * piece and peice destination.
	 * 
	 * @param movedPiece       the moved piece.
	 * @param pieceDestination the destination of the piece.
	 * @return a {@link List} of all moves matching the parameters.
	 * @see Board#findMoves(int, int)
	 */
	public List<Move> findMoves(Piece movedPiece, int pieceDestination) {
		return findMoves(movedPiece.getPosition(), pieceDestination);
	}

	/**
	 * Checks whether the game has ended. This can be throught checkmate or
	 * stalemate.
	 * 
	 * @return {@code true} if the game has ended.
	 */
	public boolean hasGameEnded() {
		int timesBoardSeen = 1;
		if (previousBoards != null) {
			for (long l : previousBoards) {
				if (l == zobristHash)
					timesBoardSeen++;
			}
		}

		return currentPlayer.isInCheckMate() || currentPlayer.isInStaleMate() || halfmoveClock >= 50
				|| timesBoardSeen >= 3;
	}

	public static boolean[] calculateAttackedSquares(Board board, List<Move> moves, List<Piece> pieces) {
		final boolean[] attackedSquares = new boolean[64];

		for (Move m : moves) {
			if (!m.isPawnMove()) {
				attackedSquares[m.getPieceDestination()] = true;
			}
		}

		for (Piece p : pieces) {
			if (p.getType() == PieceType.PAWN) {
				for (Move m : ((Pawn) p).calculateAttackMoves(board)) {
					attackedSquares[m.getPieceDestination()] = true;
				}
			}
		}

		return attackedSquares;
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

	public List<Long> getPreviousBoards() {
		return previousBoards;
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
		List<Long> previousBoards;

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

		public Builder setPreviousBoards(List<Long> previousBoards) {
			this.previousBoards = previousBoards;
			return this;
		}

		public Board build() {
			return new Board(this);
		}
	}
}