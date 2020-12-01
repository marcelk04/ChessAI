package chess.pieces;

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
	};

	public abstract int moveDirection();

	@Override
	public abstract String toString();
}