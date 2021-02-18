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
	public WhitePlayer(Board board, List<Move> playerLegals, List<Move> opponentLegals, boolean canKingSideCastle,
			boolean canQueenSideCastle) {
		super(board, playerLegals, opponentLegals, canKingSideCastle, canKingSideCastle);
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
	protected List<Move> calculateCastleMoves(List<Move> opponentLegals) {
		List<Move> kingCastles = new ArrayList<Move>();

		if (canCastle()) {
			Piece castleRook;

			// king side castles
			if (canKingSideCastle && board.getPiece(61) == null && board.getPiece(62) == null) {
				castleRook = board.getPiece(63);
				if (calculateAttacksOnTile(61, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(62, opponentLegals).isEmpty() && castleRook != null
						&& castleRook.getType() == PieceType.ROOK) {
					kingCastles.add(new KingSideCastleMove(board, playerKing, 62, (Rook) castleRook, 61));
				}
			}

			// queen side castles
			if (canQueenSideCastle && board.getPiece(57) == null && board.getPiece(58) == null
					&& board.getPiece(59) == null) {
				castleRook = board.getPiece(56);
				if (calculateAttacksOnTile(57, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(58, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(59, opponentLegals).isEmpty() && castleRook != null
						&& castleRook.getType() == PieceType.ROOK) {
					kingCastles.add(new QueenSideCastleMove(board, playerKing, 58, (Rook) castleRook, 59));
				}
			}
		}

		return kingCastles;
	}
}