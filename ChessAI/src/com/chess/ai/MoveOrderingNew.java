package com.chess.ai;

import java.util.ArrayList;
import java.util.List;

import com.chess.move.Move;
import com.chess.move.Move.PawnPromotion;

public class MoveOrderingNew {
	private static double orderTime, orderCount;

	public static List<Move> orderMoves(List<Move> moves) {
		long time = System.currentTimeMillis();
		List<Move> orderedMoves = new ArrayList<Move>();
		orderedMoves.addAll(moves);
		orderedMoves.sort((m1, m2) -> {
			return guessMoveScore(m2) - guessMoveScore(m1);
		});
		orderTime += System.currentTimeMillis() - time;
		orderCount++;
		return orderedMoves;
	}

	private static int guessMoveScore(Move move) {
		int guess = 0;

		if (move.isAttackMove()) {
			guess += 10 * move.getAttackedPiece().getValue() - move.getMovedPiece().getValue();
		}

		if (move.isPawnPromotion()) {
			guess += ((PawnPromotion) move).getPromotionPiece().getValue();
		}

		return guess;
	}

	public static double getOrderTime() {
		return orderTime / orderCount;
	}

	public static void reset() {
		orderTime = 0;
		orderCount = 0;
	}
}