package com.chess.ai;

import java.util.List;

import com.chess.Board;
import com.chess.ai.evaluation.BoardEvaluator;
import com.chess.move.Move;
import com.chess.move.MoveStatus;
import com.chess.move.MoveTransition;
import com.chess.pieces.Team;
import com.chess.player.Player;
import com.gui.objects.UIConsole;

public class MinimaxABwMO extends MinimaxAlgorithm {
	private long evaluatedBoards, timesPruned, timeInMs;
	private double movesPerBoard, prunedBoards;
	private int movesPerBoardCount = 1;

	private Move bestMove;
	private int bestEval;

	public MinimaxABwMO(MoveMaker mm, int depth, BoardEvaluator evaluator) {
		super(mm, depth, evaluator);
	}

	@Override
	public Move findBestMove() {
		final Board board = mm.getBoard();
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
		sb.append("Avg time to order moves:" + MoveOrdering.getSimpleOrderTime() + "ms");
		sb.append("|");
		sb.append("Transpositions:" + evaluator.getTranspositions());

		MoveOrdering.reset();
		evaluator.resetTranspositions();

		UIConsole.log(sb.toString());
	}

	private int min(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || board.hasGameEnded()) {
			evaluatedBoards++;

			if (depth == 0 || board.getWinner() == null)
				return evaluator.evaluate(board, depth);
			return 10000 - depth;
		}

		final List<Move> moves = MoveOrdering.calculateSimpleMoveOrder(board.getCurrentPlayer().getLegalMoves());

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

			if (depth == 0 || board.getWinner() == null)
				return evaluator.evaluate(board, depth);
			return -10000 + depth;
		}

		final List<Move> moves = MoveOrdering.calculateSimpleMoveOrder(board.getCurrentPlayer().getLegalMoves());

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