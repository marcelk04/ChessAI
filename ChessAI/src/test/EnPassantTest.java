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

		b.setPiece(new Pawn(27, Team.WHITE));
		b.setPiece(new King(60, Team.WHITE, true));

		b.setPiece(p = new Pawn(12, Team.BLACK));
		b.setPiece(new King(4, Team.BLACK, true));

		b.setMoveMaker(Team.BLACK);

		Board board = b.build();

		MoveTransition mt = board.getCurrentPlayer().makeMove(new PawnJump(board, p, 28));

		assertEquals(MoveStatus.DONE, mt.getMoveStatus());

		board = mt.getNewBoard();

		assertTrue(board.getWhitePlayer().getLegalMoves().contains(board.findMove(27, 20)));
	}

	@Test
	void blackEnPassantTest() {
		Builder b = new Builder();

		Pawn p;

		b.setPiece(p = new Pawn(51, Team.WHITE));
		b.setPiece(new King(60, Team.WHITE, true));

		b.setPiece(new Pawn(36, Team.BLACK));
		b.setPiece(new King(4, Team.BLACK, true));

		b.setMoveMaker(Team.WHITE);

		Board board = b.build();

		MoveTransition mt = board.getCurrentPlayer().makeMove(new PawnJump(board, p, 35));

		assertEquals(MoveStatus.DONE, mt.getMoveStatus());

		board = mt.getNewBoard();

		assertTrue(board.getBlackPlayer().getLegalMoves().contains(board.findMove(36, 43)));
	}
}