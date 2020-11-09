package chess.move;

import chess.Board;

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