package com.chess.ai.evaluation;

import com.chess.Board;
import com.chess.ai.hashing.TranspositionTable;
import com.chess.move.Move;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Team;
import com.chess.player.Player;

public abstract class BoardEvaluator {
	private int transpositions = 0;

	public abstract int evaluateWithoutHashing(Board board, int depth);

	@Override
	public abstract String toString();

	public int evaluate(Board board, int depth) {
		long zobristHash = board.getZobristHash();
		Integer eval = TranspositionTable.get(zobristHash);
		if (eval != null) {
			transpositions++;
			return eval;
		}

		eval = evaluateWithoutHashing(board, depth);
		TranspositionTable.put(zobristHash, eval);
		return eval;
	}

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

	protected static int winScore(Board board, int depth, int winBonus) {
		if (board.getWinner() == null)
			return 0;

		return (winBonus - depth) * -board.getWinner().moveDirection();
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
//		int score = PawnStructureAnalyzer.INSTANCE.findPassedPawns(board, team).size() * passedPawnBonus;
//
//		score *= -team.moveDirection();
//
//		return score;

		return 0;
	}

	protected static int doubledPawnScore(Pawn[][] pawns, Team team, int doubledPawnPenalty) {
		int score = PawnStructureAnalyzer.INSTANCE.doubledPawns(pawns, team) * doubledPawnPenalty;

		score *= -team.moveDirection();

		return score;
	}

	protected static int isolatedPawnScore(Pawn[][] pawns, Team team, int isolatedPawnPenalty) {
		int score = PawnStructureAnalyzer.INSTANCE.isolatedPawns(pawns, team) * isolatedPawnPenalty;

		score *= -team.moveDirection();

		return score;
	}

	public void resetTranspositions() {
		transpositions = 0;
	}

	// ===== Getters ===== \\
	public int getTranspositions() {
		return transpositions;
	}
}