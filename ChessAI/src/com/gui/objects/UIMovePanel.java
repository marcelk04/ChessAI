package com.gui.objects;

import java.util.ArrayList;
import java.util.List;

import com.chess.move.Move;

public class UIMovePanel extends UITextArea {
	private List<Move> moves;

	public UIMovePanel() {
		this(10, 10);
	}

	public UIMovePanel(int hgap, int vgap) {
		super("", hgap, vgap);
		moves = new ArrayList<Move>();
	}

	@Override
	public void clear() {
		super.clear();
		moves.clear();
	}

	private void convertList() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < moves.size(); i++) {
			if (i % 2 == 0) {
				sb.append(i / 2 + 1).append(". ").append(moves.get(i).getNotation());
			} else {
				sb.append(" ").append(moves.get(i).getNotation()).append("\n");
			}
		}

		super.setText(sb.toString());
	}

	public void addMove(Move m) {
		moves.add(m);
		convertList();
	}

	public void removeMove(Move m) {
		moves.remove(m);
		convertList();
	}

	// ===== Getters ===== \\
	public List<Move> getMoves() {
		return moves;
	}

	// ===== Setters ===== \\
	public void setMoves(List<Move> moves) {
		this.moves = moves;
		convertList();
	}

	@Override
	public void setText(String text) {
	}
}