package com.main;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JColorChooser;

import com.chess.Board;
import com.chess.algorithm.Minimax;
import com.chess.algorithm.MoveMaker;
import com.chess.algorithm.PlayerType;
import com.chess.move.MoveStatus;
import com.chess.move.MoveTransition;
import com.gui.display.Display;
import com.gui.objects.UIBoardPanel;
import com.gui.objects.UIConsole;
import com.gui.objects.UILabel;
import com.gui.objects.UIMovePanel;
import com.gui.objects.UIObject;
import com.gui.objects.UIPanel;
import com.gui.objects.UISelectionBox;
import com.gui.objects.UITakenPiecesPanel;
import com.gui.objects.UITextButton;

public class GUI {
	private Board board;
	private Display display;
	private MoveMaker mm;
	private UIBoardPanel boardPanel;

	private static final Font bold_font = new Font("Sans Serif", Font.BOLD, 15);

	public GUI() {
		display = new Display(1280, 720, "Chess");
//		display.setBackground(Color.white);
		display.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mm.reset();
			}
		});

		board = Board.create();

		UITakenPiecesPanel panelTakenPieces = new UITakenPiecesPanel(0, 0);
		panelTakenPieces.setBounds(10, 10, 200, 600);
		panelTakenPieces.setBorder(Color.black);
		display.add(panelTakenPieces);

		UIConsole panelConsole = new UIConsole();
		panelConsole.setBounds(10, 620, 1070, 90);
		panelConsole.setBorder(Color.black);
		panelConsole.setFont(new Font("Sans Serif", Font.PLAIN, 10));
		display.add(panelConsole);

		UIMovePanel panelMoves = new UIMovePanel(5, 5);
		panelMoves.setBounds(830, 10, 250, 600);
		panelMoves.setBorder(Color.black);
		panelMoves.setHorizontalAlignment(UIObject.CENTER);
		display.add(panelMoves);

		boardPanel = new UIBoardPanel(display);
		boardPanel.setBounds(220, 10, 600, 600);
		boardPanel.setBoard(board);
		boardPanel.setBorder(Color.black);

		display.add(boardPanel);

		mm = new MoveMaker(PlayerType.HUMAN, PlayerType.HUMAN, boardPanel);
		mm.setMoveExecutionListener(e -> {
			if (e.getMoveStatus() == MoveStatus.DONE) {
				boardPanel.setBoard(board = e.getNewBoard());
				boardPanel.setLastMove(e.getExecutedMove());
				panelMoves.addMove(e.getExecutedMove());
				if (e.getExecutedMove().isAttackMove()) {
					panelTakenPieces.addPiece(e.getExecutedMove().getAttackedPiece());
				}
			}
		});

		UIPanel panelSettings = new UIPanel();
		panelSettings.setBounds(1090, 10, 180, 280);
		panelSettings.setBorder(Color.black);
		display.add(panelSettings);

		UILabel lblPlayer1 = new UILabel("Player 1 (White)");
		lblPlayer1.setBounds(1100, 15, 160, 20);
		lblPlayer1.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer1.setFont(bold_font);
		panelSettings.add(lblPlayer1);

		UISelectionBox<PlayerType> boxPlayer1 = new UISelectionBox<PlayerType>(
				new PlayerType[] { PlayerType.HUMAN, PlayerType.AI });
		boxPlayer1.setBounds(1105, 45, 150, 25);
		boxPlayer1.setTextColor(Color.white);
		boxPlayer1.setBackground(Color.black);
		panelSettings.add(boxPlayer1);

		UILabel lblPlayer2 = new UILabel("Player 2 (Black)");
		lblPlayer2.setBounds(1100, 95, 160, 20);
		lblPlayer2.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer2.setFont(bold_font);
		panelSettings.add(lblPlayer2);

		UISelectionBox<PlayerType> boxPlayer2 = new UISelectionBox<PlayerType>(
				new PlayerType[] { PlayerType.HUMAN, PlayerType.AI });
		boxPlayer2.setBounds(1105, 125, 150, 25);
		boxPlayer2.setTextColor(Color.white);
		boxPlayer2.setBackground(Color.black);
		panelSettings.add(boxPlayer2);

		UILabel lblDepth = new UILabel("Search depth:");
		lblDepth.setBounds(1105, 165, 150, 20);
		lblDepth.setFont(UIObject.STANDARD_FONT);
		panelSettings.add(lblDepth);

		UISelectionBox<Integer> boxDepth = new UISelectionBox<Integer>(new Integer[] { 1, 2, 3, 4, 5 });
		boxDepth.setBounds(1105, 185, 150, 25);
		boxDepth.setTextColor(Color.white);
		boxDepth.setBackground(Color.black);
		boxDepth.setSelectedIndex(2);
		panelSettings.add(boxDepth);

		UISelectionBox<String> boxUsePruning = new UISelectionBox<String>(
				new String[] { "Not using pruning", "Using pruning" });
		boxUsePruning.setBounds(1105, 220, 150, 25);
		boxUsePruning.setTextColor(Color.white);
		boxUsePruning.setBackground(Color.black);
		boxUsePruning.setSelectedIndex(1);
		panelSettings.add(boxUsePruning);

		UITextButton btnSave = new UITextButton("Save Settings");
		btnSave.setBounds(1100, 255, 160, 25);
		btnSave.setTextColor(Color.white);
		btnSave.setBackground(Color.black);
		btnSave.setClickListener(e -> {
			saveSettings(boxPlayer1.getSelectedElement(), boxPlayer2.getSelectedElement(),
					boxDepth.getSelectedElement(), boxUsePruning.getSelectedIndex());
		});
		panelSettings.add(btnSave);

		UIPanel panelColors = new UIPanel();
		panelColors.setBounds(1090, 435, 180, 150);
		panelColors.setBorder(Color.black);
		display.add(panelColors);

		UITextButton btnLightColor = new UITextButton("Set Light Color");
		btnLightColor.setBounds(1100, 445, 160, 25);
		btnLightColor.setTextColor(Color.white);
		btnLightColor.setBackground(Color.black);
		btnLightColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new light color",
					boardPanel.getLightColor());
			if (newColor != null)
				boardPanel.setLightColor(newColor);
		});
		panelColors.add(btnLightColor);

		UITextButton btnDarkColor = new UITextButton("Set Dark Color");
		btnDarkColor.setBounds(1100, 480, 160, 25);
		btnDarkColor.setTextColor(Color.white);
		btnDarkColor.setBackground(Color.black);
		btnDarkColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new dark color",
					boardPanel.getDarkColor());
			if (newColor != null)
				boardPanel.setDarkColor(newColor);
		});
		panelColors.add(btnDarkColor);

		UITextButton btnMoveColor = new UITextButton("Set Move Color");
		btnMoveColor.setBounds(1100, 515, 160, 25);
		btnMoveColor.setTextColor(Color.white);
		btnMoveColor.setBackground(Color.black);
		btnMoveColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new move color",
					boardPanel.getMoveColor());
			if (newColor != null)
				boardPanel.setMoveColor(newColor);
		});
		panelColors.add(btnMoveColor);

		UITextButton btnLastMoveColor = new UITextButton("Set last Move Color");
		btnLastMoveColor.setBounds(1100, 550, 160, 25);
		btnLastMoveColor.setTextColor(Color.white);
		btnLastMoveColor.setBackground(Color.black);
		btnLastMoveColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new last move color",
					boardPanel.getLastMoveColor());
			if (newColor != null)
				boardPanel.setLastMoveColor(newColor);
		});
		panelColors.add(btnLastMoveColor);

		UIPanel panelUtilities = new UIPanel();
		panelUtilities.setBounds(1090, 595, 180, 115);
		panelUtilities.setBorder(Color.black);
		display.add(panelUtilities);

		UITextButton btnUndo = new UITextButton("Undo");
		btnUndo.setBounds(1100, 605, 160, 25);
		btnUndo.setTextColor(Color.white);
		btnUndo.setBackground(Color.black);
		btnUndo.setClickListener(e -> {
			MoveTransition lastMoveTransition = board.getLastMoveTransition();

			if (lastMoveTransition != null) {
				Minimax.stopAll();

				board = lastMoveTransition.getOldBoard();
				mm.setBoard(board);
				boardPanel.setBoard(board);

				MoveTransition mtBeforeLastMt = lastMoveTransition.getOldBoard().getLastMoveTransition();
				if (mtBeforeLastMt != null) {
					boardPanel.setLastMove(mtBeforeLastMt.getExecutedMove());
				} else {
					boardPanel.setLastMove(null);
				}

				if (lastMoveTransition.getExecutedMove().isAttackMove()) {
					panelTakenPieces.removePiece(lastMoveTransition.getExecutedMove().getAttackedPiece());
				}

				panelMoves.removeMove(lastMoveTransition.getExecutedMove());

				mm.makeNextMove();
			}
		});
		panelUtilities.add(btnUndo);

		UITextButton btnReset = new UITextButton("Reset");
		btnReset.setBounds(1100, 640, 160, 25);
		btnReset.setTextColor(Color.white);
		btnReset.setBackground(Color.black);
		btnReset.setClickListener(e -> {
			mm.reset();
			boardPanel.setLastMove(null);
			panelTakenPieces.clear();
			panelMoves.clear();
			mm = new MoveMaker(boxPlayer1.getSelectedElement(), boxPlayer2.getSelectedElement(), boardPanel);
			mm.setMoveExecutionListener(m -> {
				if (m.getMoveStatus().isDone()) {
					boardPanel.setBoard(board = m.getNewBoard());
					panelMoves.addMove(m.getExecutedMove());
					if (m.getExecutedMove().isAttackMove()) {
						panelTakenPieces.addPiece(m.getExecutedMove().getAttackedPiece());
					}
				}
			});
		});
		panelUtilities.add(btnReset);

		UITextButton btnGitHub = new UITextButton("GitHub repository");
		btnGitHub.setBounds(1100, 675, 160, 25);
		btnGitHub.setTextColor(Color.white);
		btnGitHub.setBackground(Color.black);
		btnGitHub.setClickListener(e -> {
			try {
				Desktop.getDesktop().browse(new URL("https://github.com/DefensivLord/ChessAI").toURI());
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		});
		panelUtilities.add(btnGitHub);
	}

	private void saveSettings(PlayerType player1, PlayerType player2, int depth, int usePruning) {
		mm.setPlayers(player1, player2);
		mm.setDepth(depth);
		mm.setUsingPruning(usePruning == 1);
		UIConsole.log("Set Player 1 to " + player1.toString() + "; Set Player 2 to " + player2.toString()
				+ "; Set search depth to " + depth + "; " + (usePruning == 0 ? "Not using Pruning" : "Using Pruning"));
	}

	// ===== Getters ===== \\
	public Display getDisplay() {
		return display;
	}

	// ===== Setters ===== \\
	public void setBoard(Board board) {
		this.board = board;
		this.boardPanel.setBoard(board);
	}

	public static void main(String[] args) {
		new GUI();
	}
}