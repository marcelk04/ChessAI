package com.chess.ai.hashing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

	public static Map<Long, Integer> getTable() {
		return table;
	}

	public static void dumpTableIntoFile(String path) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
			for (long key : table.keySet()) {
				bw.write(key + " " + get(key));
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}