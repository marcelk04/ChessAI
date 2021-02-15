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
		double time = (double) timeInMs / 1000d;
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
		sb.append("Transpositions:" + evaluator.getTranspositions());
		sb.append("|");
		sb.append("in %:" + transpositionPercentage);

		evaluator.resetTranspositions();

		UIConsole.log(sb.toString());
	}

	private int min(Board board, int depth) {
		if (depth == 0 || board.hasGameEnded()) {
			evaluatedBoards++;

			if (depth == 0 || board.getWinner() == null)
				return evaluator.evaluate(board, depth);
			return 10000 - depth;
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

			if (depth == 0 || board.getWinner() == null)
				return evaluator.evaluate(board, depth);
			return -10000 + depth;
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