package chess.pieces;

public enum PieceType {
	KING('K', "King"), QUEEN('Q', "Queen"), ROOK('R', "Rook"), BISHOP('B', "Bishop"), KNIGHT('N', "Knight"),
	PAWN('P', "Pawn");

	private final char letter;
	private final String name;

	private PieceType(final char letter, final String name) {
		this.letter = letter;
		this.name = name;
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
}