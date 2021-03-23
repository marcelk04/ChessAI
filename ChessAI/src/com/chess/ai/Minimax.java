package com.chess.ai;

import java.util.List;

import com.chess.Board;
import com.chess.ai.evaluation.BoardEvaluator;
import com.chess.move.Move;
import com.chess.move.MoveStatus;
import com.chess.move.MoveTransition;
import com.chess.pieces.Team;
import com.chess.player.Player;
import com.gui.listeners.MoveExecutionListener;
import com.gui.objects.UIConsole;
import com.main.DataManager;
import com.main.Utils;

public class Minimax extends MinimaxAlgorithm {
	private long evaluatedBoards, timeInMs;

	private Move bestMove;
	private int bestEval;

	public Minimax(MoveExecutionListener meListener, Board board, int depth, BoardEvaluator evaluator,
			boolean printOutData) {
		super(meListener, board, depth, evaluator, printOutData);
	}

	@Override
	public Move findBestMove() {
		final Player currentPlayer = board.getCurrentPlayer();
		final List<Move> moves = currentPlayer.getLegalMoves();

		long time = System.currentTimeMillis();

		if (currentPlayer.getTeam() == Team.WHITE) {
			bestEval = Integer.MIN_VALUE;

			for (Move m : moves) {
				MoveTransition moveTransition = currentPlayer.makeMove(m);
				int currentEval = min(moveTransition.getNewBoard(), depth);

				if (currentEval > bestEval && moveTransition.getMoveStatus() == MoveStatus.DONE) {
					bestEval = currentEval;
					bestMove = m;
				}
			}
		} else {
			bestEval = Integer.MAX_VALUE;

			for (Move m : moves) {
				MoveTransition moveTransition = currentPlayer.makeMove(m);
				int currentEval = max(moveTransition.getNewBoard(), depth);

				if (currentEval < bestEval && moveTransition.getMoveStatus() == MoveStatus.DONE) {
					bestEval = currentEval;
					bestMove = m;
				}
			}
		}

		timeInMs = System.currentTimeMillis() - time;

		return bestMove;
	}

	@Override
	public void printOutData() {
		double time = Utils.round(timeInMs / 1000d, 4);
		double transpositionPercentage = Utils.round((double) evaluator.getTranspositions() / (double) evaluatedBoards,
				4) * 100d;

		StringBuilder sb = new StringBuilder();
		sb.append("Evaluated Boards:" + evaluatedBoards).append("|");
		sb.append("Depth:" + depth).append("|");
		sb.append("Best Move:" + bestMove.getNotation()).append("|");
		sb.append("Best Eval:" + bestEval).append("|");
		sb.append("Time:" + time + "s").append("|");
		sb.append("Transpositions:" + evaluator.getTranspositions()).append("|");
		sb.append("in %:" + transpositionPercentage);

		DataManager.searchTimes.add((float) time);
		DataManager.searchedBoards.add((float) evaluatedBoards);
		DataManager.transpositions.add((float) evaluator.getTranspositions());
		DataManager.transpositionsPercent.add((float) transpositionPercentage);

		evaluator.resetTranspositions();

		if (running)
			UIConsole.log(sb.toString());
	}

	private int min(Board board, int depth) {
		if (depth == 0 || board.hasGameEnded()) {
			evaluatedBoards++;
			return evaluator.evaluate(board, depth);
		}

		List<Move> moves = board.getCurrentPlayer().getLegalMoves();

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

	private int max(Board board, int depth) {
		if (depth == 0 || board.hasGameEnded()) {
			evaluatedBoards++;
			return evaluator.evaluate(board, depth);
		}

		List<Move> moves = board.getCurrentPlayer().getLegalMoves();

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
}