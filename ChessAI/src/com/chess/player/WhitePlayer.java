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

public class WhitePlayer extends Player {
	public WhitePlayer(Board board, List<Move> playerLegals, boolean[] playerAttackedSquares,
			boolean[] opponentAttackedSquares, boolean canKingSideCastle, boolean canQueenSideCastle) {
		super(board, playerLegals, playerAttackedSquares, opponentAttackedSquares, canKingSideCastle,
				canKingSideCastle);
	}

	@Override
	public List<Piece> getActivePieces() {
		return board.getWhitePieces();
	}

	@Override
	public Team getTeam() {
		return Team.WHITE;
	}

	@Override
	public Player getOpponent() {
		return board.getBlackPlayer();
	}

	@Override
	protected List<Move> calculateCastleMoves(boolean[] opponentAttackedSquares) {
		final List<Move> kingCastles = new ArrayList<Move>();

		if (canCastle()) {
			Piece castleRook;

			// king side castles
			if (board.getPiece(61) == null && board.getPiece(62) == null) {
				castleRook = board.getPiece(63);
				if (!opponentAttackedSquares[61] && !opponentAttackedSquares[62] && castleRook != null
						&& castleRook.getType() == PieceType.ROOK) {
					kingCastles.add(new KingSideCastleMove(board, playerKing, 62, (Rook) castleRook, 61));
				}
			}

			// queen side castles
			if (board.getPiece(57) == null && board.getPiece(58) == null
					&& board.getPiece(59) == null) {
				castleRook = board.getPiece(56);
				if (!opponentAttackedSquares[57] && !opponentAttackedSquares[58] && !opponentAttackedSquares[59]
						&& castleRook != null && castleRook.getType() == PieceType.ROOK) {
					kingCastles.add(new QueenSideCastleMove(board, playerKing, 58, (Rook) castleRook, 59));
				}
			}
		}

		return kingCastles;
	}
}