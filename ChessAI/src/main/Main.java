package main;

import chess.Board;
import gfx.Assets;
import ui.objects.UIBoardPanel;

public class Main {
	public static void main(String[] args) {
		Assets.init();
		display.Display d = new display.Display("Chess", 800, 800);

		UIBoardPanel boardPanel = new UIBoardPanel();
		boardPanel.setBounds(0, 0, 800, 800);
		d.add(boardPanel);

		Board board = Board.create();
		boardPanel.setBoard(board);
	}
}