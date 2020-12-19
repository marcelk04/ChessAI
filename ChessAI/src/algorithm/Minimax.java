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

	public Minimax(MoveMaker mm, int depth, boolean usePruning) {
		this.mm = mm;
		this.depth = depth;
		this.usePruning = usePruning;
		this.evaluatedBoards = 0;
		
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
				int currentEval = minimax(currentPlayer.makeMove(m).getNewBoard(), depth, false);

				if (currentEval > bestEval) {
					bestEval = currentEval;
					bestMove = m;
				}
			}
		} else {
			bestEval = Integer.MAX_VALUE;

			for (Move m : currentPlayer.getLegalMoves()) {
				int currentEval = minimax(currentPlayer.makeMove(m).getNewBoard(), depth, true);

				if (currentEval < bestEval) {
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

	private int minimax(Board board, int depth, boolean isMaximizer) {
		if (depth == 0) {
			evaluatedBoards++;
			return Evaluator.evaluateBoard(board);
		}

		if (Evaluator.hasGameEnded(board)) {
			return Evaluator.evaluateBoard(board);
		}

		if (isMaximizer) {
			int maxEval = Integer.MIN_VALUE;

			for (Move m : board.getCurrentPlayer().getLegalMoves()) {
				MoveTransition mt = board.getCurrentPlayer().makeMove(m);

				if (mt.getMoveStatus() == MoveStatus.DONE) {
					int currentEval = minimax(mt.getNewBoard(), depth - 1, false);

					maxEval = Math.max(maxEval, currentEval);
				}
			}

			return maxEval;
		} else {
			int minEval = Integer.MAX_VALUE;

			for (Move m : board.getCurrentPlayer().getLegalMoves()) {
				MoveTransition mt = board.getCurrentPlayer().makeMove(m);

				if (mt.getMoveStatus() == MoveStatus.DONE) {
					int currentEval = minimax(mt.getNewBoard(), depth - 1, true);

					minEval = Math.min(minEval, currentEval);
				}
			}

			return minEval;
		}
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
}