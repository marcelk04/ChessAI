package com.chess.ai.evaluation;

import com.chess.Board;
import com.chess.pieces.Pawn;
import com.chess.player.Player;

public class PawnPositionBoardEvaluator extends PositionBoardEvaluator {
	public static final PawnPositionBoardEvaluator INSTANCE = new PawnPositionBoardEvaluator();

	@SuppressWarnings("unused")
	private static final int PASSED_PAWN_BONUS = 10;
	private static final int DOUBLED_PAWN_PENALTY = -50;
	private static final int ISOLATED_PAWN_PENALTY = -50;

	@Override
	public int evaluateWithoutHashing(Board board, int depth) {
		Pawn[][] pawns = PawnStructureAnalyzer.INSTANCE.findPawns(board, 16);
		return score(board.getWhitePlayer(), board, depth, pawns) + score(board.getBlackPlayer(), board, depth, pawns)
				+ winScore(board, depth, WIN_BONUS);
	}

	protected static int score(Player player, Board board, int depth, Pawn[][] pawns) {
		return PositionBoardEvaluator.score(player, board, depth)
				+ doubledPawnScore(pawns, player.getTeam(), DOUBLED_PAWN_PENALTY)
				+ isolatedPawnScore(pawns, player.getTeam(), ISOLATED_PAWN_PENALTY);
	}

	@Override
	public String toString() {
		return "Pawn + Position Evaluator";
	}
}