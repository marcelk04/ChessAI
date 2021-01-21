package com.chess.algorithm;

import com.chess.Board;
import com.chess.move.Move;
import com.chess.pieces.Piece;
import com.chess.pieces.Team;
import com.chess.player.Player;

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

	protected static int mobilityScore(Player player, int mobilityBonus) {
		int score = player.getLegalMoves().size() * mobilityBonus;

		if (player.getTeam() == Team.BLACK)
			score *= -1;

		return score;
	}

	protected static int attackScore(Player player, int attackBonus) {
		int score = 0;

		for (Move m : player.getLegalMoves()) {
			if (m.isAttackMove())
				score += attackBonus;
		}

		if (player.getTeam() == Team.BLACK)
			score *= -1;

		return score;
	}

	protected static int kingEscapeScore(Player player, Board board, int kingEscapeBonus) {
		int score = board.getPossibleMoves(player.getKing()).size() * kingEscapeBonus;

		if (player.getTeam() == Team.BLACK)
			score *= -1;

		return score;
	}
}