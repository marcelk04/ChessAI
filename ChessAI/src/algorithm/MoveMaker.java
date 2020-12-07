package algorithm;

import chess.Board;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import chess.pieces.Team;
import ui.listeners.MoveExecutionListener;
import ui.objects.UIBoardPanel;

public class MoveMaker {
	private PlayerType player1, player2;
	private UIBoardPanel boardPanel;
	private Board board;
	private Team currentTeam;
	private MoveExecutionListener moveExecutionListener;;

	public MoveMaker(PlayerType player1, PlayerType player2, UIBoardPanel boardPanel) {
		this.player1 = player1;
		this.player2 = player2;
		this.boardPanel = boardPanel;
		board = Board.create();
		currentTeam = board.getCurrentPlayer().getTeam();
		makeNextMove();
	}

	public void moveExecuted(MoveTransition m) {
		if (moveExecutionListener != null) {
			moveExecutionListener.onMoveExecution(m);
		}

		if (m.getMoveStatus() == MoveStatus.DONE) {
			board = m.getNewBoard();
			currentTeam = board.getCurrentPlayer().getTeam();
		}
		makeNextMove();
	}

	public void makeNextMove() {
		if ((currentTeam == Team.WHITE && player1 == PlayerType.HUMAN)
				|| (currentTeam == Team.BLACK && player2 == PlayerType.HUMAN)) {
			boardPanel.setMoveMaker(this);
		} else {
			boardPanel.setMoveMaker(null);
			new Minimax(this, 3, false);
		}
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

	// ===== Setters ===== \\
	public void setPlayer1(PlayerType player1) {
		this.player1 = player1;
	}

	public void setPlayer2(PlayerType player2) {
		this.player2 = player2;
	}

	public void setBoardPanel(UIBoardPanel boardPanel) {
		this.boardPanel = boardPanel;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setCurrentTeam(Team currentTeam) {
		this.currentTeam = currentTeam;
	}

	public void setMoveExecutionListener(MoveExecutionListener moveExecutionListener) {
		this.moveExecutionListener = moveExecutionListener;
	}
}