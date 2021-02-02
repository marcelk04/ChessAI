package com.chess.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.move.Move;
import com.chess.move.MoveStatus;
import com.chess.move.MoveTransition;
import com.chess.pieces.Team;
import com.chess.player.Player;
import com.gui.objects.UIConsole;

public class Minimax implements Runnable {
	private Thread thread;
	private boolean running;

	private final MoveMaker mm;
	private final int depth;
	private final boolean usePruning, orderMoves;
	private final BoardEvaluator evaluator;

	private long evaluatedBoards, timesPruned;
	private double movesPerBoard, prunedBoards;
	private int movesPerBoardCount;

	private static List<Minimax> list = new ArrayList<Minimax>();

	public Minimax(MoveMaker mm, int depth, boolean usePruning, boolean orderMoves) {
		this.mm = mm;
		this.depth = depth;
		this.usePruning = usePruning;
		this.orderMoves = orderMoves;
		this.evaluatedBoards = 0;
		this.timesPruned = 0;
		this.movesPerBoard = mm.getBoard().getCurrentPlayer().getLegalMoves().size();
		this.prunedBoards = 0;
		this.movesPerBoardCount = 1;
		evaluator = new PositionBoardEvaluator();
		start();
	}

	@Override
	public void run() {
		final Board board = mm.getBoard();
		final Player currentPlayer = board.getCurrentPlayer();
		Move bestMove = null;
		int bestEval;
		double time = System.currentTimeMillis();

		List<Move> moves = currentPlayer.getLegalMoves();
		if (orderMoves)
			moves = MoveOrdering.calculateSimpleMoveOrder(moves);

		if (currentPlayer.getTeam() == Team.WHITE) {
			bestEval = Integer.MIN_VALUE;

			for (Move m : moves) {
				MoveTransition moveTransition = currentPlayer.makeMove(m);
				int currentEval = min(moveTransition.getNewBoard(), depth, bestEval, Integer.MAX_VALUE);

				if (currentEval > bestEval && moveTransition.getMoveStatus() == MoveStatus.DONE) {
					bestEval = currentEval;
					bestMove = m;
				}
			}
		} else {
			bestEval = Integer.MAX_VALUE;

			for (Move m : moves) {
				MoveTransition moveTransition = currentPlayer.makeMove(m);
				int currentEval = max(moveTransition.getNewBoard(), depth, Integer.MIN_VALUE, bestEval);

				if (currentEval < bestEval && moveTransition.getMoveStatus() == MoveStatus.DONE) {
					bestEval = currentEval;
					bestMove = m;
				}
			}
		}

		MoveTransition mt = currentPlayer.makeMove(bestMove);

		if (running) {
			time = (System.currentTimeMillis() - time) / 1000d;
			UIConsole.log("Evaluated Boards:" + evaluatedBoards + "|Depth:" + depth + "|Best Move:"
					+ bestMove.getNotation() + "|Best Eval:" + bestEval + "|Time: " + time + "s|Times pruned:"
					+ timesPruned + "|Approx pruned boards:" + Math.round(prunedBoards) + "|in %:"
					+ Math.round(prunedBoards / (evaluatedBoards + prunedBoards) * 10000) / 100d);

			if (orderMoves)
				UIConsole.log("Avg. time to order moves: " + MoveOrdering.getOrderTime() + "ms");
			MoveOrdering.reset();
		}

		if (running)
			mm.moveExecuted(mt);
		stop();
	}

	private int min(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || board.hasGameEnded()) {
			evaluatedBoards++;
			return evaluator.evaluate(board, depth);
		}

		List<Move> moves = board.getCurrentPlayer().getLegalMoves();
		if (orderMoves)
			moves = MoveOrdering.calculateSimpleMoveOrder(moves);

		movesPerBoard = (movesPerBoard * movesPerBoardCount + moves.size()) / ++movesPerBoardCount;
		int minEval = beta;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = max(mt.getNewBoard(), depth - 1, alpha, minEval);

				minEval = Math.min(minEval, currentEval);

				if (usePruning && minEval <= alpha) {
					timesPruned++;
					if (depth > 1)
						prunedBoards += Math.pow(movesPerBoard, depth - 1) * (moves.size() - i - 1);
					break;
				}
			}
		}

		return minEval;
	}

	private int max(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || board.hasGameEnded()) {
			evaluatedBoards++;
			return evaluator.evaluate(board, depth);
		}

		List<Move> moves = board.getCurrentPlayer().getLegalMoves();
		if (orderMoves)
			moves = MoveOrdering.calculateSimpleMoveOrder(moves);

		movesPerBoard = (movesPerBoard * movesPerBoardCount + moves.size()) / ++movesPerBoardCount;
		int maxEval = alpha;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = min(mt.getNewBoard(), depth - 1, maxEval, beta);

				maxEval = Math.max(maxEval, currentEval);

				if (usePruning && maxEval >= beta) {
					timesPruned++;
					if (depth > 1)
						prunedBoards += Math.pow(movesPerBoard, depth - 1) * (moves.size() - i - 1);
					break;
				}
			}
		}

		return maxEval;
	}

	public synchronized void start() {
		if (running)
			return;

		thread = new Thread(this);
		thread.start();
		running = true;
		list.add(this);
	}

	public synchronized void stop() {
		if (!running)
			return;

		running = false;
		list.remove(this);
	}

	public static void stopAll() {
		if (list.size() > 0) {
			UIConsole.log("Stopping " + list.size() + " thread(s)!");
			for (int i = list.size() - 1; i >= 0; i--) {
				list.get(i).stop();
			}
		}
	}
}