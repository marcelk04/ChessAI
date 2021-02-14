package com.chess.ai.hashing;

import java.util.HashMap;
import java.util.Map;

public class TranspositionTable {
	private static final Map<Long, Integer> table = new HashMap<Long, Integer>();

	public static void put(long key, int value) {
		table.put(key, value);
	}

	public static Integer get(long key) {
		return table.get(key);
	}
}