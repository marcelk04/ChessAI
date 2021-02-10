package com.chess.algorithm;

import com.chess.Board;
import com.chess.player.Player;

public class PositionBoardEvaluator extends SimpleBoardEvaluator {
	private static final PositionBoardEvaluator INSTANCE = new PositionBoardEvaluator();

	protected static final int POSITION_BONUS = 1;

	public static PositionBoardEvaluator get() {
		return INSTANCE;
	}

	@Override
	public int evaluate(Board board, int depth) {
		return score(board.getWhitePlayer(), board, depth) + score(board.getBlackPlayer(), board, depth);
	}

	protected static int score(Player player, Board board, int depth) {
		return SimpleBoardEvaluator.score(player, board, depth) + piecePositionScore(player, POSITION_BONUS);
	}
}