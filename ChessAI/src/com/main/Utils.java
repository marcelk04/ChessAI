package com.main;

import java.util.HashMap;
import java.util.Map;

import com.chess.pieces.Bishop;
import com.chess.pieces.Knight;
import com.chess.pieces.Pawn;
import com.chess.pieces.Queen;
import com.chess.pieces.Rook;
import com.chess.pieces.Team;

public class Utils {
	public static final char[] columns = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

	private static final Map<Team, Map<Integer, Queen>> ALL_POSSIBLE_QUEENS = createAllPossibleQueens();
	private static final Map<Team, Map<Integer, Rook>> ALL_POSSIBLE_ROOKS = createAllPossibleRooks();
	private static final Map<Team, Map<Integer, Bishop>> ALL_POSSIBLE_BISHOPS = createAllPossibleBishops();
	private static final Map<Team, Map<Integer, Knight>> ALL_POSSIBLE_KNIGHTS = createAllPossibleKnights();
	private static final Map<Team, Map<Integer, Pawn>> ALL_POSSIBLE_PAWNS = createAllPossiblePawns();

	public static boolean inRange(int var, int min, int max) {
		return var >= min && var <= max;
	}

	public static int clamp(int var, int min, int max) {
		if (var <= min)
			return min;
		else if (var >= max)
			return max;
		else
			return var;
	}

	public static int getX(int index) {
		return index & 7;
	}

	public static int getY(int index) {
		return index >> 3;
	}

	public static int getIndex(int x, int y) {
		return x + y * 8;
	}

	public static double round(double number, int decimalPlaces) {
		double factor = Math.pow(10, decimalPlaces);
		return Math.round(number * factor) / factor;
	}

	public static Queen getMovedQueen(Team team, int position) {
		return ALL_POSSIBLE_QUEENS.get(team).get(position);
	}

	public static Rook getMovedRook(Team team, int position) {
		return ALL_POSSIBLE_ROOKS.get(team).get(position);
	}

	public static Bishop getMovedBishop(Team team, int position) {
		return ALL_POSSIBLE_BISHOPS.get(team).get(position);
	}

	public static Knight getMovedKnight(Team team, int position) {
		return ALL_POSSIBLE_KNIGHTS.get(team).get(position);
	}

	public static Pawn getMovedPawn(Team team, int position) {
		return ALL_POSSIBLE_PAWNS.get(team).get(position);
	}

	private static Map<Team, Map<Integer, Queen>> createAllPossibleQueens() {
		Map<Team, Map<Integer, Queen>> allPossibleQueens = new HashMap<Team, Map<Integer, Queen>>();
		Map<Integer, Queen> allPossibleWhiteQueens = new HashMap<Integer, Queen>();
		Map<Integer, Queen> allPossibleBlackQueens = new HashMap<Integer, Queen>();

		for (int i = 0; i < 64; i++) {
			allPossibleWhiteQueens.put(i, new Queen(i, Team.WHITE, true));
			allPossibleBlackQueens.put(i, new Queen(i, Team.BLACK, true));
		}

		allPossibleQueens.put(Team.WHITE, allPossibleWhiteQueens);
		allPossibleQueens.put(Team.BLACK, allPossibleBlackQueens);

		return allPossibleQueens;
	}

	private static Map<Team, Map<Integer, Rook>> createAllPossibleRooks() {
		Map<Team, Map<Integer, Rook>> allPossibleRooks = new HashMap<Team, Map<Integer, Rook>>();
		Map<Integer, Rook> allPossibleWhiteRooks = new HashMap<Integer, Rook>();
		Map<Integer, Rook> allPossibleBlackRooks = new HashMap<Integer, Rook>();

		for (int i = 0; i < 64; i++) {
			allPossibleWhiteRooks.put(i, new Rook(i, Team.WHITE, true));
			allPossibleBlackRooks.put(i, new Rook(i, Team.BLACK, true));
		}

		allPossibleRooks.put(Team.WHITE, allPossibleWhiteRooks);
		allPossibleRooks.put(Team.BLACK, allPossibleBlackRooks);

		return allPossibleRooks;
	}

	private static Map<Team, Map<Integer, Bishop>> createAllPossibleBishops() {
		Map<Team, Map<Integer, Bishop>> allPossibleBishops = new HashMap<Team, Map<Integer, Bishop>>();
		Map<Integer, Bishop> allPossibleWhiteBishops = new HashMap<Integer, Bishop>();
		Map<Integer, Bishop> allPossibleBlackBishops = new HashMap<Integer, Bishop>();

		for (int i = 0; i < 64; i++) {
			allPossibleWhiteBishops.put(i, new Bishop(i, Team.WHITE, true));
			allPossibleBlackBishops.put(i, new Bishop(i, Team.BLACK, true));
		}

		allPossibleBishops.put(Team.WHITE, allPossibleWhiteBishops);
		allPossibleBishops.put(Team.BLACK, allPossibleBlackBishops);

		return allPossibleBishops;
	}

	private static Map<Team, Map<Integer, Knight>> createAllPossibleKnights() {
		Map<Team, Map<Integer, Knight>> allPossibleKnights = new HashMap<Team, Map<Integer, Knight>>();
		Map<Integer, Knight> allPossibleWhiteKnights = new HashMap<Integer, Knight>();
		Map<Integer, Knight> allPossibleBlackKnights = new HashMap<Integer, Knight>();

		for (int i = 0; i < 64; i++) {
			allPossibleWhiteKnights.put(i, new Knight(i, Team.WHITE, true));
			allPossibleBlackKnights.put(i, new Knight(i, Team.BLACK, true));
		}

		allPossibleKnights.put(Team.WHITE, allPossibleWhiteKnights);
		allPossibleKnights.put(Team.BLACK, allPossibleBlackKnights);

		return allPossibleKnights;
	}

	private static Map<Team, Map<Integer, Pawn>> createAllPossiblePawns() {
		Map<Team, Map<Integer, Pawn>> allPossiblePawns = new HashMap<Team, Map<Integer, Pawn>>();
		Map<Integer, Pawn> allPossibleWhitePawns = new HashMap<Integer, Pawn>();
		Map<Integer, Pawn> allPossibleBlackPawns = new HashMap<Integer, Pawn>();

		for (int i = 0; i < 64; i++) {
			allPossibleWhitePawns.put(i, new Pawn(i, Team.WHITE, true));
			allPossibleBlackPawns.put(i, new Pawn(i, Team.BLACK, true));
		}

		allPossiblePawns.put(Team.WHITE, allPossibleWhitePawns);
		allPossiblePawns.put(Team.BLACK, allPossibleBlackPawns);

		return allPossiblePawns;
	}
}