package main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JColorChooser;

import chess.Board;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import display.Display;
import gfx.Assets;
import ui.listeners.ClickListener;
import ui.listeners.MoveExecutionListener;
import ui.objects.UIBoardPanel;
import ui.objects.UIButton;
import ui.objects.UILabel;
import ui.objects.UIObject;
import ui.objects.UIPanel;
import ui.objects.UISelectionBox;

public class GUI {
	private static Board board;
	private static Display display;

	private static final Font bold_font = new Font("Sans Serif", Font.BOLD, 15);

	public GUI() {
		Assets.init();

		display = new Display("Chess", 1000, 800);
		display.setBackground(Color.white);

		board = Board.create();

		UIBoardPanel boardPanel = new UIBoardPanel();
		boardPanel.setBounds(0, 0, 800, 800);
		boardPanel.setBoard(board);
		boardPanel.setMoveExecutionListener(new MoveExecutionListener() {
			@Override
			public void moveExecuted(MoveTransition e) {
				if (e.getMoveStatus() == MoveStatus.DONE) {
					boardPanel.setBoard(board = e.getNewBoard());
				}
			}
		});
		display.add(boardPanel);

		UIPanel panelSettings = new UIPanel();
		panelSettings.setBounds(810, 10, 180, 230);
		panelSettings.setBorder(Color.black);
		display.add(panelSettings);

		UILabel lblPlayer1 = new UILabel("Player 1");
		lblPlayer1.setBounds(810, 15, 180, 20);
		lblPlayer1.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer1.setFont(bold_font);
		panelSettings.add(lblPlayer1);

		UISelectionBox<String> boxPlayer1 = new UISelectionBox<String>(new String[] { "Player", "AI" });
		boxPlayer1.setBounds(825, 55, 150, 25);
		boxPlayer1.setBorder(Color.black);
		panelSettings.add(boxPlayer1);

		UILabel lblPlayer2 = new UILabel("Player 2");
		lblPlayer2.setBounds(810, 105, 180, 20);
		lblPlayer2.setHorizontalAlignment(UIObject.CENTER);
		lblPlayer2.setFont(bold_font);
		panelSettings.add(lblPlayer2);

		UISelectionBox<String> boxPlayer2 = new UISelectionBox<String>(new String[] { "Player", "AI" });
		boxPlayer2.setBounds(825, 145, 150, 25);
		boxPlayer2.setBorder(Color.black);
		panelSettings.add(boxPlayer2);

		UIButton btnSave = new UIButton("Save Settings");
		btnSave.setBounds(820, 205, 160, 25);
		btnSave.setTextColor(Color.white);
		btnSave.setBackground(Color.black);
		btnSave.setClickListener(new ClickListener() {
			@Override
			public void onClick() {
				saveSettings();
			}
		});
		panelSettings.add(btnSave);

		UIPanel panelColor = new UIPanel();
		panelColor.setBounds(810, 250, 180, 80);
		panelColor.setBorder(Color.black);
		display.add(panelColor);

		UIButton btnLightColor = new UIButton("Set Light Color");
		btnLightColor.setBounds(820, 260, 160, 25);
		btnLightColor.setTextColor(Color.white);
		btnLightColor.setBackground(Color.black);
		btnLightColor.setClickListener(new ClickListener() {
			@Override
			public void onClick() {
				Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new light color",
						boardPanel.getLightColor());
				if (newColor != null)
					boardPanel.setLightColor(newColor);
			}
		});
		panelColor.add(btnLightColor);

		UIButton btnDarkColor = new UIButton("Set Dark Color");
		btnDarkColor.setBounds(820, 295, 160, 25);
		btnDarkColor.setTextColor(Color.white);
		btnDarkColor.setBackground(Color.black);
		btnDarkColor.setClickListener(new ClickListener() {
			@Override
			public void onClick() {
				Color newColor = JColorChooser.showDialog(display.getFrame(), "Choose new dark color",
						boardPanel.getDarkColor());
				if (newColor != null)
					boardPanel.setDarkColor(newColor);
			}
		});
		panelColor.add(btnDarkColor);

		UIButton btnReset = new UIButton("Reset");
		btnReset.setBounds(820, 760, 160, 25);
		btnReset.setTextColor(Color.white);
		btnReset.setBackground(Color.black);
		btnReset.setClickListener(new ClickListener() {
			@Override
			public void onClick() {
				boardPanel.setBoard(board = Board.create());
			}
		});
		display.add(btnReset);
	}

	private void saveSettings() {
		System.out.println("saved");
	}

	public void show() {
		display.setVisible(true);
	}
}