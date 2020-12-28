package main;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JColorChooser;

import algorithm.MoveMaker;
import algorithm.PlayerType;
import chess.Board;
import chess.move.Move.AttackMove;
import chess.move.MoveStatus;
import display.Display;
import gfx.Assets;
import ui.objects.UIBoardPanel;
import ui.objects.UIConsole;
import ui.objects.UILabel;
import ui.objects.UIMovePanel;
import ui.objects.UIObject;
import ui.objects.UIPanel;
import ui.objects.UISelectionBox;
import ui.objects.UITakenPiecesPanel;
import ui.objects.UITextButton;

public class GUI {
	private Board board;
	private Display display;
	private MoveMaker mm;
	private UIBoardPanel boardPanel;

	private static final Font bold_font = new Font("Sans Serif", Font.BOLD, 15);

	public GUI() {
		Assets.init();

		display = new Display("Chess", 1280, 720);
		display.setBackground(Color.white);

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

		boardPanel = new UIBoardPanel();
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
					AttackMove m = (AttackMove) e.getExecutedMove();
					panelTakenPieces.addPiece(m.getAttackedPiece());
				}
			}
		});

		UIPanel panelSettings = new UIPanel();
		panelSettings.setBounds(1090, 10, 180, 230);
		panelSettings.setBorder(Color.black);
		display.add(panelSettings);

		UILabel lblPlayer1 = new UILabel("Player 1 (White)");
		lblPlayer1.setBounds(1090, 15, 180, 20);
		lblPlayer1.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer1.setFont(bold_font);
		panelSettings.add(lblPlayer1);

		UISelectionBox<PlayerType> boxPlayer1 = new UISelectionBox<PlayerType>(
				new PlayerType[] { PlayerType.HUMAN, PlayerType.AI });
		boxPlayer1.setBounds(1105, 55, 150, 25);
		boxPlayer1.setTextColor(Color.white);
		boxPlayer1.setBackground(Color.black);
		panelSettings.add(boxPlayer1);

		UILabel lblPlayer2 = new UILabel("Player 2 (Black)");
		lblPlayer2.setBounds(1090, 105, 180, 20);
		lblPlayer2.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer2.setFont(bold_font);
		panelSettings.add(lblPlayer2);

		UISelectionBox<PlayerType> boxPlayer2 = new UISelectionBox<PlayerType>(
				new PlayerType[] { PlayerType.HUMAN, PlayerType.AI });
		boxPlayer2.setBounds(1105, 145, 150, 25);
		boxPlayer2.setTextColor(Color.white);
		boxPlayer2.setBackground(Color.black);
		panelSettings.add(boxPlayer2);

		UITextButton btnSave = new UITextButton("Save Settings");
		btnSave.setBounds(1100, 205, 160, 25);
		btnSave.setTextColor(Color.white);
		btnSave.setBackground(Color.black);
		btnSave.setClickListener(e -> {
			saveSettings(boxPlayer1.getSelectedElement(), boxPlayer2.getSelectedElement());
		});
		panelSettings.add(btnSave);

		UIPanel panelColor = new UIPanel();
		panelColor.setBounds(1090, 250, 180, 115);
		panelColor.setBorder(Color.black);
		display.add(panelColor);

		UITextButton btnLightColor = new UITextButton("Set Light Color");
		btnLightColor.setBounds(1100, 260, 160, 25);
		btnLightColor.setTextColor(Color.white);
		btnLightColor.setBackground(Color.black);
		btnLightColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new light color",
					boardPanel.getLightColor());
			if (newColor != null)
				boardPanel.setLightColor(newColor);
		});
		panelColor.add(btnLightColor);

		UITextButton btnDarkColor = new UITextButton("Set Dark Color");
		btnDarkColor.setBounds(1100, 295, 160, 25);
		btnDarkColor.setTextColor(Color.white);
		btnDarkColor.setBackground(Color.black);
		btnDarkColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new dark color",
					boardPanel.getDarkColor());
			if (newColor != null)
				boardPanel.setDarkColor(newColor);
		});
		panelColor.add(btnDarkColor);

		UITextButton btnMoveColor = new UITextButton("Set Move Color");
		btnMoveColor.setBounds(1100, 330, 160, 25);
		btnMoveColor.setTextColor(Color.white);
		btnMoveColor.setBackground(Color.black);
		btnMoveColor.setClickListener(e -> {
			Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new move color",
					boardPanel.getMoveColor());
			if (newColor != null)
				boardPanel.setMoveColor(newColor);
		});
		display.add(btnMoveColor);

		UITextButton btnReset = new UITextButton("Reset");
		btnReset.setBounds(1090, 640, 180, 30);
		btnReset.setTextColor(Color.white);
		btnReset.setBackground(Color.black);
		btnReset.setClickListener(e -> {
			mm.stop();
			boardPanel.setBoard(board = Board.create());
			boardPanel.setLastMove(null);
			panelTakenPieces.clear();
			panelMoves.clear();
			mm = new MoveMaker(boxPlayer1.getSelectedElement(), boxPlayer2.getSelectedElement(), boardPanel);
			mm.setMoveExecutionListener(m -> {
				if (m.getMoveStatus() == MoveStatus.DONE) {
					boardPanel.setBoard(board = m.getNewBoard());
					panelMoves.addMove(m.getExecutedMove());
					if (m.getExecutedMove().isAttackMove()) {
						AttackMove a = (AttackMove) m.getExecutedMove();
						panelTakenPieces.addPiece(a.getAttackedPiece());
					}
				}
			});
		});
		display.add(btnReset);

		UITextButton btnGitHub = new UITextButton("GitHub repository");
		btnGitHub.setBounds(1090, 680, 180, 30);
		btnGitHub.setTextColor(Color.white);
		btnGitHub.setBackground(Color.black);
		btnGitHub.setClickListener(e -> {
			try {
				Desktop.getDesktop().browse(new URL("https://github.com/DefensivLord/ChessAI").toURI());
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		});
		display.add(btnGitHub);
	}

	private void saveSettings(PlayerType player1, PlayerType player2) {
		mm.setPlayers(player1, player2);
		UIConsole.log("Set Player 1 to " + player1.toString() + "; Set Player 2 to " + player2.toString());
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
		new GUI().getDisplay().setVisible(true);
	}
}