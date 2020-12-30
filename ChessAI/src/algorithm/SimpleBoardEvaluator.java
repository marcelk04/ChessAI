package algorithm;

import chess.Board;

public class SimpleBoardEvaluator extends BoardEvaluator {
	@Override
	public int evaluate(Board board, int depth) {
		return pieceScore(board.getWhitePlayer()) + pieceScore(board.getBlackPlayer());
	}
}