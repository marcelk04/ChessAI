package main;

import chess.Board;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import display.Display;
import gfx.Assets;
import ui.listeners.MoveExecutionListener;
import ui.objects.UIBoardPanel;

public class Main {
	public static void main(String[] args) {
		Assets.init();
		Display d = new Display("Chess", 800, 800);

		UIBoardPanel boardPanel = new UIBoardPanel();
		boardPanel.setBounds(0, 0, 800, 800);
		boardPanel.setMoveExecutionListener(new MoveExecutionListener() {
			@Override
			public void moveExecuted(MoveTransition e) {
				if (e.getMoveStatus() == MoveStatus.DONE)
					boardPanel.setBoard(e.getNewBoard());
			}
		});
		d.add(boardPanel);

		Board board = Board.create();
		boardPanel.setBoard(board);
	}
}