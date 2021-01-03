package chess.pieces;

public enum PieceType {
	KING('K', "King", 900), QUEEN('Q', "Queen", 90), ROOK('R', "Rook", 46), BISHOP('B', "Bishop", 32),
	KNIGHT('N', "Knight", 31), PAWN('P', "Pawn", 10);

	private final char letter;
	private final String name;
	private final int value;

	private PieceType(final char letter, final String name, final int value) {
		this.letter = letter;
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return name;
	}

	// ===== Getters ===== \\
	public char getLetter() {
		return letter;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
}