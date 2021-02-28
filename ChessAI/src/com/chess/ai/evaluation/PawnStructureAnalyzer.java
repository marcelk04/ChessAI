package com.chess.ai.evaluation;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Piece.PieceType;
import com.chess.pieces.Team;
import com.main.Utils;

public class PawnStructureAnalyzer {
	public static final PawnStructureAnalyzer INSTANCE = new PawnStructureAnalyzer();

	public Pawn[][] findPawns(Board board, int cutoff) {
		Pawn[][] pawns = new Pawn[8][8];
		int numPawns = 0;

		for (Piece p : board.getAllPieces()) {
			if (p.getType() == PieceType.PAWN) {
				pawns[Utils.getX(p.getPosition())][Utils.getY(p.getPosition())] = (Pawn) p;
				numPawns++;
			}

			if (numPawns >= cutoff)
				break;
		}

		return pawns;
	}

	public int doubledPawns(Pawn[][] pawns, Team team) {
		int doubledPawns = 0;

		for (int x = 0; x < 8; x++) {
			boolean pawnFound = false;
			for (int y = 0; y < 8; y++) {
				Pawn current = pawns[x][y];
				if (current == null || current.getTeam() != team) {
					pawnFound = false;
					continue;
				}

				if (pawnFound) // doubledPawn
					doubledPawns++;

				pawnFound = true;
			}
		}

		return doubledPawns;
	}

	public int isolatedPawns(Pawn[][] pawns, Team team) {
		int isolatedPawns = 0;

		int[] pawnsPerFile = new int[8];

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Pawn current = pawns[x][y];
				if (current == null || current.getTeam() != team)
					continue;

				pawnsPerFile[x]++;
			}
		}

		int left = 0, middle = pawnsPerFile[0], right = pawnsPerFile[1];

		for (int x = 0; x < 8; x++) {
			if (left == 0 && right == 0 && middle > 0)
				isolatedPawns += middle;

			left = middle;
			middle = right;

			if (x < 6)
				right = pawnsPerFile[x + 2];
			else
				right = 0;
		}

		return isolatedPawns;
	}

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