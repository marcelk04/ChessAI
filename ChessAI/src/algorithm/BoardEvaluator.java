package algorithm;

import chess.Board;
import chess.pieces.Piece;
import chess.pieces.Team;
import chess.player.Player;

public abstract class BoardEvaluator {
	public abstract int evaluate(Board board, int depth);

	protected static int pieceScore(Player player) {
		int score = 0;
		for (Piece p : player.getActivePieces()) {
			score += p.getValue();
		}

		if (player.getTeam() == Team.BLACK)
			score *= -1;

		return score;
	}
}