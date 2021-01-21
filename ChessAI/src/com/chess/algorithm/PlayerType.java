package com.chess.algorithm;

public enum PlayerType {
	HUMAN {
		@Override
		public String toString() {
			return "Human";
		}
	},
	AI {
		@Override
		public String toString() {
			return "AI";
		}
	};

	public abstract String toString();
}