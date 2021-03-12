package com.main;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.chess.Board;
import com.chess.ai.MinimaxAlgorithm;
import com.chess.ai.MinimaxAlgorithm.AIType;
import com.chess.ai.MoveMaker;
import com.chess.ai.PlayerType;
import com.chess.ai.evaluation.BoardEvaluator;
import com.chess.ai.evaluation.PawnPositionBoardEvaluator;
import com.chess.ai.evaluation.PositionBoardEvaluator;
import com.chess.ai.evaluation.SimpleBoardEvaluator;
import com.chess.move.MoveTransition;
import com.file.ColorSaver;
import com.file.PGNUtilities;
import com.gfx.Assets;
import com.gui.display.Display;
import com.gui.objects.UIBoardPanel;
import com.gui.objects.UIConsole;
import com.gui.objects.UIDialog;
import com.gui.objects.UIGraphPanel;
import com.gui.objects.UIImageButton;
import com.gui.objects.UILabel;
import com.gui.objects.UIMovePanel;
import com.gui.objects.UIObject;
import com.gui.objects.UIPanel;
import com.gui.objects.UIPiecesPanel;
import com.gui.objects.UISelectionBox;
import com.gui.objects.UITextButton;
import com.gui.objects.UITextField;

public class GUI {
	private Board board;
	private Display display;
	private MoveMaker mm;
	private UIBoardPanel boardPanel;
	private ColorSaver colorSaver;
	private List<Float> shownGraph;

	private static Color text_color = Color.white, button_background_color = Color.black, border_color = Color.black;
	@SuppressWarnings("unused")
	private static final Font normal_font = UIObject.STANDARD_FONT, bold_font = new Font("Sans Serif", Font.BOLD, 15);
	private static final int arc_width = 0, arc_height = 0;

	public GUI() {
		display = new Display(1280, 720, "Chess");
		display.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mm.reset();
//				TranspositionTable.dumpTableIntoFile("res/transpositionTable.txt");
			}
		});

		colorSaver = new ColorSaver("res/colorConfig.txt");

		UIPiecesPanel panelTakenPieces = new UIPiecesPanel(0, 0, 4);
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
			if (e.getMoveStatus().isDone()) {
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
			MinimaxAlgorithm.stopAll();
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

		UITextButton btnFile = new UITextButton("Open/Save File");
		btnFile.setBounds(1100, 120, 160, 25);
		btnFile.setArcBounds(arc_width, arc_height);
		btnFile.setTextColor(text_color);
		btnFile.setBackground(button_background_color);
		btnFile.setClickListener(e -> showFileDialog());
		display.add(btnFile);

		UITextButton btnGraphs = new UITextButton("Show Graphs");
		btnGraphs.setBounds(1100, 155, 160, 25);
		btnGraphs.setArcBounds(arc_width, arc_height);
		btnGraphs.setTextColor(text_color);
		btnGraphs.setBackground(button_background_color);
		btnGraphs.setClickListener(e -> showGraphDialog());
		display.add(btnGraphs);

		UIPanel panelUtilities = new UIPanel();
		panelUtilities.setBounds(1090, 560, 180, 150);
		panelUtilities.setArcBounds(arc_width, arc_height);
		panelUtilities.setBorder(border_color);
		display.add(panelUtilities);

		UITextButton btnUndo = new UITextButton("Undo");
		btnUndo.setBounds(10, 10, 160, 25);
		btnUndo.setArcBounds(arc_width, arc_height);
		btnUndo.setTextColor(text_color);
		btnUndo.setBackground(button_background_color);
		btnUndo.setClickListener(e -> {
			MoveTransition lastMoveTransition = board.getLastMoveTransition();

			if (lastMoveTransition == null)
				return;

			MinimaxAlgorithm.stopAll();

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
		});
		panelUtilities.addRelative(btnUndo);

		UITextButton btnReset = new UITextButton("Reset");
		btnReset.setBounds(10, 45, 160, 25);
		btnReset.setArcBounds(arc_width, arc_height);
		btnReset.setTextColor(text_color);
		btnReset.setBackground(button_background_color);
		btnReset.setClickListener(e -> {
			mm.reset();
			mm.setBoard(board = Board.create());
			boardPanel.setBoard(board);
			boardPanel.setLastMove(null);
			boardPanel.setMoveMaker(mm);
			panelTakenPieces.clear();
			panelMoves.clear();
		});
		panelUtilities.addRelative(btnReset);

		UITextButton btnGitHub = new UITextButton("GitHub repository");
		btnGitHub.setBounds(10, 80, 160, 25);
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
		panelUtilities.addRelative(btnGitHub);

		UITextButton btnHelp = new UITextButton("Help");
		btnHelp.setBounds(10, 115, 160, 25);
		btnHelp.setArcBounds(arc_width, arc_height);
		btnHelp.setTextColor(text_color);
		btnHelp.setBackground(button_background_color);
		btnHelp.setClickListener(e -> {
			StringBuilder sb = new StringBuilder();
			sb.append("================================\nHELP\n================================\n\n");
			sb.append("================\nConfigure AI\n================\n\n");
			sb.append("When pressing the button \"Configure AI\", a dialog opens showing several buttons. "
					+ "On the top you have two buttons next to eachother, directly under the labels "
					+ "\"Player 1 (White)\" and \"Player 2 (Black)\". Initially, those should both "
					+ "display the word \"Human\", meaning a human can play with these colors. However, "
					+ "if you click on these buttons, their state changes between \"Human\" and \"AI\". "
					+ "When \"AI\" is selected for a player, the computer is making the moves for that "
					+ "color. If both players are set to \"AI\", the computer is playing against itself. "
					+ "\nUnder these buttons you will find another buttons labeled \"Search Depth\". On "
					+ "that button you can choose a number between one and five, which determines how "
					+ "many moves the computer should look ahead. Generally speaking, the more moves, the "
					+ "better plays the computer, but it will also take longer. \nUnder that there is a "
					+ "button switching between the states \"Minimax\", \"Minimax + AlphaBeta\" and "
					+ "\"Minimax + AlphaBeta + MoveOrdering\". This button changes, how efficiently the "
					+ "algorithm should search moves. In this case, Minimax is the bare minimum, since it "
					+ "is just the algorithm wihtout any optimisations. If you add AlphaBeta, the "
					+ "algorithm will cut moves that will not lead to a good board off. With Move "
					+ "Ordering, the moves are sorted causing betters cutoffs. \nFollowing is a button "
					+ "where you can choose the type of evaluator you want. \"Simple Evaluator\" is here "
					+ "also the minimum, with the other evaluators extending this one. \"Position "
					+ "Evaluator\" is the best evaluator, since it is not too slow, but calculates good "
					+ "results by also taking the position of pieces into account. The \"Pawn Position "
					+ "Evaluator\" is an evaluator which also analyzes the pawn structure, however, "
					+ "because it is very slow, it is not recommended to use. \nBy pressing \"Save & "
					+ "Exit\", the changes will be saved and the dialog will close. The game will start "
					+ "directly. \"If you do not want to save your changes, you can just press \"Exit\", "
					+ "and immedeatly leave the dialog.\n\n");
			sb.append("================\nStop AI\n================\n\n");
			sb.append("The button \"Stop AI\" will stop the currently running algorithm and set both players "
					+ "to \"Human\".");

			UIDialog.showInformationDialog(display, sb.toString(), 600, 400);
		});
		panelUtilities.addRelative(btnHelp);

		display.getObjects().repaint();
	}

	private void showConfigurationDialog() {
		UIDialog dialog = new UIDialog();
		dialog.setSize(350, 265);
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

		UISelectionBox<PlayerType> boxPlayer2 = new UISelectionBox<PlayerType>(PlayerType.values());
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

		UISelectionBox<AIType> boxAIType = new UISelectionBox<AIType>(AIType.values());
		boxAIType.setBounds(10, 120, 330, 25);
		boxAIType.setArcBounds(arc_width, arc_height);
		boxAIType.setTextColor(text_color);
		boxAIType.setBackground(button_background_color);
		boxAIType.setSelectedElement(mm.getAIType());
		dialog.addRelative(boxAIType);

		UISelectionBox<BoardEvaluator> boxEvaluatorType = new UISelectionBox<BoardEvaluator>(new BoardEvaluator[] {
				SimpleBoardEvaluator.INSTANCE, PositionBoardEvaluator.INSTANCE, PawnPositionBoardEvaluator.INSTANCE });
		boxEvaluatorType.setBounds(10, 155, 330, 25);
		boxEvaluatorType.setArcBounds(arc_width, arc_height);
		boxEvaluatorType.setTextColor(text_color);
		boxEvaluatorType.setBackground(button_background_color);
		boxEvaluatorType.setSelectedElement(mm.getEvaluator());
		dialog.addRelative(boxEvaluatorType);

		UITextButton btnSave = new UITextButton("Save & Exit");
		btnSave.setBounds(10, 195, 330, 25);
		btnSave.setArcBounds(arc_width, arc_height);
		btnSave.setTextColor(text_color);
		btnSave.setBackground(button_background_color);
		btnSave.setClickListener(e -> {
			saveSettings(boxPlayer1.getSelectedElement(), boxPlayer2.getSelectedElement(),
					boxDepth.getSelectedElement(), boxAIType.getSelectedElement(),
					boxEvaluatorType.getSelectedElement());
			display.removeLastDialog();
		});
		dialog.addRelative(btnSave);

		UITextButton btnExit = new UITextButton("Exit");
		btnExit.setBounds(10, 230, 330, 25);
		btnExit.setArcBounds(arc_width, arc_height);
		btnExit.setTextColor(text_color);
		btnExit.setBackground(button_background_color);
		btnExit.setClickListener(e -> display.removeLastDialog());
		dialog.addRelative(btnExit);

		display.showDialog(dialog);
	}

	private void saveSettings(PlayerType player1, PlayerType player2, int depth, AIType aiType,
			BoardEvaluator evaluator) {
		mm.setDepth(depth);
		mm.setAIType(aiType);
		mm.setEvaluator(evaluator);
		mm.setPlayers(player1, player2);

		StringBuilder sb = new StringBuilder();
		sb.append("Set WHITE to " + player1);
		sb.append("|");
		sb.append("Set BLACK to " + player2);
		sb.append("|");
		sb.append("Set search depth to " + depth);
		sb.append("|");
		sb.append("Set AIType to " + aiType);
		sb.append("|");
		sb.append("Set BoardEvaluator to " + evaluator);

		UIConsole.log(sb.toString());
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

	private void showFileDialog() {
		UIDialog dialog = new UIDialog();
		dialog.setSize(180, 185);
		dialog.setPositionRelativeTo(display.getObjects());
		dialog.setArcBounds(arc_width, arc_height);
		dialog.setBorder(border_color);

		UITextButton btnLoadPGN = new UITextButton("Load PGN");
		btnLoadPGN.setBounds(10, 10, 160, 25);
		btnLoadPGN.setArcBounds(arc_width, arc_height);
		btnLoadPGN.setTextColor(text_color);
		btnLoadPGN.setBackground(button_background_color);
		btnLoadPGN.setClickListener(e -> {
		});
		dialog.addRelative(btnLoadPGN);

		UITextButton btnLoadFEN = new UITextButton("Load FEN");
		btnLoadFEN.setBounds(10, 45, 160, 25);
		btnLoadFEN.setArcBounds(arc_width, arc_height);
		btnLoadFEN.setTextColor(text_color);
		btnLoadFEN.setBackground(button_background_color);
		btnLoadFEN.setClickListener(e -> {
		});
		dialog.addRelative(btnLoadFEN);

		UITextButton btnSavePGN = new UITextButton("Save PGN");
		btnSavePGN.setBounds(10, 80, 160, 25);
		btnSavePGN.setArcBounds(arc_width, arc_height);
		btnSavePGN.setTextColor(text_color);
		btnSavePGN.setBackground(button_background_color);
		btnSavePGN.setClickListener(e -> showSaveAsPGNDialog());
		dialog.addRelative(btnSavePGN);

		UITextButton btnPrintFEN = new UITextButton("Print FEN");
		btnPrintFEN.setBounds(10, 115, 160, 25);
		btnPrintFEN.setArcBounds(arc_width, arc_height);
		btnPrintFEN.setTextColor(text_color);
		btnPrintFEN.setBackground(button_background_color);
		btnPrintFEN.setClickListener(e -> System.out.println(board.convertToFEN()));
		dialog.addRelative(btnPrintFEN);

		UITextButton btnExit = new UITextButton("Exit");
		btnExit.setBounds(10, 150, 160, 25);
		btnExit.setArcBounds(arc_width, arc_height);
		btnExit.setTextColor(text_color);
		btnExit.setBackground(button_background_color);
		btnExit.setClickListener(e -> display.removeLastDialog());
		dialog.addRelative(btnExit);

		display.showDialog(dialog);
	}

	private void showSaveAsPGNDialog() {
		UIDialog dialog = new UIDialog();
		dialog.setSize(270, 255);
		dialog.setPositionRelativeTo(display.getObjects());
		dialog.setArcBounds(arc_width, arc_height);
		dialog.setBorder(border_color);

		UILabel lblTitle = new UILabel("Save as PGN");
		lblTitle.setBounds(10, 10, 240, 25);
		lblTitle.setFont(new Font("Sans Serif", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(UIObject.CENTER);
		dialog.addRelative(lblTitle);

		UILabel lblEvent = new UILabel("Event:");
		lblEvent.setBounds(10, 45, 80, 25);
		lblEvent.setFont(bold_font);
		dialog.addRelative(lblEvent);

		UITextField tfEvent = new UITextField();
		tfEvent.setBounds(100, 45, 160, 20);
		tfEvent.setArcBounds(arc_width, arc_height);
		dialog.addRelative(tfEvent);

		UILabel lblSite = new UILabel("Site:");
		lblSite.setBounds(10, 80, 80, 25);
		lblSite.setFont(bold_font);
		dialog.addRelative(lblSite);

		UITextField tfSite = new UITextField();
		tfSite.setBounds(100, 80, 160, 20);
		tfSite.setArcBounds(arc_width, arc_height);
		dialog.addRelative(tfSite);

		UILabel lblWhite = new UILabel("White:");
		lblWhite.setBounds(10, 115, 80, 25);
		lblWhite.setFont(bold_font);
		dialog.addRelative(lblWhite);

		UITextField tfWhite = new UITextField();
		tfWhite.setBounds(100, 115, 160, 20);
		tfWhite.setArcBounds(arc_width, arc_height);
		dialog.addRelative(tfWhite);

		UILabel lblBlack = new UILabel("Black:");
		lblBlack.setBounds(10, 150, 80, 25);
		lblBlack.setFont(bold_font);
		dialog.addRelative(lblBlack);

		UITextField tfBlack = new UITextField();
		tfBlack.setBounds(100, 150, 160, 20);
		tfBlack.setArcBounds(arc_width, arc_height);
		dialog.addRelative(tfBlack);

		UITextButton btnDefault = new UITextButton("Fill in default");
		btnDefault.setBounds(10, 185, 250, 25);
		btnDefault.setArcBounds(arc_width, arc_height);
		btnDefault.setTextColor(text_color);
		btnDefault.setBackground(button_background_color);
		btnDefault.setClickListener(e -> {
			tfEvent.setText("ChessAI Event");
			tfSite.setText("Here");
			tfWhite.setText(mm.getPlayer1().toString());
			tfBlack.setText(mm.getPlayer2().toString());
		});
		dialog.addRelative(btnDefault);

		UITextButton btnSave = new UITextButton("Save");
		btnSave.setBounds(10, 220, 120, 25);
		btnSave.setArcBounds(arc_width, arc_height);
		btnSave.setTextColor(text_color);
		btnSave.setBackground(button_background_color);
		btnSave.setClickListener(e -> {
			if (tfEvent.getText().equals("") || tfSite.getText().equals("") || tfWhite.getText().equals("")
					|| tfBlack.getText().equals("")) {
				StringBuilder sb = new StringBuilder();
				sb.append("All textfields have to be filled!").append("\n");
				sb.append("Not filled textfields:").append("\n");

				if (tfEvent.getText().equals(""))
					sb.append("- Event").append("\n");
				if (tfSite.getText().equals(""))
					sb.append("- Site").append("\n");
				if (tfWhite.getText().equals(""))
					sb.append("- White").append("\n");
				if (tfBlack.getText().equals(""))
					sb.append("- Black").append("\n");

				UIDialog.showInformationDialog(display, sb.toString());
				return;
			}

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return ".pgn";
				}

				@Override
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().toLowerCase().endsWith(".pgn");
				}
			});
			int option = fileChooser.showSaveDialog(display.getFrame());
			if (option == JFileChooser.APPROVE_OPTION) {
				try {
					PGNUtilities.saveGameToPGN(fileChooser.getSelectedFile(), tfEvent.getText(), tfSite.getText(),
							new Date(), mm.getRound(), tfWhite.getText(), tfBlack.getText(), board.getResult(),
							board.getExecutedMoves());
					display.removeLastDialog();
				} catch (IOException e1) {
				}
			}
		});
		dialog.addRelative(btnSave);

		UITextButton btnBack = new UITextButton("Back");
		btnBack.setBounds(140, 220, 120, 25);
		btnBack.setArcBounds(arc_width, arc_height);
		btnBack.setTextColor(text_color);
		btnBack.setBackground(button_background_color);
		btnBack.setClickListener(e -> display.removeLastDialog());
		dialog.addRelative(btnBack);

		display.showDialog(dialog);
	}

	private void showGraphDialog() {
		UIDialog dialog = new UIDialog();
		dialog.setSize(1220, 655);
		dialog.setPositionRelativeTo(display.getObjects());
		dialog.setBorder(border_color);

		shownGraph = DataManager.searchedBoards;

		UIGraphPanel graphPanel = new UIGraphPanel(shownGraph);
		graphPanel.setBounds(10, 10, 1200, 600);
		dialog.addRelative(graphPanel);

		UISelectionBox<String> boxSwitchMode = new UISelectionBox<String>(
				new String[] { "Normal", "Average", "Derivative" });
		boxSwitchMode.setBounds(700, 620, 160, 25);
		boxSwitchMode.setArcBounds(arc_width, arc_height);
		boxSwitchMode.setTextColor(text_color);
		boxSwitchMode.setBackground(button_background_color);
		boxSwitchMode.setClickListener(e -> {
			switch (boxSwitchMode.getSelectedIndex()) {
			case 0:
				graphPanel.setValues(shownGraph);
				break;
			case 1:
				graphPanel.setValues(DataManager.calculateAverage(shownGraph));
				break;
			case 2:
				graphPanel.setValues(DataManager.calculateDerivative(shownGraph));
				break;
			}
		});
		dialog.addRelative(boxSwitchMode);

		UISelectionBox<String> boxSwitchGraph = new UISelectionBox<String>(
				new String[] { "Searched Boards", "Search times", "Pruned Boards", "Times pruned", "Pruned Boards (%)",
						"Transpositions", "Transpositions (%)", "Move Order Times" });
		boxSwitchGraph.setBounds(360, 620, 160, 25);
		boxSwitchGraph.setArcBounds(arc_width, arc_height);
		boxSwitchGraph.setTextColor(text_color);
		boxSwitchGraph.setBackground(button_background_color);
		boxSwitchGraph.setClickListener(e -> {
			boxSwitchMode.setSelectedIndex(0);

			switch (boxSwitchGraph.getSelectedIndex()) {
			case 0:
				shownGraph = DataManager.searchedBoards;
				break;
			case 1:
				shownGraph = DataManager.searchTimes;
				break;
			case 2:
				shownGraph = DataManager.prunedBoards;
				break;
			case 3:
				shownGraph = DataManager.timesPruned;
				break;
			case 4:
				shownGraph = DataManager.prunedBoardsPercent;
				break;
			case 5:
				shownGraph = DataManager.transpositions;
				break;
			case 6:
				shownGraph = DataManager.transpositionsPercent;
				break;
			case 7:
				shownGraph = DataManager.moveOrderTimes;
				break;
			}

			graphPanel.setValues(shownGraph);
		});
		dialog.addRelative(boxSwitchGraph);

		UITextButton btnOk = new UITextButton("Ok");
		btnOk.setBounds(530, 620, 160, 25);
		btnOk.setArcBounds(arc_width, arc_height);
		btnOk.setTextColor(text_color);
		btnOk.setBackground(button_background_color);
		btnOk.setClickListener(e -> display.removeLastDialog());
		dialog.addRelative(btnOk);

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