package algorithm;

import static algorithm.Evaluator.*;

import java.util.ArrayList;
import java.util.List;

import chess.Board;
import chess.move.Move;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import chess.pieces.Team;
import chess.player.Player;
import ui.objects.UIConsole;

public class Minimax implements Runnable {
	private Thread thread;
	private boolean running;

	private MoveMaker mm;
	private int depth;
	private boolean usePruning;
	private long evaluatedBoards, timesPruned;
	private double movesPerBoard, prunedBoards;
	private int count;

	private static List<Minimax> list = new ArrayList<Minimax>();

	public Minimax(MoveMaker mm, int depth, boolean usePruning) {
		this.mm = mm;
		this.depth = depth;
		this.usePruning = usePruning;
		this.evaluatedBoards = 0;
		this.timesPruned = 0;
		this.movesPerBoard = mm.getBoard().getCurrentPlayer().getLegalMoves().size();
		this.prunedBoards = 0;
		this.count = 1;
		start();
	}

	@Override
	public void run() {
		final Board board = mm.getBoard();
		final Player currentPlayer = board.getCurrentPlayer();
		Move bestMove = null;
		int bestEval;
		double time = System.currentTimeMillis();

		if (currentPlayer.getTeam() == Team.WHITE) {
			bestEval = Integer.MIN_VALUE;

			for (Move m : currentPlayer.getLegalMoves()) {
				MoveTransition moveTransition = currentPlayer.makeMove(m);
				int currentEval = min(moveTransition.getNewBoard(), depth, bestEval, Integer.MAX_VALUE);

				if (currentEval > bestEval && moveTransition.getMoveStatus() == MoveStatus.DONE) {
					bestEval = currentEval;
					bestMove = m;
				}
			}
		} else {
			bestEval = Integer.MAX_VALUE;

			for (Move m : currentPlayer.getLegalMoves()) {
				MoveTransition moveTransition = currentPlayer.makeMove(m);
				int currentEval = max(moveTransition.getNewBoard(), depth, Integer.MIN_VALUE, bestEval);

				if (currentEval < bestEval && moveTransition.getMoveStatus() == MoveStatus.DONE) {
					bestEval = currentEval;
					bestMove = m;
				}
			}
		}

		MoveTransition mt = currentPlayer.makeMove(bestMove);

		time = (System.currentTimeMillis() - time) / 1000d;
		UIConsole.log("Evaluated Boards:" + evaluatedBoards + "|Depth:" + depth + "|Best Move:" + bestMove.getNotation()
				+ "|Best Eval:" + bestEval + "|Time: " + time + "s|Times pruned:" + timesPruned
				+ "|Approx pruned boards:" + Math.round(prunedBoards) + "|in %:"
				+ Math.round(prunedBoards / (evaluatedBoards + prunedBoards) * 10000) / 100d);

		if (running)
			mm.moveExecuted(mt);
		stop();
	}

	private int min(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || hasGameEnded(board)) {
			evaluatedBoards++;
			return evaluateBoard(board);
		}

		final List<Move> moves = board.getCurrentPlayer().getLegalMoves();
		movesPerBoard = (movesPerBoard * count + moves.size()) / ++count;
		int minEval = beta;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = max(mt.getNewBoard(), depth - 1, alpha, minEval);

				minEval = Math.min(minEval, currentEval);

				if (usePruning && minEval <= alpha) {
					timesPruned++;
					prunedBoards += Math.pow(movesPerBoard, depth - 1) * (moves.size() - i - 1);
					break;
				}
			}
		}

		return minEval;
	}

	private int max(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || hasGameEnded(board)) {
			evaluatedBoards++;
			return evaluateBoard(board);
		}

		final List<Move> moves = board.getCurrentPlayer().getLegalMoves();
		movesPerBoard = (movesPerBoard * count + moves.size()) / ++count;
		int maxEval = alpha;

		for (int i = 0; i < moves.size(); i++) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(moves.get(i));

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = min(mt.getNewBoard(), depth - 1, maxEval, beta);

				maxEval = Math.max(maxEval, currentEval);

				if (usePruning && maxEval >= beta) {
					timesPruned++;
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
		UIConsole.log("Stopping " + list.size() + " thread(s)!");
		for (int i = list.size() - 1; i >= 0; i--) {
			list.get(i).stop();
		}
	}
}