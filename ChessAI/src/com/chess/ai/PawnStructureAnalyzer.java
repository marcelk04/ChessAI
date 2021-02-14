package com.chess.ai;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Piece.PieceType;
import com.chess.pieces.Team;
import com.main.Utils;

public class PawnStructureAnalyzer {
	public static PawnStructureAnalyzer INSTANCE = new PawnStructureAnalyzer();

	public List<Pawn> findPassedPawns(Board board, Team team) {
		List<Pawn> passedPawns = new ArrayList<Pawn>();

		if (team == Team.WHITE) {
			final int[] columnFreeUntil = new int[8];

			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					columnFreeUntil[x] = y;
					Piece pieceAtCoordinate = board.getPiece(Utils.getIndex(x, y));

					if (pieceAtCoordinate != null && pieceAtCoordinate.getTeam() == Team.BLACK
							&& pieceAtCoordinate.getType() == PieceType.PAWN)
						break;
				}
			}

			for (Piece p : board.getWhitePieces()) {
				if (p.getType() != PieceType.PAWN)
					continue;

				final int x = Utils.getX(p.getPosition()), y = Utils.getY(p.getPosition());

				if (x != 0 && columnFreeUntil[x - 1] < y) // check left
					continue;

				if (x != 7 && columnFreeUntil[x + 1] < y) // check right
					continue;

				if (columnFreeUntil[x] < y) // check middle
					continue;

				passedPawns.add((Pawn) p);
			}
		} else {
			final int[] columnFreeFrom = new int[8];

			for (int x = 0; x < 8; x++) {
				for (int y = 7; y >= 0; y--) {
					columnFreeFrom[x] = y;
					Piece pieceAtCoordinate = board.getPiece(Utils.getIndex(x, y));

					if (pieceAtCoordinate != null && pieceAtCoordinate.getTeam() == Team.WHITE
							&& pieceAtCoordinate.getType() == PieceType.PAWN)
						break;
				}
			}

			for (Piece p : board.getBlackPieces()) {
				if (p.getType() != PieceType.PAWN)
					continue;

				final int x = Utils.getX(p.getPosition()), y = Utils.getY(p.getPosition());

				if (x != 0 && columnFreeFrom[x - 1] > y) // check left
					continue;

				if (x != 7 && columnFreeFrom[x + 1] > y) // check right
					continue;

				if (columnFreeFrom[x] > y) // check middle
					continue;

				passedPawns.add((Pawn) p);
			}
		}

		return passedPawns;
	}
}