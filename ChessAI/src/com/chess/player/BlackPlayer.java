package com.chess.player;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.chess.move.Move.KingSideCastleMove;
import com.chess.move.Move.QueenSideCastleMove;
import com.chess.pieces.Piece;
import com.chess.pieces.PieceType;
import com.chess.pieces.Rook;
import com.chess.pieces.Team;

public class BlackPlayer extends Player {
	public BlackPlayer(Board board, List<Move> playerLegals, List<Move> opponentLegals) {
		super(board, playerLegals, opponentLegals);
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
	protected List<Move> calculateCastleMoves(List<Move> opponentLegals) {
		List<Move> kingCastles = new ArrayList<Move>();

		if (canCastle() && !playerKing.gotMovedAtLeastOnce() && playerKing.getPosition() == 4 && !kingInCheck) {
			Piece castleRook;

			// king side castles
			if (board.getPiece(5) == null && board.getPiece(6) == null) {
				castleRook = board.getPiece(7);
				if (castleRook != null && castleRook.getType() == PieceType.ROOK && !castleRook.gotMovedAtLeastOnce()
						&& calculateAttacksOnTile(5, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(6, opponentLegals).isEmpty()) {
					kingCastles.add(new KingSideCastleMove(board, playerKing, 6, (Rook) castleRook, 5));
				}
			}

			// queen side castles
			if (board.getPiece(1) == null && board.getPiece(2) == null && board.getPiece(3) == null) {
				castleRook = board.getPiece(0);
				if (castleRook != null && castleRook.getType() == PieceType.ROOK && !castleRook.gotMovedAtLeastOnce()
						&& calculateAttacksOnTile(1, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(2, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(3, opponentLegals).isEmpty()) {
					kingCastles.add(new QueenSideCastleMove(board, playerKing, 2, (Rook) castleRook, 3));
				}
			}
		}

		return kingCastles;
	}
}