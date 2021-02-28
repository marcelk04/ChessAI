package com.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.chess.Board;
import com.chess.Board.Builder;
import com.chess.ai.evaluation.PawnStructureAnalyzer;
import com.chess.pieces.King;
import com.chess.pieces.Team;
import com.main.Utils;

class PawnStructureTest {
	@Test
	void passedPawnTest() {
		Builder b = new Builder();
		b.setPiece(new King(0, Team.BLACK, false));
		b.setPiece(new King(7, Team.WHITE, false));
		b.setPiece(Utils.getMovedPawn(Team.WHITE, 33));
		b.setPiece(Utils.getMovedPawn(Team.BLACK, 34));
		b.setPiece(Utils.getMovedPawn(Team.WHITE, 42));
		b.setPiece(Utils.getMovedPawn(Team.WHITE, 44));
		b.setPiece(Utils.getMovedPawn(Team.BLACK, 21));
		b.setMoveMaker(Team.WHITE);
		Board board = b.build();

		assertEquals(1, PawnStructureAnalyzer.INSTANCE.findPassedPawns(board, Team.WHITE).size());
		assertEquals(0, PawnStructureAnalyzer.INSTANCE.findPassedPawns(board, Team.BLACK).size());
	}
}