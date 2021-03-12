package com.chess.ai.hashing;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.chess.Board;
import com.chess.pieces.Piece;
import com.chess.pieces.Team;
import com.chess.pieces.Piece.PieceType;
import com.main.Utils;

public class ZobristHashing {
	static {
		initialiseRandoms();
	}

	public static Map<PieceType, long[]> whitePiecesRandoms;
	public static Map<PieceType, long[]> blackPiecesRandoms;
	public static long blackToMove;
	public static long[] castlingRandoms;
	public static long[] enPassantFiles;

	private static long key = 233414022021L;

	public static void initialiseRandoms() {
		Random rnd = new Random(key);

		whitePiecesRandoms = new HashMap<PieceType, long[]>();
		blackPiecesRandoms = new HashMap<PieceType, long[]>();

		for (PieceType type : PieceType.values()) {
			long[] whiteRandoms = new long[64];
			long[] blackRandoms = new long[64];

			for (int i = 0; i < 64; i++) {
				whiteRandoms[i] = rnd.nextLong();
				blackRandoms[i] = rnd.nextLong();
			}

			whitePiecesRandoms.put(type, whiteRandoms);
			blackPiecesRandoms.put(type, blackRandoms);
		}

		blackToMove = rnd.nextLong();

		castlingRandoms = new long[4];
		for (int i = 0; i < 4; i++) {
			castlingRandoms[i] = rnd.nextLong();
		}

		enPassantFiles = new long[8];
		for (int i = 0; i < 8; i++) {
			enPassantFiles[i] = rnd.nextLong();
		}
	}

	public static long getZobristHash(Board board) {
		long hash = 0;

		for (Piece p : board.getWhitePieces())
			hash ^= whitePiecesRandoms.get(p.getType())[p.getPosition()];

		for (Piece p : board.getBlackPieces())
			hash ^= blackPiecesRandoms.get(p.getType())[p.getPosition()];

		if (board.getCurrentPlayer().getTeam() == Team.BLACK)
			hash ^= blackToMove;

		if (board.getWhitePlayer().canKingSideCastle())
			hash ^= castlingRandoms[0];

		if (board.getWhitePlayer().canQueenSideCastle())
			hash ^= castlingRandoms[1];

		if (board.getBlackPlayer().canKingSideCastle())
			hash ^= castlingRandoms[2];

		if (board.getBlackPlayer().canQueenSideCastle())
			hash ^= castlingRandoms[3];

		if (board.getEnPassantPawn() != null)
			hash ^= enPassantFiles[Utils.getX(board.getEnPassantPawn().getPosition())];

		return hash;
	}
}