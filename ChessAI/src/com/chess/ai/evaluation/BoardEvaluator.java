package com.chess.ai.evaluation;

import com.chess.Board;
import com.chess.ai.PawnStructureAnalyzer;
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

		score *= -player.getTeam().moveDirection();

		return score;
	}

	protected static int mobilityScore(Player player, int mobilityBonus) {
		int score = player.getLegalMoves().size() * mobilityBonus;

		score *= -player.getTeam().moveDirection();

		return score;
	}

	protected static int attackScore(Player player, int attackBonus) {
		int score = 0;

		for (Move m : player.getLegalMoves()) {
			if (m.isAttackMove())
				score += attackBonus;
		}

		score *= -player.getTeam().moveDirection();

		return score;
	}

	protected static int kingEscapeScore(Player player, Board board, int kingEscapeBonus) {
		int score = board.getPossibleMoves(player.getKing()).size() * kingEscapeBonus;

		score *= -player.getTeam().moveDirection();

		return score;
	}

	protected static int piecePositionScore(Player player, int positionBonus) {
		int score = 0;

		for (Piece p : player.getActivePieces()) {
			score += p.positionBonus() * positionBonus;
		}

		score *= -player.getTeam().moveDirection();

		return score;
	}

	protected static int passedPawnScore(Board board, Team team, int passedPawnBonus) {
		int score = PawnStructureAnalyzer.INSTANCE.findPassedPawns(board, team).size() * passedPawnBonus;

		score *= -team.moveDirection();

		return score;
	}
}