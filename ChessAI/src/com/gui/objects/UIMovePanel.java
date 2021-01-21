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

		for (int i = 0; i < moves.size(); i++) {
			m = moves.get(i);
			text += m.getMovedPiece().getTeam().toString() + ": " + m.getNotation();

			if (i < moves.size() - 1)
				text += "\n";
		}

		super.setText(text);
	}

	public void addMove(Move m) {
		moves.add(0, m);
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