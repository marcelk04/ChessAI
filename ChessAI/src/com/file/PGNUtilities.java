package com.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.chess.move.Move;

public class PGNUtilities {
	public static void saveGameToPGN(File file, String event, String site, Date date, int round, String whitePlayer,
			String blackPlayer, String result, List<Move> executedMoves) throws IOException {
		String formattedDate = new SimpleDateFormat("yyyy.MM.dd").format(date);

		StringBuilder sb = new StringBuilder();
		sb.append(getTagPair("Event", event)).append("\n");
		sb.append(getTagPair("Site", site)).append("\n");
		sb.append(getTagPair("Date", formattedDate)).append("\n");
		sb.append(getTagPair("Round", round + "")).append("\n");
		sb.append(getTagPair("White", whitePlayer)).append("\n");
		sb.append(getTagPair("Black", blackPlayer)).append("\n");
		sb.append(getTagPair("Result", result)).append("\n\n");

		for (int i = 0; i < executedMoves.size(); i++) {
			if (i % 2 == 0) {
				sb.append(i / 2 + 1).append(". ");
			}
			sb.append(executedMoves.get(i).getNotation() + " ");
		}

		sb.append(result);

		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

		bw.write(sb.toString());

		bw.close();
	}

	private static String getTagPair(String tag, String data) {
		return "[" + tag + " \"" + data + "\"]";
	}
}