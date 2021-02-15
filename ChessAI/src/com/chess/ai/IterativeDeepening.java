package com.chess.ai;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.ai.MinimaxAlgorithm.AIType;
import com.chess.ai.evaluation.BoardEvaluator;
import com.chess.move.MoveTransition;
import com.gui.listeners.MoveExecutionListener;
import com.gui.objects.UIConsole;

public class IterativeDeepening implements Runnable, MoveExecutionListener {
	private Thread thread;
	private boolean running = false;

	private static final List<IterativeDeepening> allActiveThreads = new ArrayList<IterativeDeepening>();

	private final AIType aiType;
	private final MoveExecutionListener meListener;
	private final Board board;
	private final BoardEvaluator evaluator;
	private final long maxTime;
	private final int maxDepth;

	private long startTime;
	private int depth;
	private MoveTransition bestMoveTransition;

	public IterativeDeepening(AIType aiType, MoveExecutionListener meListener, Board board, BoardEvaluator evaluator,
			long maxTime, int maxDepth) {
		this.aiType = aiType;
		this.meListener = meListener;
		this.board = board;
		this.evaluator = evaluator;
		this.maxTime = maxTime;
		this.startTime = System.currentTimeMillis();
		this.maxDepth = maxDepth;
		this.depth = 1;

		start();
	}

	@Override
	public void run() {
		aiType.createNew(this, board, depth, evaluator, false);

		while (System.currentTimeMillis() - startTime <= maxTime && depth <= maxDepth) {
		}

		stopAll();

		StringBuilder sb = new StringBuilder();
		sb.append("Best move:" + bestMoveTransition.getExecutedMove().getNotation());
		sb.append("|");
		sb.append("Search depth:" + depth);

		UIConsole.log(sb.toString());

		meListener.onMoveExecution(bestMoveTransition);
	}

	@Override
	public void onMoveExecution(MoveTransition e) {
		bestMoveTransition = e;
		depth++;
		if (running && System.currentTimeMillis() - startTime <= maxTime && depth <= maxDepth)
			aiType.createNew(this, board, depth + 1, evaluator, false);

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
}