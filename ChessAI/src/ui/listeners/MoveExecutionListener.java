package ui.listeners;

import chess.move.MoveTransition;

public interface MoveExecutionListener {
	void moveExecuted(MoveTransition e);
}