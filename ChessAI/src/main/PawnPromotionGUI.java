package main;

import java.awt.Font;
import java.awt.image.BufferedImage;

import chess.pieces.PieceType;
import chess.pieces.Team;
import display.Display;
import gfx.Assets;
import ui.objects.UIImageButton;
import ui.objects.UILabel;
import ui.objects.UIObject;

public class PawnPromotionGUI {
	private final Display display;
	private PieceType selectedPiece = null;

	public PawnPromotionGUI(int width, int height, Team currentTeam) {
		this.display = new Display("Pawn Promotion", width, height);

		Assets.init();

		UILabel title = new UILabel("Select a piece");
		title.setBounds(0, 0, width, 30);
		title.setHorizontalAlignment(UIObject.CENTER);
		title.setFont(new Font("Sans Serif", Font.BOLD, 20));
		display.add(title);

		int buttonWidth = width / 4;
		int buttonHeight = height - 30;

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

		UIImageButton btnQueen = new UIImageButton(images[0], false);
		btnQueen.setBounds(buttonWidth * 0, 30, buttonWidth, buttonHeight);
		btnQueen.setClickListener(e -> selectedPiece = PieceType.QUEEN);
		display.add(btnQueen);

		UIImageButton btnRook = new UIImageButton(images[1], false);
		btnRook.setBounds(buttonWidth * 1, 30, buttonWidth, buttonHeight);
		btnRook.setClickListener(e -> selectedPiece = PieceType.ROOK);
		display.add(btnRook);

		UIImageButton btnBishop = new UIImageButton(images[2], false);
		btnBishop.setBounds(buttonWidth * 2, 30, buttonWidth, buttonHeight);
		btnBishop.setClickListener(e -> selectedPiece = PieceType.BISHOP);
		display.add(btnBishop);

		UIImageButton btnKnight = new UIImageButton(images[3], false);
		btnKnight.setBounds(buttonWidth * 3, 30, buttonWidth, buttonHeight);
		btnKnight.setClickListener(e -> selectedPiece = PieceType.KNIGHT);
		display.add(btnKnight);

		display.setVisible(true);
	}

	public static PieceType getPieceInput(int width, int height, Team currentTeam) {
		PawnPromotionGUI gui = new PawnPromotionGUI(width, height, currentTeam);
		PieceType selectedPiece;

		while ((selectedPiece = gui.getSelectedPiece()) == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		gui.getDisplay().close();
		return selectedPiece;
	}

	public static PieceType getPieceInput(Team currentTeam) {
		return getPieceInput(300, 105, currentTeam);
	}

	public PieceType getSelectedPiece() {
		return selectedPiece;
	}

	public Display getDisplay() {
		return display;
	}

	public static void main(String[] args) {
		System.out.println(PawnPromotionGUI.getPieceInput(Team.WHITE));
	}
}