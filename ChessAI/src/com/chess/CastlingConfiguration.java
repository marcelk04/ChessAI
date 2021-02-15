package com.chess;

public class CastlingConfiguration {
	public static final CastlingConfiguration ALL_TRUE = new CastlingConfiguration(true, true, true, true);
	public static final CastlingConfiguration ALL_FALSE = new CastlingConfiguration(false, false, false, false);

	public boolean canWhiteKingSideCastle, canWhiteQueenSideCastle;
	public boolean canBlackKingSideCastle, canBlackQueenSideCastle;

	public CastlingConfiguration() {
	}

	public CastlingConfiguration(boolean canWhiteKingSideCastle, boolean canWhiteQueenSideCastle,
			boolean canBlackKingSideCastle, boolean canBlackQueenSideCastle) {
		this.canWhiteKingSideCastle = canWhiteKingSideCastle;
		this.canWhiteQueenSideCastle = canWhiteQueenSideCastle;
		this.canBlackKingSideCastle = canBlackKingSideCastle;
		this.canBlackQueenSideCastle = canBlackQueenSideCastle;
	}

	// ===== Getters ===== \\
	public boolean isCanWhiteKingSideCastle() {
		return canWhiteKingSideCastle;
	}

	public boolean isCanWhiteQueenSideCastle() {
		return canWhiteQueenSideCastle;
	}

	public boolean isCanBlackKingSideCastle() {
		return canBlackKingSideCastle;
	}

	public boolean isCanBlackQueenSideCastle() {
		return canBlackQueenSideCastle;
	}

	// ===== Setters ===== \\
	public void setCanWhiteKingSideCastle(boolean canWhiteKingSideCastle) {
		this.canWhiteKingSideCastle = canWhiteKingSideCastle;
	}

	public void setCanWhiteQueenSideCastle(boolean canWhiteQueenSideCastle) {
		this.canWhiteQueenSideCastle = canWhiteQueenSideCastle;
	}

	public void setCanBlackKingSideCastle(boolean canBlackKingSideCastle) {
		this.canBlackKingSideCastle = canBlackKingSideCastle;
	}

	public void setCanBlackQueenSideCastle(boolean canBlackQueenSideCastle) {
		this.canBlackQueenSideCastle = canBlackQueenSideCastle;
	}

}