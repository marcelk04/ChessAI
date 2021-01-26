package com.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.chess.pieces.Piece.PieceType;
import com.chess.pieces.Team;
import com.gfx.Assets;
import com.gui.display.Display;
import com.gui.objects.UIImageButton;
import com.gui.objects.UILabel;
import com.gui.objects.UIObject;

public class PawnPromotionGUI {
	private static PieceType selectedPiece = null;

	private PawnPromotionGUI() {
	}

	public static PieceType getPieceInput(Team currentTeam, Display display) {
		display.getObjects().getObjects().forEach(o -> o.setEnabled(false));

		Graphics g = display.getGraphics();
		g.setColor(new Color(63, 63, 63, 171));
		g.fillRect(0, 0, display.getWidth(), display.getHeight());

		int buttonWidth = 75;
		int buttonHeight = buttonWidth;

		int buttonX = display.getWidth() / 2 - buttonWidth * 2;
		int buttonY = (display.getHeight() - buttonHeight) / 2;

		BufferedImage[] images = new BufferedImage[4];

		if (currentTeam == Team.WHITE) {
			images[0] = Assets.white_queen;
			images[1] = Assets.white_rook;
			images[2] = Assets.white_bishop;
			images[3] = Assets.white_knight;
		} else {
			images[0] = Assets.black_queen;
			images[1] = Assets.black_rook;
			images[2] = Assets.black_bishop;
			images[3] = Assets.black_knight;
		}

		selectedPiece = null;

		UILabel lblTitle = new UILabel("Select a piece");
		lblTitle.setBounds(buttonX, buttonY - 30, buttonWidth * 4, 30);
		lblTitle.setHorizontalAlignment(UIObject.CENTER);
		lblTitle.setFont(new Font("Sans Serif", Font.BOLD, 20));
		display.add(lblTitle);

		UIImageButton btnQueen = new UIImageButton(images[0], false);
		btnQueen.setBounds(buttonX + buttonWidth * 0, buttonY, buttonWidth, buttonHeight);
		btnQueen.setClickListener(e -> selectedPiece = PieceType.QUEEN);
		display.add(btnQueen);

		UIImageButton btnRook = new UIImageButton(images[1], false);
		btnRook.setBounds(buttonX + buttonWidth * 1, buttonY, buttonWidth, buttonHeight);
		btnRook.setClickListener(e -> selectedPiece = PieceType.ROOK);
		display.add(btnRook);

		UIImageButton btnBishop = new UIImageButton(images[2], false);
		btnBishop.setBounds(buttonX + buttonWidth * 2, buttonY, buttonWidth, buttonHeight);
		btnBishop.setClickListener(e -> selectedPiece = PieceType.BISHOP);
		display.add(btnBishop);

		UIImageButton btnKnight = new UIImageButton(images[3], false);
		btnKnight.setBounds(buttonX + buttonWidth * 3, buttonY, buttonWidth, buttonHeight);
		btnKnight.setClickListener(e -> selectedPiece = PieceType.KNIGHT);
		display.add(btnKnight);

		while (selectedPiece == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		display.remove(btnQueen);
		display.remove(btnRook);
		display.remove(btnBishop);
		display.remove(btnKnight);

		display.getObjects().getObjects().forEach(o -> o.setEnabled(true));
		display.getObjects().repaint();

		return selectedPiece;
	}
}