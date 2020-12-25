package algorithm;

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
	private long evaluatedBoards;
	
	private static Minimax minimax;

	public Minimax(MoveMaker mm, int depth, boolean usePruning) {
		this.mm = mm;
		this.depth = depth;
		this.usePruning = usePruning;
		this.evaluatedBoards = 0;
		
		minimax = this;

		start();
	}

	@Override
	public void run() {
		final Board board = mm.getBoard();
		final Player currentPlayer = board.getCurrentPlayer();
		Move bestMove = null;
		int bestEval;

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
		UIConsole.log("Evaluated Boards: " + evaluatedBoards + " with search depth " + depth + " | Best Move: "
				+ bestMove.getNotation());
		mm.moveExecuted(mt);
	}

	private int min(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || Evaluator.hasGameEnded(board)) {
			evaluatedBoards++;
			return Evaluator.evaluateBoard(board);
		}

		int minEval = beta;

		for (Move m : board.getCurrentPlayer().getLegalMoves()) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(m);

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = max(mt.getNewBoard(), depth - 1, alpha, minEval);

				minEval = Math.min(minEval, currentEval);

				if (usePruning && minEval <= alpha) {
					break;
				}
			}
		}

		return minEval;
	}

	private int max(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || Evaluator.hasGameEnded(board)) {
			evaluatedBoards++;
			return Evaluator.evaluateBoard(board);
		}

		int maxEval = alpha;

		for (Move m : board.getCurrentPlayer().getLegalMoves()) {
			MoveTransition mt = board.getCurrentPlayer().makeMove(m);

			if (mt.getMoveStatus() == MoveStatus.DONE) {
				int currentEval = min(mt.getNewBoard(), depth - 1, maxEval, beta);

				maxEval = Math.max(maxEval, currentEval);

				if (usePruning && maxEval >= beta) {
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
	}

	public synchronized void stop() {
		if (!running)
			return;

		running = false;
	}
	
	public static void terminate() {
		minimax.stop();
	}
}