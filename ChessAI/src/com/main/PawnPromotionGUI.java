package com.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import com.chess.pieces.Piece.PieceType;
import com.chess.pieces.Team;
import com.gfx.Assets;
import com.gui.display.Display;
import com.gui.objects.UIDialog;
import com.gui.objects.UIImageButton;
import com.gui.objects.UILabel;
import com.gui.objects.UIObject;

public class PawnPromotionGUI {
	private static PieceType selectedPiece = null;

	private PawnPromotionGUI() {
	}

	public static PieceType getPieceInput(Team currentTeam, Display display) {
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

		UIDialog dialog = new UIDialog();
		dialog.setSize(300, 105);
		dialog.setPositionRelativeTo(display.getObjects());
		dialog.setBorder(Color.black);

		UILabel lblTitle = new UILabel("Select a piece");
		lblTitle.setBounds(0, 0, 300, 30);
		lblTitle.setHorizontalAlignment(UIObject.CENTER);
		lblTitle.setFont(new Font("Sans Serif", Font.BOLD, 20));
		dialog.addRelative(lblTitle);

		UIImageButton btnQueen = new UIImageButton(images[0], false);
		btnQueen.setBounds(0, 30, 75, 75);
		btnQueen.setClickListener(e -> selectedPiece = PieceType.QUEEN);
		dialog.addRelative(btnQueen);

		UIImageButton btnRook = new UIImageButton(images[1], false);
		btnRook.setBounds(75, 30, 75, 75);
		btnRook.setClickListener(e -> selectedPiece = PieceType.ROOK);
		dialog.addRelative(btnRook);

		UIImageButton btnBishop = new UIImageButton(images[2], false);
		btnBishop.setBounds(150, 30, 75, 75);
		btnBishop.setClickListener(e -> selectedPiece = PieceType.BISHOP);
		dialog.addRelative(btnBishop);

		UIImageButton btnKnight = new UIImageButton(images[3], false);
		btnKnight.setBounds(225, 30, 75, 75);
		btnKnight.setClickListener(e -> selectedPiece = PieceType.KNIGHT);
		dialog.addRelative(btnKnight);

		display.showDialog(dialog);

		while (selectedPiece == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		display.removeLastDialog();

		return selectedPiece;
	}
}