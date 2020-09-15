package states;

import java.awt.Graphics;

import chess.board.ChessBoard;
import main.Game;

public class GameState extends State {
	private ChessBoard board;

	public GameState(Game game) {
		super(game);

		board = new ChessBoard(game.getMouseManager());
	}

	@Override
	public void tick() {
		board.tick();
	}

	@Override
	public void render(Graphics g) {
		board.render(g);
	}
	
	public ChessBoard getBoard() {
		return board;
	}
}