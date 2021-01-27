package com.chess.move;

import com.chess.Board;

public class MoveTransition {
	private final Board oldBoard, newBoard;
	private final Move executedMove;
	private final MoveStatus moveStatus;

	public MoveTransition(final Board oldBoard, final Board newBoard, final Move executedMove,
			final MoveStatus moveStatus) {
		this.oldBoard = oldBoard;
		this.newBoard = newBoard;
		this.executedMove = executedMove;
		this.moveStatus = moveStatus;

		newBoard.setLastMoveTransition(this);
	}

	@Override
	public String toString() {
		return "Old Board: " + oldBoard + "; New Board: " + newBoard + "; Executed Move: " + executedMove.getNotation()
				+ "; Move Status: " + moveStatus;
	}

	public Board getOldBoard() {
		return oldBoard;
	}

	public Board getNewBoard() {
		return newBoard;
	}

	public Move getExecutedMove() {
		return executedMove;
	}

	public MoveStatus getMoveStatus() {
		return moveStatus;
	}
}