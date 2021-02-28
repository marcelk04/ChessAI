package com.main;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
	public static final List<Float> searchTimes = new ArrayList<Float>();
	public static final List<Float> searchedBoards = new ArrayList<Float>();
	public static final List<Float> prunedBoards = new ArrayList<Float>();
	public static final List<Float> timesPruned = new ArrayList<Float>();
	public static final List<Float> prunedBoardsPercent = new ArrayList<Float>();
	public static final List<Float> transpositions = new ArrayList<Float>();
	public static final List<Float> transpositionsPercent = new ArrayList<Float>();

	public static List<Float> calculateAverage(List<Float> data) {
		final List<Float> averageData = new ArrayList<Float>();
		float average = 0;

		for (int i = 0; i < data.size();) {
			average = (average * i + data.get(i)) / ++i;
			averageData.add(average);
		}

		return averageData;
	}

	public static List<Float> calculateDerivative(List<Float> data) {
		final List<Float> derivative = new ArrayList<Float>();

		for (int i = 0; i < data.size() - 1; i++) {
			derivative.add(data.get(i + 1) - data.get(i));
		}

		return derivative;
	}

	public static void clear() {
		searchTimes.clear();
		searchedBoards.clear();
		prunedBoards.clear();
		timesPruned.clear();
		prunedBoardsPercent.clear();
	}
}