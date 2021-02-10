package com.chess.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.chess.move.MoveStatus;
import com.chess.move.MoveTransition;
import com.chess.pieces.Team;
import com.chess.player.Player;

public class MoveOrdering {
	private static final BoardEvaluator evaluator = new SimpleBoardEvaluator();
	private static final int ORDER_SEARCH_DEPTH = 2;

	private static double simpleOrderTime = 0, simpleOrderCount = 0;
	private static double complexOrderTime = 0, complexOrderCount = 0;

	public static List<Move> orderMoves(Board board) {
		return orderMoves(board, ORDER_SEARCH_DEPTH);
	}

	public static List<Move> orderMoves(Board board, int depth) {
		final long time = System.currentTimeMillis();

		final List<MoveOrderEntry> moveOrderEntries = new ArrayList<MoveOrderEntry>();

		for (Move move : board.getCurrentPlayer().getLegalMoves()) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(move);

			if (mt.getMoveStatus().isDone()) {
				int attackBonus = calculateAttackBonus(board.getCurrentPlayer(), move);
				int currentEval = attackBonus + (board.getCurrentPlayer().getTeam() == Team.WHITE
						? min(mt.getNewBoard(), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE)
						: max(mt.getNewBoard(), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE));
				moveOrderEntries.add(new MoveOrderEntry(move, currentEval));
			}
		}

		if (board.getCurrentPlayer().getTeam() == Team.WHITE) {
			moveOrderEntries.sort((o1, o2) -> {
				return o2.getEval() - o1.getEval();
			});
		} else {
			moveOrderEntries.sort((o1, o2) -> {
				return o1.getEval() - o2.getEval();
			});
		}

		List<Move> orderedMoves = new ArrayList<Move>();
		for (MoveOrderEntry entry : moveOrderEntries) {
			orderedMoves.add(entry.getMove());
		}

		complexOrderTime += System.currentTimeMillis() - time;
		complexOrderCount++;

		return orderedMoves;
	}

	private static int calculateAttackBonus(Player player, Move move) {
		int attackBonus = move.isAttackMove() ? 1000 : 0;
		attackBonus *= -player.getTeam().moveDirection();
		return attackBonus;
	}

	public static List<Move> calculateSimpleMoveOrder(final List<Move> moves) {
		final long time = System.currentTimeMillis();

		final List<Move> orderedMoves = new ArrayList<Move>();
		orderedMoves.addAll(moves);

		orderedMoves.sort((o1, o2) -> {
			return attackScore(o2) - attackScore(o1);
		});

		simpleOrderTime += System.currentTimeMillis() - time;
		simpleOrderCount++;

		return orderedMoves;
	}

	private static int attackScore(Move move) {
		return move.isAttackMove()
				? (move.getAttackedPiece().getValue() - move.getMovedPiece().getValue())
						* -move.getMovedPiece().getTeam().moveDirection()
				: 0;
	}

	private static int min(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || board.hasGameEnded()) {
			return evaluator.evaluate(board, depth);
		}

		final List<Move> moves = calculateSimpleMoveOrder(board.getCurrentPlayer().getLegalMoves());
		int minEval = beta;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = max(mt.getNewBoard(), depth - 1, alpha, minEval);

				minEval = Math.min(minEval, currentEval);

				if (minEval <= beta)
					break;
			}
		}

		return minEval;
	}

	private static int max(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || board.hasGameEnded()) {
			return evaluator.evaluate(board, depth);
		}

		final List<Move> moves = calculateSimpleMoveOrder(board.getCurrentPlayer().getLegalMoves());
		int maxEval = alpha;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = min(mt.getNewBoard(), depth - 1, maxEval, beta);

				maxEval = Math.max(maxEval, currentEval);

				if (maxEval >= beta)
					break;
			}
		}

		return maxEval;
	}

	public static double getSimpleOrderTime() {
		return simpleOrderTime / simpleOrderCount;
	}

	public static double getComplexOrderTime() {
		return complexOrderTime / complexOrderCount;
	}

	public static void reset() {
		simpleOrderTime = 0;
		simpleOrderCount = 0;
		complexOrderTime = 0;
		complexOrderCount = 0;
	}

	// ===== Inner Classes ===== \\
	private static class MoveOrderEntry {
		private Move move;
		private int eval;

		MoveOrderEntry(Move move, int eval) {
			this.move = move;
			this.eval = eval;
		}

		// ===== Getters ===== \\
		Move getMove() {
			return move;
		}

		int getEval() {
			return eval;
		}
	}
}