package com.chess.ai;

import com.chess.Board;
import com.chess.ai.MinimaxAlgorithm.AIType;
import com.chess.ai.evaluation.BoardEvaluator;
import com.chess.ai.evaluation.SimpleBoardEvaluator;
import com.chess.move.MoveStatus;
import com.chess.move.MoveTransition;
import com.chess.pieces.Team;
import com.gui.listeners.MoveExecutionListener;
import com.gui.objects.UIBoardPanel;
import com.gui.objects.UIConsole;

public class MoveMaker implements MoveExecutionListener {
	private PlayerType player1, player2;
	private UIBoardPanel boardPanel;
	private Board board;
	private Team currentTeam;
	private MoveExecutionListener moveExecutionListener;

	private int depth = 3;
	private AIType aiType = AIType.MINIMAX;
	private BoardEvaluator evaluator = SimpleBoardEvaluator.INSTANCE;

	public MoveMaker(PlayerType player1, PlayerType player2, UIBoardPanel boardPanel) {
		this.player1 = player1;
		this.player2 = player2;
		this.boardPanel = boardPanel;
		this.board = Board.create();
		this.currentTeam = board.getCurrentPlayer().getTeam();
		makeNextMove();
	}

	@Override
	public void onMoveExecution(MoveTransition m) {
		if (moveExecutionListener != null)
			moveExecutionListener.onMoveExecution(m);

		if (m.getMoveStatus() == MoveStatus.DONE) {
			board = m.getNewBoard();
			currentTeam = board.getCurrentPlayer().getTeam();
			boardPanel.setLastMove(m.getExecutedMove());
		} else {
			UIConsole.log("Couldn't execute move|" + m);
			throw new RuntimeException();
		}
		makeNextMove();
	}

	public void makeNextMove() {
		if (board.hasGameEnded()) {
			Team winner = board.getWinner();
			if (winner != null)
				UIConsole.log(winner + " has won!");
			else
				UIConsole.log("The game is a draw!");
			boardPanel.setMoveMaker(null);
		} else {
			if ((currentTeam == Team.WHITE && player1 == PlayerType.HUMAN)
					|| (currentTeam == Team.BLACK && player2 == PlayerType.HUMAN)) {
				boardPanel.setMoveMaker(this);
			} else {
				boardPanel.setMoveMaker(null);
				aiType.createNew(this, board, depth, evaluator, true);
//				new IterativeDeepening(aiType, this, board, evaluator, 10000, 10);
			}
		}

	}

	public void reset() {
		MinimaxAlgorithm.stopAll();
	}

	// ===== Getters ===== \\
	public PlayerType getPlayer1() {
		return player1;
	}

	public PlayerType getPlayer2() {
		return player2;
	}

	public UIBoardPanel getBoardPanel() {
		return boardPanel;
	}

	public Board getBoard() {
		return board;
	}

	public Team getCurrentTeam() {
		return currentTeam;
	}

	public MoveExecutionListener getMoveExecutionListener() {
		return moveExecutionListener;
	}

	public int getDepth() {
		return depth;
	}

	public AIType getAIType() {
		return aiType;
	}

	public BoardEvaluator getEvaluator() {
		return evaluator;
	}

	// ===== Setters ===== \\
	public void setPlayers(PlayerType player1, PlayerType player2) {
		this.player1 = player1;
		this.player2 = player2;
		makeNextMove();
	}

	public void setBoardPanel(UIBoardPanel boardPanel) {
		this.boardPanel = boardPanel;
	}

	public void setBoard(Board board) {
		this.board = board;
		this.currentTeam = board.getCurrentPlayer().getTeam();
	}

	public void setMoveExecutionListener(MoveExecutionListener moveExecutionListener) {
		this.moveExecutionListener = moveExecutionListener;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setAIType(AIType aiType) {
		this.aiType = aiType;
	}

	public void setEvaluator(BoardEvaluator evaluator) {
		this.evaluator = evaluator;
	}
}