package main;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JColorChooser;

import chess.Board;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import display.Display;
import gfx.Assets;
import ui.listeners.ClickListener;
import ui.listeners.MoveExecutionListener;
import ui.objects.UIBoardPanel;
import ui.objects.UILabel;
import ui.objects.UIObject;
import ui.objects.UIPanel;
import ui.objects.UISelectionBox;
import ui.objects.UITextButton;

public class GUI {
	private Board board;
	private Display display;

	private static final Font bold_font = new Font("Sans Serif", Font.BOLD, 15);

	public GUI() {
		Assets.init();

		display = new Display("Chess", 1010, 820);
		display.setBackground(Color.white);

		board = Board.create();

		UIBoardPanel boardPanel = new UIBoardPanel();
		boardPanel.setBounds(10, 10, 800, 800);
		boardPanel.setBoard(board);
		boardPanel.setBorder(Color.black);
		boardPanel.setMoveExecutionListener(new MoveExecutionListener() {
			@Override
			public void onMoveExecution(MoveTransition e) {
				if (e.getMoveStatus() == MoveStatus.DONE) {
					boardPanel.setBoard(board = e.getNewBoard());
				}
			}
		});
		display.add(boardPanel);

		UIPanel panelSettings = new UIPanel();
		panelSettings.setBounds(820, 10, 180, 230);
		panelSettings.setBorder(Color.black);
		display.add(panelSettings);

		UILabel lblPlayer1 = new UILabel("Player 1 (White)");
		lblPlayer1.setBounds(820, 15, 180, 20);
		lblPlayer1.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer1.setFont(bold_font);
		panelSettings.add(lblPlayer1);

		UISelectionBox<String> boxPlayer1 = new UISelectionBox<String>(new String[] { "Player", "AI" });
		boxPlayer1.setBounds(835, 55, 150, 25);
		boxPlayer1.setTextColor(Color.white);
		boxPlayer1.setBackground(Color.black);
		panelSettings.add(boxPlayer1);

		UILabel lblPlayer2 = new UILabel("Player 2 (Black)");
		lblPlayer2.setBounds(820, 105, 180, 20);
		lblPlayer2.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer2.setFont(bold_font);
		panelSettings.add(lblPlayer2);

		UISelectionBox<String> boxPlayer2 = new UISelectionBox<String>(new String[] { "Player", "AI" });
		boxPlayer2.setBounds(835, 145, 150, 25);
		boxPlayer2.setTextColor(Color.white);
		boxPlayer2.setBackground(Color.black);
		panelSettings.add(boxPlayer2);

		UITextButton btnSave = new UITextButton("Save Settings");
		btnSave.setBounds(830, 205, 160, 25);
		btnSave.setTextColor(Color.white);
		btnSave.setBackground(Color.black);
		btnSave.setClickListener(new ClickListener() {
			@Override
			public void onClick(MouseEvent e) {
				saveSettings();
			}
		});
		panelSettings.add(btnSave);

		UIPanel panelColor = new UIPanel();
		panelColor.setBounds(820, 250, 180, 115);
		panelColor.setBorder(Color.black);
		display.add(panelColor);

		UITextButton btnLightColor = new UITextButton("Set Light Color");
		btnLightColor.setBounds(830, 260, 160, 25);
		btnLightColor.setTextColor(Color.white);
		btnLightColor.setBackground(Color.black);
		btnLightColor.setClickListener(new ClickListener() {
			@Override
			public void onClick(MouseEvent e) {
				Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new light color",
						boardPanel.getLightColor());
				if (newColor != null)
					boardPanel.setLightColor(newColor);
			}
		});
		panelColor.add(btnLightColor);

		UITextButton btnDarkColor = new UITextButton("Set Dark Color");
		btnDarkColor.setBounds(830, 295, 160, 25);
		btnDarkColor.setTextColor(Color.white);
		btnDarkColor.setBackground(Color.black);
		btnDarkColor.setClickListener(new ClickListener() {
			@Override
			public void onClick(MouseEvent e) {
				Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new dark color",
						boardPanel.getDarkColor());
				if (newColor != null)
					boardPanel.setDarkColor(newColor);
			}
		});
		panelColor.add(btnDarkColor);

		UITextButton btnMoveColor = new UITextButton("Set Move Color");
		btnMoveColor.setBounds(830, 330, 160, 25);
		btnMoveColor.setTextColor(Color.white);
		btnMoveColor.setBackground(Color.black);
		btnMoveColor.setClickListener(new ClickListener() {
			@Override
			public void onClick(MouseEvent e) {
				Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new move color",
						boardPanel.getMoveColor());
				if (newColor != null)
					boardPanel.setMoveColor(newColor);
			}
		});
		display.add(btnMoveColor);

		UITextButton btnReset = new UITextButton("Reset");
		btnReset.setBounds(820, 740, 180, 30);
		btnReset.setTextColor(Color.white);
		btnReset.setBackground(Color.black);
		btnReset.setClickListener(new ClickListener() {
			@Override
			public void onClick(MouseEvent e) {
				boardPanel.setBoard(board = Board.create());
			}
		});
		display.add(btnReset);

		UITextButton btnGitHub = new UITextButton("GitHub repository");
		btnGitHub.setBounds(820, 780, 180, 30);
		btnGitHub.setTextColor(Color.white);
		btnGitHub.setBackground(Color.black);
		btnGitHub.setClickListener(new ClickListener() {
			@Override
			public void onClick(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URL("https://github.com/DefensivLord/ChessAI").toURI());
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		display.add(btnGitHub);
	}

	private void saveSettings() {
		System.out.println("Not yet implemented");
	}

	// ===== Getters ===== \\
	public Display getDisplay() {
		return display;
	}

	public static void main(String[] args) {
		new GUI().getDisplay().setVisible(true);
	}
}