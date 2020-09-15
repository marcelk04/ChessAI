package tree;

import algorithm.Move;
import chess.board.ChessBoard;
import chess.pieces.Piece.Team;

public class ChessEvent {
	private ChessBoard board;
	private Move move;
	private int eval;

	public ChessEvent(ChessBoard board, Move move) {
		this.board = board;
		this.move = move;
		this.eval = board.getValue(Team.black) - board.getValue(Team.white);
	}

	public ChessEvent(ChessBoard board, Move move, int eval) {
		this.board = board;
		this.move = move;
		this.eval = eval;
	}
	
	public ChessEvent(int eval) {
		this.eval = eval;
	}

	@Override
	public String toString() {
		return eval + (move != null ? "|" + move.getData() + "|" + move.getPiece().getTeam() : "");
	}

	public ChessBoard getBoard() {
		return board;
	}

	public void setBoard(ChessBoard board) {
		this.board = board;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public int getEval() {
		return eval;
	}

	public void setEval(int eval) {
		this.eval = eval;
	}
}