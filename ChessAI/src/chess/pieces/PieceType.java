package chess.pieces;

public enum PieceType {
	KING('K', "King", 9000), QUEEN('Q', "Queen", 900), ROOK('R', "Rook", 460), BISHOP('B', "Bishop", 320),
	KNIGHT('N', "Knight", 310), PAWN('P', "Pawn", 100);

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