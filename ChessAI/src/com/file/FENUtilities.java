package com.file;

import com.chess.Board;
import com.chess.Board.Builder;
import com.chess.pieces.King;
import com.chess.pieces.Team;
import com.main.Utils;

public class FENUtilities {
	// rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
	// r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3
	public static Board loadBoardFromFEN(String fen) {
		Builder b = new Builder();

		String[] fenParts = fen.split(" ");

		if (fenParts.length != 6)
			return null;
		
		parseBoardConfig(b, fenParts[0]);

		return null;
	}
	
	public static Builder parseBoardConfig(Builder builder, String boardConfig) {
		String[] board = boardConfig.split("/");

		if (board.length != 8)
			return null;

		for (int y = 0; y < 8; y++) {
			String row = board[y];

			int currentX = 0;

			for (int x = 0; x < row.length(); x++) {
				char currentPiece = row.charAt(currentX);

				if (Character.isDigit(currentPiece)) {
					currentX += Integer.valueOf(currentPiece);
				} else {
					Team team = Character.isUpperCase(currentPiece) ? Team.WHITE : Team.BLACK;
					int position = Utils.getIndex(currentX, y);

					switch (Character.toLowerCase(currentPiece)) {
					case 'k':
						builder.setPiece(new King(position, team, true));
						break;
					case 'q':
						break;
					case 'r':
						break;
					case 'b':
						break;
					case 'n':
						break;
					case 'p':
						break;
					default:
						return null;
					}

					currentX++;
				}
			}

			if (currentX != 8)
				return null;
		}
		
		return builder;
	}
}