package com.file;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.Board.Builder;
import com.chess.CastlingConfiguration;
import com.chess.pieces.Bishop;
import com.chess.pieces.King;
import com.chess.pieces.Knight;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Queen;
import com.chess.pieces.Rook;
import com.chess.pieces.Team;
import com.main.Utils;

public class FENUtilities {
	public static final String standardFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	// rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
	// r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3
	public static Board loadBoardFromFEN(String fen) {
		Builder b = new Builder();

		String[] fenParts = fen.split(" ");

		if (fenParts.length != 6)
			return null;

		Team moveMaker;

		parseBoardConfig(b, fenParts[0]);
		b.setMoveMaker(moveMaker = parseCurrentMoveMaker(fenParts[1]));
		parseCastlingConfig(b, fenParts[2]);
		parseEnPassantPawn(b, fenParts[3]);
		parseHalfmoveClock(b, fenParts[4]);
		parseHalfmoveCounter(b, fenParts[5], moveMaker);

		return b.build();
	}

	/**
	 * Converts the board into a {@link String} matching the FEN file format.
	 * 
	 * @return a {@link String} representation of the board in the FEN format.
	 */
	public static String convertToFEN(Board board) {
		StringBuilder sb = new StringBuilder();

		// append piece positions
		for (int y = 0; y < 8; y++) {
			int squaresSincePiece = 0;

			for (int x = 0; x < 8; x++) {
				Piece p = board.getPiece(Utils.getIndex(x, y));
				if (p != null) {
					if (squaresSincePiece != 0)
						sb.append(squaresSincePiece);

					sb.append(p.getTeam() == Team.WHITE ? p.getType().getLetter()
							: (p.getType().getLetter() + "").toLowerCase());

					squaresSincePiece = 0;
				} else {
					squaresSincePiece++;

					if (x == 7)
						sb.append(squaresSincePiece);
				}
			}

			if (y != 7)
				sb.append("/");
		}

		sb.append(" ");

		// append current side to move
		if (board.getCurrentPlayer().getTeam() == Team.WHITE)
			sb.append("w");
		else
			sb.append("b");

		sb.append(" ");

		// append castling rights
		int castlingRights = 0;

		if (board.getWhitePlayer().canKingSideCastle()) {
			sb.append("K");
			castlingRights++;
		}

		if (board.getWhitePlayer().canQueenSideCastle()) {
			sb.append("Q");
			castlingRights++;
		}

		if (board.getBlackPlayer().canKingSideCastle()) {
			sb.append("k");
			castlingRights++;
		}

		if (board.getBlackPlayer().canQueenSideCastle()) {
			sb.append("q");
			castlingRights++;
		}

		if (castlingRights == 0) {
			sb.append("-");
		}

		sb.append(" ");

		// append en passant pawn
		if (board.getEnPassantPawn() != null) {
			sb.append(Utils.columns[Utils.getX(board.getEnPassantPawn().getPosition())]);

			if (board.getEnPassantPawn().getTeam() == Team.WHITE) {
				sb.append(7 - Utils.getY(board.getEnPassantPawn().getPosition()));
			} else {
				sb.append(9 - Utils.getY(board.getEnPassantPawn().getPosition()));
			}
		} else {
			sb.append("-");
		}

		sb.append(" ");

		// append halfmove clock
		sb.append(board.getHalfmoveClock()).append(" ");

		// append fullmove counter
		sb.append(Math.round(board.getHalfmoveCounter() / 2f));

		return sb.toString();
	}

	private static Builder parseBoardConfig(Builder builder, String boardConfig) {
		String[] board = boardConfig.split("/");
		List<Piece> pieces = new ArrayList<Piece>();

		if (board.length != 8)
			return null;

		for (int y = 0; y < 8; y++) {
			String row = board[y];

			int currentX = 0;

			for (int x = 0; x < row.length(); x++) {
				char currentPiece = row.charAt(x);

				if (Character.isDigit(currentPiece)) {
					currentX += Integer.parseInt(currentPiece + "");
				} else {
					Team team = Character.isUpperCase(currentPiece) ? Team.WHITE : Team.BLACK;
					int position = Utils.getIndex(currentX, y);

					switch (Character.toLowerCase(currentPiece)) {
					case 'k':
						pieces.add(new King(position, team));
						break;
					case 'q':
						pieces.add(new Queen(position, team));
						break;
					case 'r':
						pieces.add(new Rook(position, team));
						break;
					case 'b':
						pieces.add(new Bishop(position, team));
						break;
					case 'n':
						pieces.add(new Knight(position, team));
						break;
					case 'p':
						pieces.add(new Pawn(position, team));
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

		for (Piece p : pieces) {
			builder.setPiece(p);
		}

		return builder;
	}

	private static Team parseCurrentMoveMaker(String currentMoveMaker) {
		if (currentMoveMaker.length() != 1)
			return null;

		return currentMoveMaker.equalsIgnoreCase("b") ? Team.BLACK : Team.WHITE;
	}

	private static Builder parseCastlingConfig(Builder builder, String castlingConfiguration) {
		if (castlingConfiguration.length() == 0 || castlingConfiguration.length() > 4) // if the String is empty or too
																						// long
			return null;

		if (castlingConfiguration.equals("-")) // if there are no possible castles
			return builder.setCastlingConfiguration(CastlingConfiguration.ALL_FALSE);

		CastlingConfiguration config = CastlingConfiguration.ALL_FALSE;

		for (char current : castlingConfiguration.toCharArray()) {
			switch (current) {
			case 'K':
				config.canWhiteKingSideCastle = true;
				break;
			case 'Q':
				config.canWhiteQueenSideCastle = true;
				break;
			case 'k':
				config.canBlackKingSideCastle = true;
				break;
			case 'q':
				config.canBlackQueenSideCastle = true;
				break;
			}
		}

		return builder.setCastlingConfiguration(config);
	}

	private static Builder parseEnPassantPawn(Builder builder, String enPassantPawn) {
		if (enPassantPawn.length() == 0 || enPassantPawn.length() > 2)
			return null;

		if (enPassantPawn.equals("-"))
			return builder;

		char[] arr = enPassantPawn.toCharArray();

		int file = Utils.getFile(arr[0]);

		if (file == -1)
			return null;

		int row = Integer.parseInt(arr[1] + "");

		Team team;

		if (row == 2) {
			row++;
			team = Team.BLACK;
		} else if (row == 5) {
			row--;
			team = Team.WHITE;
		} else {
			return null;
		}

		int position = Utils.getIndex(file, row);

		return builder.setEnPassantPawn(Utils.getMovedPawn(team, position));
	}

	private static Builder parseHalfmoveClock(Builder builder, String halfmoveClock) {
		if (halfmoveClock.length() != 1)
			return null;

		return builder.setHalfmoveClock(Integer.parseInt(halfmoveClock));
	}

	private static Builder parseHalfmoveCounter(Builder builder, String halfmoveCounter, Team currentMoveMaker) {
		if (halfmoveCounter.length() != 1)
			return null;

		int fullmoveCounter = Integer.parseInt(halfmoveCounter);

		int halfmoveCounterInt = fullmoveCounter * 2;
		if (currentMoveMaker == Team.WHITE)
			halfmoveCounterInt--;

		return builder.setHalfmoveCounter(halfmoveCounterInt);
	}
}