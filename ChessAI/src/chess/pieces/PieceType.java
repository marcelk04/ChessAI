package chess.pieces;

public enum PieceType {
	KING('K', "King"), QUEEN('Q', "Queen"), ROOK('R', "Rook"), BISHOP('B', "Bishop"), KNIGHT('N', "Knight"),
	PAWN('P', "Pawn");

	private char letter;
	private String name;

	private PieceType(char letter, String name) {
		this.letter = letter;
		this.name = name;
	}

	public char getLetter() {
		return letter;
	}

	public String getName() {
		return name;
	}
}