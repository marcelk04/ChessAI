package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import chess.Board;
import chess.Board.Builder;
import chess.move.Move;
import chess.pieces.King;
import chess.pieces.Rook;
import chess.pieces.Team;

class CastleTest {
	@Test
	void test() {
		Builder b = new Builder();

		b.setPiece(new Rook(0, 0, Team.BLACK));
		b.setPiece(new King(4, 0, Team.BLACK, true, true));
		b.setPiece(new Rook(7, 0, Team.BLACK));

		b.setPiece(new Rook(0, 7, Team.WHITE));
		b.setPiece(new King(4, 7, Team.WHITE, true, true));
		b.setPiece(new Rook(7, 7, Team.WHITE));

		b.setMoveMaker(Team.WHITE);

		Board board = b.build();

		List<Move> whiteLegals = board.getWhitePlayer().getLegalMoves();
		List<Move> blackLegals = board.getBlackPlayer().getLegalMoves();

		assertTrue(whiteLegals.contains(board.findMove(4, 7, 6, 7)));
		assertTrue(whiteLegals.contains(board.findMove(4, 7, 2, 7)));

		assertTrue(blackLegals.contains(board.findMove(4, 0, 6, 0)));
		assertTrue(blackLegals.contains(board.findMove(4, 0, 2, 0)));
	}
}