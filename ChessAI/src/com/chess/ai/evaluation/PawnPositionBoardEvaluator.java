package com.chess.ai.evaluation;

import com.chess.Board;
import com.chess.player.Player;

public class PawnPositionBoardEvaluator extends PositionBoardEvaluator {
	public static final PawnPositionBoardEvaluator INSTANCE = new PawnPositionBoardEvaluator();

	private static final int PASSED_PAWN_BONUS = 10;

	@Override
	public int evaluateWithoutHashing(Board board, int depth) {
		return score(board.getWhitePlayer(), board, depth) + score(board.getBlackPlayer(), board, depth);
	}

	protected static int score(Player player, Board board, int depth) {
		return PositionBoardEvaluator.score(player, board, depth)
				+ passedPawnScore(board, player.getTeam(), PASSED_PAWN_BONUS);
	}
}