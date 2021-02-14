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
import com.chess.ai.Minimax;
import com.chess.ai.MoveMaker;
import com.chess.ai.PlayerType;
import com.chess.ai.hashing.ZobristHashing;
import com.chess.move.MoveStatus;
import com.chess.move.MoveTransition;
import com.file.ColorSaver;
import com.gfx.Assets;
import com.gui.display.Display;
import com.gui.objects.UIBoardPanel;
import com.gui.objects.UIConsole;
import com.gui.objects.UIDialog;
import com.gui.objects.UIImageButton;
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
	private ColorSaver colorSaver;

	private static Color text_color = Color.white, button_background_color = Color.black, border_color = Color.black;
	@SuppressWarnings("unused")
	private static final Font normal_font = UIObject.STANDARD_FONT, bold_font = new Font("Sans Serif", Font.BOLD, 15);
	private static final int arc_width = 0, arc_height = 0;

	public GUI() {
		display = new Display(1280, 720, "Chess");
//		display.setBackground(Color.white);
		display.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mm.reset();
			}
		});

		colorSaver = new ColorSaver("res/colorConfig.txt");

//		board = Board.create();

		UITakenPiecesPanel panelTakenPieces = new UITakenPiecesPanel(0, 0);
		panelTakenPieces.setBounds(10, 10, 200, 600);
		panelTakenPieces.setArcBounds(arc_width, arc_height);
		panelTakenPieces.setBorder(border_color);
		display.add(panelTakenPieces);

		UIConsole panelConsole = new UIConsole();
		panelConsole.setBounds(10, 620, 1070, 90);
		panelConsole.setArcBounds(arc_width, arc_height);
		panelConsole.setBorder(border_color);
		panelConsole.setFont(new Font("Sans Serif", Font.PLAIN, 10));
		display.add(panelConsole);

		UIMovePanel panelMoves = new UIMovePanel(5, 5);
		panelMoves.setBounds(830, 10, 250, 600);
		panelMoves.setArcBounds(arc_width, arc_height);
		panelMoves.setBorder(border_color);
		panelMoves.setHorizontalAlignment(UIObject.CENTER);
		display.add(panelMoves);

		boardPanel = new UIBoardPanel(display);
		boardPanel.setBounds(220, 10, 600, 600);
		boardPanel.setBorder(border_color);
		boardPanel.setLightColor(colorSaver.getLightColor());
		boardPanel.setDarkColor(colorSaver.getDarkColor());
		boardPanel.setMoveColor(colorSaver.getMoveColor());
		boardPanel.setLastMoveColor(colorSaver.getLastMoveColor());

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
		boardPanel.setBoard(board = mm.getBoard());

		UITextButton btnConfigureAI = new UITextButton("Configure AI");
		btnConfigureAI.setBounds(1100, 15, 160, 25);
		btnConfigureAI.setArcBounds(arc_width, arc_height);
		btnConfigureAI.setTextColor(text_color);
		btnConfigureAI.setBackground(button_background_color);
		btnConfigureAI.setClickListener(e -> showConfigurationDialog());
		display.add(btnConfigureAI);

		UITextButton btnStopAI = new UITextButton("Stop AI");
		btnStopAI.setBounds(1100, 50, 160, 25);
		btnStopAI.setArcBounds(arc_width, arc_height);
		btnStopAI.setTextColor(text_color);
		btnStopAI.setBackground(button_background_color);
		btnStopAI.setClickListener(e -> {
			Minimax.stopAll();
			mm.setPlayers(PlayerType.HUMAN, PlayerType.HUMAN);
		});
		display.add(btnStopAI);

		UITextButton btnEditColors = new UITextButton("Edit colors");
		btnEditColors.setBounds(1100, 85, 160, 25);
		btnEditColors.setArcBounds(arc_width, arc_height);
		btnEditColors.setTextColor(text_color);
		btnEditColors.setBackground(button_background_color);
		btnEditColors.setClickListener(e -> showColorDialog());
		display.add(btnEditColors);

		UIPanel panelUtilities = new UIPanel();
		panelUtilities.setBounds(1090, 595, 180, 115);
		panelUtilities.setArcBounds(arc_width, arc_height);
		panelUtilities.setBorder(border_color);
		display.add(panelUtilities);

		UITextButton btnUndo = new UITextButton("Undo");
		btnUndo.setBounds(1100, 605, 160, 25);
		btnUndo.setArcBounds(arc_width, arc_height);
		btnUndo.setTextColor(text_color);
		btnUndo.setBackground(button_background_color);
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
		btnReset.setArcBounds(arc_width, arc_height);
		btnReset.setTextColor(text_color);
		btnReset.setBackground(button_background_color);
		btnReset.setClickListener(e -> {
			mm.reset();
			mm.setBoard(board = Board.create());
			boardPanel.setBoard(board);
			boardPanel.setLastMove(null);
			panelTakenPieces.clear();
			panelMoves.clear();
		});
		panelUtilities.add(btnReset);

		UITextButton btnGitHub = new UITextButton("GitHub repository");
		btnGitHub.setBounds(1100, 675, 160, 25);
		btnGitHub.setArcBounds(arc_width, arc_height);
		btnGitHub.setTextColor(text_color);
		btnGitHub.setBackground(button_background_color);
		btnGitHub.setClickListener(e -> {
			try {
				Desktop.getDesktop().browse(new URL("https://github.com/DefensivLord/ChessAI").toURI());
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		});
		panelUtilities.add(btnGitHub);

		display.getObjects().repaint();
	}

	private void showConfigurationDialog() {
		UIDialog dialog = new UIDialog();
		dialog.setSize(350, 230);
		dialog.setPositionRelativeTo(display.getObjects());
		dialog.setArcBounds(arc_width, arc_height);
		dialog.setBorder(border_color);

		UILabel lblPlayer1 = new UILabel("Player 1 (White)");
		lblPlayer1.setBounds(10, 10, 160, 25);
		lblPlayer1.setArcBounds(arc_width, arc_height);
		lblPlayer1.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer1.setFont(bold_font);
		dialog.addRelative(lblPlayer1);

		UISelectionBox<PlayerType> boxPlayer1 = new UISelectionBox<PlayerType>(
				new PlayerType[] { PlayerType.HUMAN, PlayerType.AI });
		boxPlayer1.setBounds(10, 40, 160, 25);
		boxPlayer1.setArcBounds(arc_width, arc_height);
		boxPlayer1.setTextColor(text_color);
		boxPlayer1.setBackground(button_background_color);
		boxPlayer1.setSelectedElement(mm.getPlayer1());
		dialog.addRelative(boxPlayer1);

		UILabel lblPlayer2 = new UILabel("Player 2 (Black)");
		lblPlayer2.setBounds(180, 10, 160, 25);
		lblPlayer2.setArcBounds(arc_width, arc_height);
		lblPlayer2.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer2.setFont(bold_font);
		dialog.addRelative(lblPlayer2);

		UISelectionBox<PlayerType> boxPlayer2 = new UISelectionBox<PlayerType>(
				new PlayerType[] { PlayerType.HUMAN, PlayerType.AI });
		boxPlayer2.setBounds(180, 40, 160, 25);
		boxPlayer2.setArcBounds(arc_width, arc_height);
		boxPlayer2.setTextColor(text_color);
		boxPlayer2.setBackground(button_background_color);
		boxPlayer2.setSelectedElement(mm.getPlayer2());
		dialog.addRelative(boxPlayer2);

		UILabel lblDepth = new UILabel("Search depth:");
		lblDepth.setBounds(10, 85, 160, 25);
		lblDepth.setHorizontalAlignment(UIObject.RIGHT);
		dialog.addRelative(lblDepth);

		UISelectionBox<Integer> boxDepth = new UISelectionBox<Integer>(new Integer[] { 1, 2, 3, 4, 5 });
		boxDepth.setBounds(180, 85, 160, 25);
		boxDepth.setArcBounds(arc_width, arc_height);
		boxDepth.setTextColor(text_color);
		boxDepth.setBackground(button_background_color);
		boxDepth.setSelectedIndex(mm.getDepth() - 1);
		dialog.addRelative(boxDepth);

		UISelectionBox<String> boxUsePruning = new UISelectionBox<String>(
				new String[] { "Not using pruning", "Using pruning" });
		boxUsePruning.setBounds(10, 120, 160, 25);
		boxUsePruning.setArcBounds(arc_width, arc_height);
		boxUsePruning.setTextColor(text_color);
		boxUsePruning.setBackground(button_background_color);
		boxUsePruning.setSelectedIndex(mm.isUsingPruning() ? 1 : 0);
		dialog.addRelative(boxUsePruning);

		UISelectionBox<String> boxOrderMoves = new UISelectionBox<String>(
				new String[] { "Not ordering moves", "Ordering moves simple", "Ordering moves complex" });
		boxOrderMoves.setBounds(180, 120, 160, 25);
		boxOrderMoves.setArcBounds(arc_width, arc_height);
		boxOrderMoves.setTextColor(text_color);
		boxOrderMoves.setBackground(button_background_color);
		if (mm.isOrderingMovesSimple())
			boxOrderMoves.setSelectedIndex(1);
		else if (mm.isOrderingMovesComplex())
			boxOrderMoves.setSelectedIndex(2);
		dialog.addRelative(boxOrderMoves);

		UITextButton btnSave = new UITextButton("Save & Exit");
		btnSave.setBounds(10, 160, 330, 25);
		btnSave.setArcBounds(arc_width, arc_height);
		btnSave.setTextColor(text_color);
		btnSave.setBackground(button_background_color);
		btnSave.setClickListener(e -> {
			saveSettings(boxPlayer1.getSelectedElement(), boxPlayer2.getSelectedElement(),
					boxDepth.getSelectedElement(), boxUsePruning.getSelectedIndex(), boxOrderMoves.getSelectedIndex());
			display.removeLastDialog();
		});
		dialog.addRelative(btnSave);

		UITextButton btnExit = new UITextButton("Exit");
		btnExit.setBounds(10, 195, 330, 25);
		btnExit.setArcBounds(arc_width, arc_height);
		btnExit.setTextColor(text_color);
		btnExit.setBackground(button_background_color);
		btnExit.setClickListener(e -> display.removeLastDialog());
		dialog.addRelative(btnExit);

		display.showDialog(dialog);
	}

	private void saveSettings(PlayerType player1, PlayerType player2, int depth, int usePruning, int orderMoves) {
		mm.setPlayers(player1, player2);
		mm.setDepth(depth);
		mm.setUsingPruning(usePruning == 1);
		mm.setOrderingMovesSimple(orderMoves == 1);
		mm.setOrderingMovesComplex(orderMoves == 2);
		UIConsole.log("Set Player 1 to " + player1.toString() + "; Set Player 2 to " + player2.toString()
				+ "; Set search depth to " + depth + "; " + (usePruning == 0 ? "Not using Pruning" : "Using Pruning")
				+ "; " + (orderMoves == 0 ? "Not ordering moves" : "Ordering moves"));
	}

	private void showColorDialog() {
		UIDialog dialog = new UIDialog();
		dialog.setSize(270, 225);
		dialog.setPositionRelativeTo(display.getObjects());
		dialog.setArcBounds(arc_width, arc_height);
		dialog.setBorder(border_color);

		UILabel lblLightColor = new UILabel("Light color");
		lblLightColor.setBounds(10, 10, 160, 25);
		lblLightColor.setFont(bold_font);
		dialog.addRelative(lblLightColor);

		UIImageButton btnLightColor = new UIImageButton(Assets.pencil, false);
		btnLightColor.setBounds(180, 10, 80, 25);
		btnLightColor.setArcBounds(arc_width, arc_height);
		btnLightColor.setBackground(boardPanel.getLightColor());
		btnLightColor.setBorder(border_color);
		btnLightColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Edit light color",
					btnLightColor.getBackground());
			if (newColor != null) {
				btnLightColor.setBackground(newColor);
				colorSaver.setLightColor(newColor);
			}
		});
		dialog.addRelative(btnLightColor);

		UILabel lblDarkColor = new UILabel("Dark color");
		lblDarkColor.setBounds(10, 45, 160, 25);
		lblDarkColor.setFont(bold_font);
		dialog.addRelative(lblDarkColor);

		UIImageButton btnDarkColor = new UIImageButton(Assets.pencil, false);
		btnDarkColor.setBounds(180, 45, 80, 25);
		btnDarkColor.setArcBounds(arc_width, arc_height);
		btnDarkColor.setBackground(boardPanel.getDarkColor());
		btnDarkColor.setBorder(border_color);
		btnDarkColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Edit dark color",
					btnDarkColor.getBackground());
			if (newColor != null) {
				btnDarkColor.setBackground(newColor);
				colorSaver.setDarkColor(newColor);
			}
		});
		dialog.addRelative(btnDarkColor);

		UILabel lblMoveColor = new UILabel("Move color");
		lblMoveColor.setBounds(10, 80, 160, 25);
		lblMoveColor.setFont(bold_font);
		dialog.addRelative(lblMoveColor);

		UIImageButton btnMoveColor = new UIImageButton(Assets.pencil, false);
		btnMoveColor.setBounds(180, 80, 80, 25);
		btnMoveColor.setArcBounds(arc_width, arc_height);
		btnMoveColor.setBackground(boardPanel.getMoveColor());
		btnMoveColor.setBorder(border_color);
		btnMoveColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Edit move color",
					btnMoveColor.getBackground());
			if (newColor != null) {
				btnMoveColor.setBackground(newColor);
				colorSaver.setMoveColor(newColor);
			}
		});
		dialog.addRelative(btnMoveColor);

		UILabel lblLastMoveColor = new UILabel("Last move color");
		lblLastMoveColor.setBounds(10, 115, 160, 25);
		lblLastMoveColor.setFont(bold_font);
		dialog.addRelative(lblLastMoveColor);

		UIImageButton btnLastMoveColor = new UIImageButton(Assets.pencil, false);
		btnLastMoveColor.setBounds(180, 115, 80, 25);
		btnLastMoveColor.setArcBounds(arc_width, arc_height);
		btnLastMoveColor.setBackground(boardPanel.getLastMoveColor());
		btnLastMoveColor.setBorder(border_color);
		btnLastMoveColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Edit last move color",
					btnLastMoveColor.getBackground());
			if (newColor != null) {
				btnLastMoveColor.setBackground(newColor);
				colorSaver.setLastMoveColor(newColor);
			}
		});
		dialog.addRelative(btnLastMoveColor);

		UITextButton btnReset = new UITextButton("Reset");
		btnReset.setBounds(10, 155, 250, 25);
		btnReset.setArcBounds(arc_width, arc_height);
		btnReset.setTextColor(text_color);
		btnReset.setBackground(button_background_color);
		btnReset.setClickListener(e -> {
			btnLightColor.setBackground(UIBoardPanel.STANDARD_LIGHT_COLOR);
			btnDarkColor.setBackground(UIBoardPanel.STANDARD_DARK_COLOR);
			btnMoveColor.setBackground(UIBoardPanel.STANDARD_MOVE_COLOR);
			btnLastMoveColor.setBackground(UIBoardPanel.STANDARD_LAST_MOVE_COLOR);

			colorSaver.setLightColor(UIBoardPanel.STANDARD_LIGHT_COLOR);
			colorSaver.setDarkColor(UIBoardPanel.STANDARD_DARK_COLOR);
			colorSaver.setMoveColor(UIBoardPanel.STANDARD_MOVE_COLOR);
			colorSaver.setLastMoveColor(UIBoardPanel.STANDARD_LAST_MOVE_COLOR);
		});
		dialog.addRelative(btnReset);

		UITextButton btnSave = new UITextButton("Save & Exit");
		btnSave.setBounds(10, 190, 120, 25);
		btnSave.setArcBounds(arc_width, arc_height);
		btnSave.setTextColor(text_color);
		btnSave.setBackground(button_background_color);
		btnSave.setClickListener(e -> {
			boardPanel.setLightColor(btnLightColor.getBackground());
			boardPanel.setDarkColor(btnDarkColor.getBackground());
			boardPanel.setMoveColor(btnMoveColor.getBackground());
			boardPanel.setLastMoveColor(btnLastMoveColor.getBackground());
			colorSaver.saveColors();
			display.removeLastDialog();
		});
		dialog.addRelative(btnSave);

		UITextButton btnExit = new UITextButton("Exit");
		btnExit.setBounds(140, 190, 120, 25);
		btnExit.setArcBounds(arc_width, arc_height);
		btnExit.setTextColor(text_color);
		btnExit.setBackground(button_background_color);
		btnExit.setClickListener(e -> display.removeLastDialog());
		dialog.addRelative(btnExit);

		display.showDialog(dialog);
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