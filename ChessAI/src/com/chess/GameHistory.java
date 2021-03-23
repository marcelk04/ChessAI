package com.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.move.Move;

public class GameHistory {
	private List<Move> moves;
	private Map<String, List<Float>> data;

	public GameHistory() {
		moves = new ArrayList<Move>();
		data = new HashMap<String, List<Float>>();
	}

	public void addDataElement(String tag, float element) {
		List<Float> selectedData;
		if ((selectedData = data.get(tag)) == null)
			data.put(tag, selectedData = new ArrayList<Float>());
		selectedData.add(element);
	}
}