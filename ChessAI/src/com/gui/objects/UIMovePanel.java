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
		String text = "";
		Move m;

		for (int i = moves.size() - 1; i >= 0; i--) {
			m = moves.get(i);
			text += m.getMovedPiece().getTeam().toString() + ": " + m.getNotation();

			if (i > 0)
				text += "\n";
		}

		super.setText(text);
	}

	public void addMove(Move m) {
		moves.add(m);
		if (super.text != null && !super.text.equals(""))
			super.setText(super.text + "\n" + m.getMovedPiece().getTeam().toString() + ": " + m.getNotation());
		else
			super.setText(m.getMovedPiece().getTeam().toString() + ": " + m.getNotation());
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