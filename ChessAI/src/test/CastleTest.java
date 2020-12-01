package test;

import chess.Board.Builder;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Rook;
import chess.pieces.Team;
import main.GUI;

class CastleTest {
	public static void test() {
		GUI gui = new GUI();
		Builder b = new Builder();

		b.setPiece(new Rook(0, 0, Team.BLACK));
		b.setPiece(new King(4, 0, Team.BLACK, true, true));
		b.setPiece(new Rook(7, 0, Team.BLACK));
		b.setPiece(new Pawn(0, 1, Team.BLACK));
		b.setPiece(new Pawn(1, 1, Team.BLACK));
		b.setPiece(new Pawn(2, 1, Team.BLACK));
		b.setPiece(new Pawn(3, 1, Team.BLACK));
		b.setPiece(new Pawn(4, 1, Team.BLACK));
		b.setPiece(new Pawn(5, 1, Team.BLACK));
		b.setPiece(new Pawn(6, 1, Team.BLACK));
		b.setPiece(new Pawn(7, 1, Team.BLACK));

		b.setPiece(new Pawn(0, 6, Team.WHITE));
		b.setPiece(new Pawn(1, 6, Team.WHITE));
		b.setPiece(new Pawn(2, 6, Team.WHITE));
		b.setPiece(new Pawn(3, 6, Team.WHITE));
		b.setPiece(new Pawn(4, 6, Team.WHITE));
		b.setPiece(new Pawn(5, 6, Team.WHITE));
		b.setPiece(new Pawn(6, 6, Team.WHITE));
		b.setPiece(new Pawn(7, 6, Team.WHITE));
		b.setPiece(new Rook(0, 7, Team.WHITE));
		b.setPiece(new King(4, 7, Team.WHITE, true, true));
		b.setPiece(new Rook(7, 7, Team.WHITE));

		b.setMoveMaker(Team.WHITE);

		gui.setBoard(b.build());
		gui.getDisplay().setVisible(true);
	}

	public static void main(String[] args) {
		test();
	}
}