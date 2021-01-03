package algorithm;

import chess.Board;
import chess.player.Player;

public class SimpleBoardEvaluator extends BoardEvaluator {
	protected static final int MOBILITY_BONUS = 3;
	protected static final int ATTACK_BONUS = 1;

	@Override
	public int evaluate(Board board, int depth) {
		return score(board.getWhitePlayer(), depth) + score(board.getBlackPlayer(), depth);
	}

	protected static int score(Player player, int depth) {
		return pieceScore(player) + mobilityScore(player, MOBILITY_BONUS) + attackScore(player, ATTACK_BONUS);
	}
}