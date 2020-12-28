package algorithm;

import chess.Board;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import chess.pieces.Team;
import ui.listeners.MoveExecutionListener;
import ui.objects.UIBoardPanel;
import ui.objects.UIConsole;

public class MoveMaker {
	private PlayerType player1, player2;
	private UIBoardPanel boardPanel;
	private Board board;
	private Team currentTeam;
	private MoveExecutionListener moveExecutionListener;
	private static boolean stopped = false;

	public MoveMaker(PlayerType player1, PlayerType player2, UIBoardPanel boardPanel) {
		this.player1 = player1;
		this.player2 = player2;
		this.boardPanel = boardPanel;
		this.board = Board.create();
		this.currentTeam = board.getCurrentPlayer().getTeam();
		makeNextMove();
	}

	public void moveExecuted(MoveTransition m) {
		if (moveExecutionListener != null) {
			moveExecutionListener.onMoveExecution(m);
		}

		if (m.getMoveStatus() == MoveStatus.DONE) {
			board = m.getNewBoard();
			currentTeam = board.getCurrentPlayer().getTeam();
		} else {
			UIConsole.log("Couldn't execute move|" + m);
			throw new RuntimeException();
		}
		makeNextMove();
	}

	public void makeNextMove() {
		if (!stopped) {
			if (Evaluator.hasGameEnded(board)) {
				UIConsole.log(board.getCurrentPlayer().getOpponent().getTeam() + " has won!");
				boardPanel.setMoveMaker(null);
			} else {
				if ((currentTeam == Team.WHITE && player1 == PlayerType.HUMAN)
						|| (currentTeam == Team.BLACK && player2 == PlayerType.HUMAN)) {
					boardPanel.setMoveMaker(this);
				} else {
					boardPanel.setMoveMaker(null);
					new Minimax(this, 3, true);
				}
			}
		}
	}

	public void stop() {
		Minimax.stopAll();
		stopped = true;
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
	public void setPlayers(PlayerType player1, PlayerType player2) {
		this.player1 = player1;
		this.player2 = player2;
		stopped = false;
		makeNextMove();
	}

	public void setBoardPanel(UIBoardPanel boardPanel) {
		this.boardPanel = boardPanel;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setMoveExecutionListener(MoveExecutionListener moveExecutionListener) {
		this.moveExecutionListener = moveExecutionListener;
	}
}