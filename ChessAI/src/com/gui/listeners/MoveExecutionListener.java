package com.gui.listeners;

import com.chess.move.MoveTransition;

public interface MoveExecutionListener {
	void onMoveExecution(MoveTransition e);
}