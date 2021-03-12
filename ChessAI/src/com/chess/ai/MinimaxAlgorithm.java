package com.chess.ai;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.ai.evaluation.BoardEvaluator;
import com.chess.move.Move;
import com.gui.listeners.MoveExecutionListener;
import com.gui.objects.UIConsole;

public abstract class MinimaxAlgorithm implements Runnable {
	private static List<MinimaxAlgorithm> allActiveThreads = new ArrayList<MinimaxAlgorithm>();

	protected Thread thread;
	protected boolean running = false;

	protected final MoveExecutionListener meListener;
	protected final Board board;
	protected final int depth;
	protected final BoardEvaluator evaluator;
	protected final boolean printOutData;

	public MinimaxAlgorithm(MoveExecutionListener meListener, Board board, int depth, BoardEvaluator evaluator,
			boolean printOutData) {
		this.meListener = meListener;
		this.board = board;
		this.depth = depth;
		this.evaluator = evaluator;
		this.printOutData = printOutData;

		start();
	}

	public abstract Move findBestMove();

	public abstract void printOutData();

	@Override
	public void run() {
		Move bestMove = findBestMove();
		if (printOutData && running)
			printOutData();
		if (running)
			meListener.onMoveExecution(board.getCurrentPlayer().makeMove(bestMove));
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
		if (allActiveThreads.isEmpty())
			return;

		UIConsole.log("Stopping " + allActiveThreads.size() + " thread(s)!");
		for (int i = allActiveThreads.size() - 1; i >= 0; i--) {
			allActiveThreads.get(i).stop();
		}
	}

	public static enum AIType {
		MINIMAX(0) {
			@Override
			public MinimaxAlgorithm createNew(MoveExecutionListener meListener, Board board, int depth,
					BoardEvaluator evaluator, boolean printOutData) {
				return new Minimax(meListener, board, depth, evaluator, printOutData);
			}

			@Override
			public String toString() {
				return "Minimax";
			}
		},
		MINIMAX_AB(1) {
			@Override
			public MinimaxAlgorithm createNew(MoveExecutionListener meListener, Board board, int depth,
					BoardEvaluator evaluator, boolean printOutData) {
				return new MinimaxAB(meListener, board, depth, evaluator, printOutData);
			}

			@Override
			public String toString() {
				return "Minimax + AlphaBeta";
			}
		},
		MINIMAX_AB_MO(2) {
			@Override
			public MinimaxAlgorithm createNew(MoveExecutionListener meListener, Board board, int depth,
					BoardEvaluator evaluator, boolean printOutData) {
				return new MinimaxABwMO(meListener, board, depth, evaluator, printOutData);
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

		public abstract MinimaxAlgorithm createNew(MoveExecutionListener meListener, Board board, int depth,
				BoardEvaluator evaluator, boolean printOutData);

		@Override
		public abstract String toString();

		public int getNumber() {
			return number;
		}
	}
}