package chess.pieces;

import chess.Board;
import chess.player.Player;

public enum Team {
	WHITE {
		@Override
		public int moveDirection() {
			return -1;
		}

		@Override
		public String toString() {
			return "White";
		}

		@Override
		public Player getPlayer(Board board) {
			return board.getWhitePlayer();
		}
	},
	BLACK {
		@Override
		public int moveDirection() {
			return 1;
		}

		@Override
		public String toString() {
			return "Black";
		}

		@Override
		public Player getPlayer(Board board) {
			return board.getBlackPlayer();
		}
	};

	public abstract int moveDirection();

	@Override
	public abstract String toString();

	public abstract Player getPlayer(Board board);
}