package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import chess.Board;
import chess.Board.Builder;
import chess.move.Move.PawnJump;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Team;

class EnPassantTest {

	@Test
	void whiteEnPassantTest() {
		Builder b = new Builder();

		Pawn p;

		b.setPiece(new Pawn(3, 3, Team.WHITE));
		b.setPiece(new King(4, 7, Team.WHITE, true, true));

		b.setPiece(p = new Pawn(4, 1, Team.BLACK));
		b.setPiece(new King(4, 0, Team.BLACK, true, true));

		b.setMoveMaker(Team.BLACK);

		Board board = b.build();

		MoveTransition mt = board.getCurrentPlayer().makeMove(new PawnJump(board, p, 4, 3));

		assertEquals(MoveStatus.DONE, mt.getMoveStatus());

		board = mt.getNewBoard();

		assertTrue(board.getWhitePlayer().getLegalMoves().contains(board.findMove(3, 3, 4, 2)));
	}

	@Test
	void blackEnPassantTest() {
		Builder b = new Builder();

		Pawn p;

		b.setPiece(p = new Pawn(3, 6, Team.WHITE));
		b.setPiece(new King(4, 7, Team.WHITE, true, true));

		b.setPiece(new Pawn(4, 4, Team.BLACK));
		b.setPiece(new King(4, 0, Team.BLACK, true, true));

		b.setMoveMaker(Team.WHITE);

		Board board = b.build();

		MoveTransition mt = board.getCurrentPlayer().makeMove(new PawnJump(board, p, 3, 4));

		assertEquals(MoveStatus.DONE, mt.getMoveStatus());

		board = mt.getNewBoard();

		assertTrue(board.getBlackPlayer().getLegalMoves().contains(board.findMove(4, 4, 3, 5)));
	}
}