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

public class MinimaxAB extends MinimaxAlgorithm {
	private long evaluatedBoards, timesPruned, timeInMs;
	private double movesPerBoard, prunedBoards;
	private int movesPerBoardCount = 1;

	private Move bestMove;
	private int bestEval;

	public MinimaxAB(MoveExecutionListener meListener, Board board, int depth, BoardEvaluator evaluator,
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

		timeInMs = System.currentTimeMillis() - time;

		return bestMove;
	}

	@Override
	public void printOutData() {
		double time = (double) timeInMs / 1000d;
		double approxPrunedBoards = Math.round(this.prunedBoards);
		double percentageOfPrunedBoards = Math.round(this.prunedBoards / (evaluatedBoards + this.prunedBoards) * 10000)
				/ 100d;
		double transpositionPercentage = Math
				.round((double) evaluator.getTranspositions() / (double) evaluatedBoards * 10000) / 100d;

		StringBuilder sb = new StringBuilder();
		sb.append("Evaluated Boards:" + evaluatedBoards);
		sb.append("|");
		sb.append("Depth:" + depth);
		sb.append("|");
		sb.append("Best Move:" + bestMove.getNotation());
		sb.append("|");
		sb.append("Best Eval:" + bestEval);
		sb.append("|");
		sb.append("Time:" + time + "s");
		sb.append("|");
		sb.append("Times pruned:" + timesPruned);
		sb.append("|");
		sb.append("Approx pruned boards:" + approxPrunedBoards);
		sb.append("|");
		sb.append("in %:" + percentageOfPrunedBoards);
		sb.append("|");
		sb.append("Transpositions:" + evaluator.getTranspositions());
		sb.append("|");
		sb.append("in %:" + transpositionPercentage);

		DataManager.searchTimes.add((float) time);
		DataManager.searchedBoards.add((float) evaluatedBoards);
		DataManager.prunedBoardsPercent.add((float) percentageOfPrunedBoards);
		DataManager.prunedBoards.add((float) approxPrunedBoards);
		DataManager.timesPruned.add((float) timesPruned);
		DataManager.transpositions.add((float) evaluator.getTranspositions());
		DataManager.transpositionsPercent.add((float) transpositionPercentage);

		evaluator.resetTranspositions();

		UIConsole.log(sb.toString());
	}

	private int min(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || board.hasGameEnded()) {
			evaluatedBoards++;
			return evaluator.evaluate(board, depth);
		}

		final List<Move> moves = board.getCurrentPlayer().getLegalMoves();

		movesPerBoard = (movesPerBoard * movesPerBoardCount + moves.size()) / ++movesPerBoardCount;
		int minEval = beta;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = max(mt.getNewBoard(), depth - 1, alpha, minEval);

				minEval = Math.min(minEval, currentEval);

				if (minEval <= alpha) {
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

		final List<Move> moves = board.getCurrentPlayer().getLegalMoves();

		movesPerBoard = (movesPerBoard * movesPerBoardCount + moves.size()) / ++movesPerBoardCount;
		int maxEval = alpha;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = min(mt.getNewBoard(), depth - 1, maxEval, beta);

				maxEval = Math.max(maxEval, currentEval);

				if (maxEval >= beta) {
					timesPruned++;
					if (depth > 1)
						prunedBoards += Math.pow(movesPerBoard, depth - 1) * (moves.size() - i - 1);
					break;
				}
			}
		}

		return maxEval;
	}
}