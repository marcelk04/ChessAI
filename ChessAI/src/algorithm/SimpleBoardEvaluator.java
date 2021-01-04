package algorithm;

import chess.Board;
import chess.player.Player;

public class SimpleBoardEvaluator extends BoardEvaluator {
	protected static final float MOBILITY_BONUS = 1f;
	protected static final float ATTACK_BONUS = 0.5f;
	protected static final float KING_ESCAPE_BONUS = 0.1f;

	@Override
	public int evaluate(Board board, int depth) {
		return score(board.getWhitePlayer(), board, depth) + score(board.getBlackPlayer(), board, depth);
	}

	protected static int score(Player player, Board board, int depth) {
		return pieceScore(player) + mobilityScore(player, MOBILITY_BONUS) + attackScore(player, ATTACK_BONUS)
				+ kingEscapeScore(player, board, KING_ESCAPE_BONUS);
	}
}