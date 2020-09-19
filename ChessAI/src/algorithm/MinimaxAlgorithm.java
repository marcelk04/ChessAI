package algorithm;

import chess.board.ChessBoard;
import chess.pieces.Piece;
import chess.pieces.Team;
import tree.ChessEvent;
import tree.Node;
import tree.TreeGUI;

public class MinimaxAlgorithm implements Runnable {
	private Thread thread;
	private boolean running = false;

	private ChessBoard board;
	private int depth;
	private TreeGUI gui;

	public MinimaxAlgorithm(ChessBoard board, int depth, TreeGUI gui) {
		this.board = board;
		this.depth = depth;
		this.gui = gui;

		start();
	}

	@Override
	public void run() {
//		Node<ChessEvent> root = minimaxABTree(board, depth, -1000000000, 1000000000, true);
		Node<ChessEvent> root = minimaxTree(board, depth, true);
		Move bestMove = root.getData().getMove();
		board.makeMove(bestMove);
		gui.addTree(root);
//		root.deleteChildren();

		stop();
	}

	private Node<ChessEvent> minimaxABTree(ChessBoard board, int depth, int alpha, int beta, boolean isMaximizer) {
		if (board.checkWin() || depth == 0) {
			if (board.getWinner() == Team.black) {
				return new Node<ChessEvent>(new ChessEvent(board.clone(), null, 10000 + depth));
			} else if (board.getWinner() == Team.white) {
				return new Node<ChessEvent>(new ChessEvent(board.clone(), null, -10000 - depth));
			} else {
				return new Node<ChessEvent>(new ChessEvent(board.clone(), null, board.getEvaluation()));
			}
		}

		Node<ChessEvent> finalEval = new Node<ChessEvent>(new ChessEvent(board.clone(), null));

		if (isMaximizer) {
			Node<ChessEvent> maxEval = new Node<ChessEvent>(new ChessEvent(-1000000000));

			pieces: for (Piece currentPossiblePiece : board.getPieces(Team.black)) {
				for (Move currentPossibleMove : currentPossiblePiece.getMoves()) {
					ChessBoard boardCopy = board.clone();
					boardCopy.movePiece(currentPossibleMove.getOldX(), currentPossibleMove.getOldY(),
							currentPossibleMove.getNewX(), currentPossibleMove.getNewY());

					Node<ChessEvent> eval = minimaxABTree(boardCopy, depth - 1, alpha, beta, false);
					eval.getData().setMove(currentPossibleMove);
					finalEval.addChild(eval);

					if (eval.getData().getEval() > maxEval.getData().getEval())
						maxEval = eval;

					alpha = Math.max(alpha, eval.getData().getEval());
					if (beta <= alpha)
						break pieces;
				}
			}

			finalEval.getData().setMove(maxEval.getData().getMove());
			finalEval.getData().setEval(maxEval.getData().getEval());
		} else {
			Node<ChessEvent> minEval = new Node<ChessEvent>(new ChessEvent(1000000000));

			pieces: for (Piece currentPossiblePiece : board.getPieces(Team.white)) {
				for (Move currentPossibleMove : currentPossiblePiece.getMoves()) {
					ChessBoard boardCopy = board.clone();
					boardCopy.movePiece(currentPossibleMove.getOldX(), currentPossibleMove.getOldY(),
							currentPossibleMove.getNewX(), currentPossibleMove.getNewY());

					Node<ChessEvent> eval = minimaxABTree(boardCopy, depth - 1, alpha, beta, true);
					eval.getData().setMove(currentPossibleMove);
					finalEval.addChild(eval);

					if (eval.getData().getEval() < minEval.getData().getEval())
						minEval = eval;

					beta = Math.min(eval.getData().getEval(), beta);
					if (beta <= alpha)
						break pieces;
				}
			}

			finalEval.getData().setMove(minEval.getData().getMove());
			finalEval.getData().setEval(minEval.getData().getEval());
		}

		return finalEval;
	}

	private Node<ChessEvent> minimaxTree(ChessBoard board, int depth, boolean isMaximizer) {
		if (board.checkWin() || depth == 0) {
			if (board.getWinner() == Team.black) {
				return new Node<ChessEvent>(new ChessEvent(board.clone(), null, 10000 + depth));
			} else if (board.getWinner() == Team.white) {
				return new Node<ChessEvent>(new ChessEvent(board.clone(), null, -10000 - depth));
			} else {
				return new Node<ChessEvent>(new ChessEvent(board.clone(), null, board.getEvaluation()));
			}
		}

		Node<ChessEvent> finalEval = new Node<ChessEvent>(new ChessEvent(board.clone(), null));

		if (isMaximizer) {
			Node<ChessEvent> maxEval = new Node<ChessEvent>(new ChessEvent(-1000000000));

			for (Piece currentPossiblePiece : board.getPieces(Team.black)) {
				for (Move currentPossibleMove : currentPossiblePiece.getMoves()) {
					ChessBoard boardCopy = board.clone();
					if (!boardCopy.movePiece(currentPossibleMove.getOldX(), currentPossibleMove.getOldY(),
							currentPossibleMove.getNewX(), currentPossibleMove.getNewY())) {
						System.out.println("error");
					}

					Node<ChessEvent> eval = minimaxTree(boardCopy, depth - 1, false);
					eval.getData().setMove(currentPossibleMove);
					finalEval.addChild(eval);

					if (eval.getData().getEval() > maxEval.getData().getEval())
						maxEval = eval;
				}
			}

			finalEval.getData().setMove(maxEval.getData().getMove());
			finalEval.getData().setEval(maxEval.getData().getEval());
		} else {
			Node<ChessEvent> minEval = new Node<ChessEvent>(new ChessEvent(1000000000));

			for (Piece currentPossiblePiece : board.getPieces(Team.white)) {
				for (Move currentPossibleMove : currentPossiblePiece.getMoves()) {
					ChessBoard boardCopy = board.clone();
					if (!boardCopy.movePiece(currentPossibleMove.getOldX(), currentPossibleMove.getOldY(),
							currentPossibleMove.getNewX(), currentPossibleMove.getNewY())) {
						System.out.println("error");
					}

					Node<ChessEvent> eval = minimaxTree(boardCopy, depth - 1, true);
					eval.getData().setMove(currentPossibleMove);
					finalEval.addChild(eval);

					if (eval.getData().getEval() < minEval.getData().getEval())
						minEval = eval;
				}
			}

			finalEval.getData().setMove(minEval.getData().getMove());
			finalEval.getData().setEval(minEval.getData().getEval());
		}

		return finalEval;
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
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}