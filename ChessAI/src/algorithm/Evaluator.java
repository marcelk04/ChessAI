package algorithm;

import chess.Board;
import chess.pieces.Team;

public class Evaluator {
	public static int evaluateBoard(Board board) {
		return Team.WHITE.getEval(board) + Team.BLACK.getEval(board);
	}

	public static boolean hasGameEnded(Board board) {
		return board.getCurrentPlayer().isInCheckMate() || board.getCurrentPlayer().isInStaleMate();
	}
}