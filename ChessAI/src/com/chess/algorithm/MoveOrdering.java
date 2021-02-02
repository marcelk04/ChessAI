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

	private static double orderTime = 0, orderCount = 0;

	public static List<Move> orderMoves(Board board) {
		return orderMoves(board, ORDER_SEARCH_DEPTH);
	}

	public static List<Move> orderMoves(Board board, int depth) {
		List<MoveOrderEntry> moveOrderEntries = new ArrayList<MoveOrderEntry>();

		for (Move move : board.getCurrentPlayer().getLegalMoves()) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(move);

			if (mt.getMoveStatus().isDone()) {
				int attackBonus = calculateAttackBonus(board.getCurrentPlayer(), move);
				int currentEval = attackBonus
						+ (board.getCurrentPlayer().getTeam() == Team.WHITE ? min(mt.getNewBoard(), depth - 1)
								: max(mt.getNewBoard(), depth - 1));
				moveOrderEntries.add(new MoveOrderEntry(move, currentEval));
			}
		}

		moveOrderEntries.sort((o1, o2) -> {
			if (o1.getEval() <= o2.getEval())
				return 1;
			return -1;
		});

		List<Move> orderedMoves = new ArrayList<Move>();
		for (MoveOrderEntry entry : moveOrderEntries) {
			orderedMoves.add(entry.getMove());
		}

		return orderedMoves;
	}

	private static int calculateAttackBonus(Player player, Move move) {
		int attackBonus = move.isAttackMove() ? 1000 : 0;
		attackBonus *= -player.getTeam().moveDirection();
		return attackBonus;
	}

	public static List<Move> calculateSimpleMoveOrder(List<Move> moves) {
		long time = System.currentTimeMillis();

		List<Move> orderedMoves = new ArrayList<Move>();
		orderedMoves.addAll(moves);

		orderedMoves.sort((o1, o2) -> {
			return attackScore(o2) - attackScore(o1);
		});

		orderTime += System.currentTimeMillis() - time;
		orderCount++;

		return orderedMoves;
	}

	private static int attackScore(Move move) {
		return move.isAttackMove()
				? (move.getAttackedPiece().getValue() - move.getMovedPiece().getValue())
						* -move.getMovedPiece().getTeam().moveDirection()
				: 0;
	}

	private static int min(Board board, int depth) {
		if (depth == 0 || board.hasGameEnded()) {
			return evaluator.evaluate(board, depth);
		}

		final List<Move> moves = calculateSimpleMoveOrder(board.getCurrentPlayer().getLegalMoves());
		int minEval = Integer.MAX_VALUE;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = max(mt.getNewBoard(), depth - 1);

				minEval = Math.min(minEval, currentEval);
			}
		}

		return minEval;
	}

	private static int max(Board board, int depth) {
		if (depth == 0 || board.hasGameEnded()) {
			return evaluator.evaluate(board, depth);
		}

		final List<Move> moves = calculateSimpleMoveOrder(board.getCurrentPlayer().getLegalMoves());
		int maxEval = Integer.MIN_VALUE;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = min(mt.getNewBoard(), depth - 1);

				maxEval = Math.max(maxEval, currentEval);
			}
		}

		return maxEval;
	}

	public static double getOrderTime() {
		return orderTime / orderCount;
	}

	public static void reset() {
		orderTime = 0;
		orderCount = 0;
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