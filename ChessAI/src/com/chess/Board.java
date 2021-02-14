package com.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Board {
	private final Map<Integer, Piece> boardConfig;
	private final Pawn enPassantPawn;
	private final List<Piece> whitePieces, blackPieces;
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	private MoveTransition lastMoveTransition = null;

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
		List<Move> legalMoves = new ArrayList<Move>();
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
		List<Piece> allPieces = new ArrayList<Piece>();
		allPieces.addAll(whitePieces);
		allPieces.addAll(blackPieces);
		return allPieces;
	}

	public List<Move> getPossibleMoves(Piece piece) {
		List<Move> movesToSearch = piece.getTeam() == Team.WHITE ? whitePlayer.getLegalMoves()
				: blackPlayer.getLegalMoves();
		List<Move> possibleMoves = new ArrayList<Move>();
		for (Move m : movesToSearch) {
			if (m.getMovedPiece().equals(piece)) {
				possibleMoves.add(m);
			}
		}
		return possibleMoves;
	}

	public List<Move> getAllLegalMoves() {
		List<Move> allLegalMoves = new ArrayList<Move>();
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

	// ===== Setters ===== \\
	public void setLastMoveTransition(MoveTransition lastMoveTransition) {
		this.lastMoveTransition = lastMoveTransition;
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