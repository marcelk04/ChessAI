package chess.pieces;

import chess.Board;

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
		public int getEval(Board board) {
			int eval = 0;
			for (Piece p : board.getWhitePieces()) {
				eval += p.getValue();
			}
			return eval;
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
		public int getEval(Board board) {
			int eval = 0;
			for (Piece p : board.getBlackPieces()) {
				eval -= p.getValue();
			}
			return eval;
		}
	};

	public abstract int moveDirection();

	@Override
	public abstract String toString();

	public abstract int getEval(Board board);
}