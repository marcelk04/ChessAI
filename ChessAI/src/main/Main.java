package main;

import java.awt.Color;

import chess.Board;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import display.Display;
import gfx.Assets;
import ui.listeners.ClickListener;
import ui.listeners.MoveExecutionListener;
import ui.objects.UIBoardPanel;
import ui.objects.UISelectionBox;

public class Main {
	public static void main(String[] args) {
		Assets.init();
		Display d = new Display("Chess", 1000, 800);

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

		UISelectionBox<String> box = new UISelectionBox<String>(new String[] { "Option 1", "Option 2", "Option 3" });
		box.setBounds(810, 10, 180, 20);
		box.setClickListener(new ClickListener() {
			@Override
			public void onClick() {
				System.out.println(box.getSelectedElement());
			}
		});
		d.add(box);
	}
}