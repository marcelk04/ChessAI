package com.chess.ai.evaluation;

import com.chess.Board;
import com.chess.player.Player;

public class SimpleBoardEvaluator extends BoardEvaluator {
	public static final SimpleBoardEvaluator INSTANCE = new SimpleBoardEvaluator();

	protected static final int MOBILITY_BONUS = 5;
	protected static final int ATTACK_BONUS = 20;
	protected static final int WIN_BONUS = 100000;

	@Override
	public int evaluateWithoutHashing(Board board, int depth) {
		return score(board.getWhitePlayer(), board, depth) + score(board.getBlackPlayer(), board, depth)
				+ winScore(board, depth, WIN_BONUS);
	}

	protected static int score(Player player, Board board, int depth) {
		return pieceScore(player) + mobilityScore(player, MOBILITY_BONUS) + attackScore(player, ATTACK_BONUS);
	}

	@Override
	public String toString() {
		return "Simple Evaluator";
	}
}