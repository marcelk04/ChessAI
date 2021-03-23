package com.chess.player;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.chess.move.Move.KingSideCastleMove;
import com.chess.move.Move.QueenSideCastleMove;
import com.chess.pieces.Piece;
import com.chess.pieces.Rook;
import com.chess.pieces.Team;
import com.chess.pieces.Piece.PieceType;

public class BlackPlayer extends Player {
	public BlackPlayer(Board board, List<Move> playerLegals, boolean[] playerAttackedSquares,
			boolean[] opponentAttackedSquares, boolean canKingSideCastle, boolean canQueenSideCastle) {
		super(board, playerLegals, playerAttackedSquares, opponentAttackedSquares, canKingSideCastle,
				canKingSideCastle);
	}

	@Override
	public List<Piece> getActivePieces() {
		return board.getBlackPieces();
	}

	@Override
	public Team getTeam() {
		return Team.BLACK;
	}

	@Override
	public Player getOpponent() {
		return board.getWhitePlayer();
	}

	@Override
	protected List<Move> calculateCastleMoves(boolean[] opponentAttackedSquares) {
		final List<Move> kingCastles = new ArrayList<Move>();

		if (canCastle()) {
			Piece castleRook;

			// king side castles
			if (canKingSideCastle && board.getPiece(5) == null && board.getPiece(6) == null) {
				castleRook = board.getPiece(7);
				if (!opponentAttackedSquares[5] && !opponentAttackedSquares[6] && castleRook != null
						&& castleRook.getType() == PieceType.ROOK) {
					kingCastles.add(new KingSideCastleMove(board, playerKing, 6, (Rook) castleRook, 5));
				}
			}

			// queen side castles
			if (canQueenSideCastle && board.getPiece(1) == null && board.getPiece(2) == null
					&& board.getPiece(3) == null) {
				castleRook = board.getPiece(0);
				if (!opponentAttackedSquares[1] && !opponentAttackedSquares[2] && !opponentAttackedSquares[3]
						&& castleRook != null && castleRook.getType() == PieceType.ROOK) {
					kingCastles.add(new QueenSideCastleMove(board, playerKing, 2, (Rook) castleRook, 3));
				}
			}
		}

		return kingCastles;
	}
}