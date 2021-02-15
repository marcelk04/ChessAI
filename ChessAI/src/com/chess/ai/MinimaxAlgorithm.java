package com.chess.ai;

import java.util.ArrayList;
import java.util.List;

import com.chess.ai.evaluation.BoardEvaluator;
import com.chess.move.Move;
import com.gui.objects.UIConsole;

public abstract class MinimaxAlgorithm implements Runnable {
	private static List<MinimaxAlgorithm> allActiveThreads = new ArrayList<MinimaxAlgorithm>();

	protected Thread thread;
	protected boolean running = false;

	protected final MoveMaker mm;
	protected final int depth;
	protected final BoardEvaluator evaluator;

	public MinimaxAlgorithm(MoveMaker mm, int depth, BoardEvaluator evaluator) {
		this.mm = mm;
		this.depth = depth;
		this.evaluator = evaluator;

		start();
	}

	public abstract Move findBestMove();

	public abstract void printOutData();

	@Override
	public void run() {
		Move bestMove = findBestMove();
		printOutData();
		if (running)
			mm.moveExecuted(mm.getBoard().getCurrentPlayer().makeMove(bestMove));
		stop();
	}

	public synchronized void start() {
		if (running)
			return;

		thread = new Thread(this);
		thread.start();
		running = true;
		allActiveThreads.add(this);
	}

	public synchronized void stop() {
		if (!running)
			return;

		running = false;
		allActiveThreads.remove(this);
	}

	public static void stopAll() {
		if (allActiveThreads.size() == 0)
			return;

		UIConsole.log("Stopping " + allActiveThreads.size() + " thread(s)!");
		for (int i = allActiveThreads.size() - 1; i >= 0; i--) {
			allActiveThreads.get(i).stop();
		}
	}

	public static enum AIType {
		MINIMAX(0) {
			@Override
			public MinimaxAlgorithm createNew(MoveMaker mm, int depth, BoardEvaluator evaluator) {
				return new Minimax(mm, depth, evaluator);
			}

			@Override
			public String toString() {
				return "Minimax";
			}
		},
		MINIMAX_AB(1) {
			@Override
			public MinimaxAlgorithm createNew(MoveMaker mm, int depth, BoardEvaluator evaluator) {
				return new MinimaxAB(mm, depth, evaluator);
			}

			@Override
			public String toString() {
				return "Minimax + AlphaBeta";
			}
		},
		MINIMAX_AB_MO(2) {
			@Override
			public MinimaxAlgorithm createNew(MoveMaker mm, int depth, BoardEvaluator evaluator) {
				return new MinimaxABwMO(mm, depth, evaluator);
			}

			@Override
			public String toString() {
				return "Minimax + AlphaBeta + MoveOrdering";
			}
		};

		final int number;

		AIType(final int number) {
			this.number = number;
		}

		public abstract MinimaxAlgorithm createNew(MoveMaker mm, int depth, BoardEvaluator evaluator);
		
		@Override
		public abstract String toString();

		public int getNumber() {
			return number;
		}
	}
}