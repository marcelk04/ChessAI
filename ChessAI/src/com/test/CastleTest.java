package com.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.chess.Board;
import com.chess.Board.Builder;
import com.chess.CastlingConfiguration;
import com.chess.move.Move;
import com.chess.pieces.King;
import com.chess.pieces.Rook;
import com.chess.pieces.Team;

class CastleTest {
	@Test
	void test() {
		Builder b = new Builder();

		b.setPiece(new Rook(0, Team.BLACK));
		b.setPiece(new King(4, Team.BLACK));
		b.setPiece(new Rook(7, Team.BLACK));

		b.setPiece(new Rook(56, Team.WHITE));
		b.setPiece(new King(60, Team.WHITE));
		b.setPiece(new Rook(63, Team.WHITE));

		b.setMoveMaker(Team.WHITE);
		b.setCastlingConfiguration(CastlingConfiguration.ALL_TRUE);

		Board board = b.build();

		List<Move> whiteLegals = board.getWhitePlayer().getLegalMoves();
		List<Move> blackLegals = board.getBlackPlayer().getLegalMoves();

		assertTrue(whiteLegals.contains(board.findMove(60, 62)));
		assertTrue(whiteLegals.contains(board.findMove(60, 58)));

		assertTrue(blackLegals.contains(board.findMove(4, 6)));
		assertTrue(blackLegals.contains(board.findMove(4, 2)));
	}
}