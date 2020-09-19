package chess.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import algorithm.MinimaxAlgorithm;
import algorithm.Move;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Piece.Team;
import chess.pieces.Queen;
import chess.pieces.Rook;
import input.MouseManager;
import tree.TreeGUI;

public class ChessBoard {
	private Piece[][] board;
	private MouseManager mouseManager;
	private Piece selectedPiece;
	private Set<Move> selectedPiece_moves;
	private Team playingTeam, winner;
	private King white_king, black_king;
	private boolean win = false, playingAgainstAI = true;
	private MinimaxAlgorithm algorithm;
	private Color backgroundColor;
	private TreeGUI gui;

	public ChessBoard(MouseManager mouseManager) {
		board = new Piece[8][8];
		this.mouseManager = mouseManager;
		playingTeam = Team.white;
		backgroundColor = Color.lightGray;
		gui = new TreeGUI();

		init();
	}

	private ChessBoard() {
		board = new Piece[8][8];
	}

	public void tick() {
		if (mouseManager.isLeftPressed() && !win) {
			int clickedX = (int) (mouseManager.getMouseX() / 80);
			int clickedY = (int) (mouseManager.getMouseY() / 80);

			if (selectedPiece != null && selectedPiece.movesContain(clickedX, clickedY)) {
				// if there is a piece to move and a tile the piece can move to has been clicked
				makeMove(new Move(selectedPiece, clickedX, clickedY));
				selectedPiece = null;
				selectedPiece_moves = null;

				if (!win && playingAgainstAI)
					algorithm = new MinimaxAlgorithm(this, 2, gui);
			} else {
				selectedPiece = getPiece(clickedX, clickedY);
				if (selectedPiece != null && (selectedPiece.getTeam() == playingTeam
						|| playingAgainstAI && selectedPiece.getTeam() == Team.white)) {
					// if the player clicked on a piece that can be moved
					selectedPiece_moves = selectedPiece.getMoves();
				} else {
					selectedPiece = null;
					selectedPiece_moves = null;
				}
			}
		}

		if (win)
			algorithm = null;
	}

	public void render(Graphics g) {
		g.setColor(backgroundColor);

		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board.length; x++) {
				if ((x + y + 1) % 2 == 0)
					g.fillRect(x * 80, y * 80, 80, 80);

				if (board[x][y] != null)
					board[x][y].render(g);
			}
		}

		if (selectedPiece_moves != null) {
			g.setColor(Color.black);
			for (Move m : selectedPiece_moves) {
				g.drawRect(m.getNewX() * 80, m.getNewY() * 80, 80, 80);
			}
		}

		if (winner == Team.black) {
			g.setColor(Color.black);
			g.setFont(new Font("Impact", Font.BOLD, 30));
			g.drawString("Black has won!", 210, 335);
		} else if (winner == Team.white) {
			g.setColor(Color.black);
			g.setFont(new Font("Impact", Font.BOLD, 30));
			g.drawString("White has won!", 210, 335);
		}
	}

	/**
	 * Executes a move. Also see {@link ChessBoard#movePiece}.
	 * 
	 * @param move the move the be made.
	 * @return {@code false} if there is no piece to be moved.
	 */
	public boolean makeMove(Move move) {
		boolean moved = movePiece(move.getOldX(), move.getOldY(), move.getNewX(), move.getNewY());
		move.updatePiecePosition();
		return moved;
	}

	/**
	 * Moves a piece from a coordinate to another.
	 * 
	 * @param x    the x coordinate of the piece.
	 * @param y    the y coordinate of the piece.
	 * @param newX the new x coordinate of the piece.
	 * @param newY the new y coordinate of the piece.
	 * @return {@code false} if there is no piece to be moved.
	 */
	public boolean movePiece(int x, int y, int newX, int newY) {
		if (board[x][y] == null)
			return false;

		board[newX][newY] = board[x][y];
		board[x][y] = null;

		if (playingTeam == Team.white)
			playingTeam = Team.black;
		else
			playingTeam = Team.white;

		return true;
	}

	/**
	 * Returns the current evaluation of the board.
	 * 
	 * @return the evaluation of the board.
	 */
	public int getEvaluation() {
		return getValue(Team.black) - getValue(Team.white);
	}

	/**
	 * Resets the board to the beginning state. Also executes the
	 * {@link ChessBoard#init} method.
	 */
	public void reset() {
		algorithm = null;
		win = false;
		playingTeam = Team.white;
		winner = null;
		selectedPiece = null;
		selectedPiece_moves = null;

		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board.length; x++) {
				board[x][y] = null;
			}
		}

		init();
	}

	/**
	 * Returns the piece at the specified coordinates. Will return {@code null} if
	 * there is no piece.
	 * 
	 * @param x the x coordinate.
	 * @param y the y coordinate.
	 * @return the piece at the coordinates.
	 */
	public Piece getPiece(int x, int y) {
		return board[x][y];
	}

	/**
	 * Returns the piece at the destination coordinates of a Move. Returns
	 * {@code null} if there is no piece at the specified coordinates.
	 * 
	 * @param move the move.
	 * @return the piece at the destination coordinates.
	 */
	public Piece getPiece(Move move) {
		if (move != null)
			return getPiece(move.getNewX(), move.getNewY());
		return null;
	}

	/**
	 * Adds a piece to the board. The coordinates used are the ones specified by the
	 * piece.
	 * 
	 * @param piece the piece to be added.
	 */
	public void addPiece(Piece piece) {
		int posX = piece.getX();
		int posY = piece.getY();
		board[posX][posY] = piece;
	}

	/**
	 * Sets a piece on the board. Only used for cloning the board.
	 * 
	 * @param x     the x coordinate of the piece.
	 * @param y     the y coordinate of the piece.
	 * @param piece the piece to be set.
	 */
	private void setPiece(int x, int y, Piece piece) {
		board[x][y] = piece;
		if (piece instanceof King) {
			if (piece.getTeam() == Team.white)
				this.white_king = (King) piece;
			else if (piece.getTeam() == Team.black)
				this.black_king = (King) piece;
		}
	}

	@Override
	public ChessBoard clone() {
		ChessBoard board = new ChessBoard();

		for (int y = 0; y < this.board.length; y++) {
			for (int x = 0; x < this.board.length; x++) {
				if (this.board[x][y] != null)
					board.setPiece(x, y, this.board[x][y].clone(board));
			}
		}

		return board;
	}

	/**
	 * Returns the winner of the game and {@code null} if neither team has won.
	 * 
	 * @return the winner.
	 */
	public Team getWinner() {
		return winner;
	}

	/**
	 * Checks if a team has won the game.
	 * 
	 * @return {@code true} if a team has won.
	 */
	public boolean checkWin() {
		if (white_king == null) {
			win = true;
			winner = Team.black;
		} else if (black_king == null) {
			win = true;
			winner = Team.white;
		}

		return win;
	}

	/**
	 * Returns the value of a specific team. The value is currently just determined
	 * by the value of the pieces, not their positions.
	 * <p>
	 * TODO Expand evaluation of the value of a team.
	 * 
	 * @param team the team of the pieces.
	 * @return the value of the specific team.
	 */
	public int getValue(Team team) {
		int value = 0;

		List<Piece> pieces = getPieces(team);

		for (Piece p : pieces)
			value += p.getValue();

		return value;
	}

	/**
	 * Returns all pieces of a specific team.
	 * 
	 * @param team the team of the pieces.
	 * @return all pieces of the specified team.
	 */
	public List<Piece> getPieces(Team team) {
		List<Piece> pieces = new ArrayList<Piece>();

		for (int y = 0; y < this.board.length; y++) {
			for (int x = 0; x < this.board.length; x++) {
				if (this.board[x][y] != null && this.board[x][y].getTeam() == team) {
					pieces.add(this.board[x][y]);
				}
			}
		}

		return pieces;
	}

	/**
	 * Returns the king of a specific team.
	 * 
	 * @param team the team of the king.
	 * @return the king of the specified team.
	 */
	public King getKing(Team team) {
		if (team == Piece.Team.white)
			return white_king;
		else
			return black_king;
	}

	/**
	 * Initializes the ChessBoard with all pieces in their correct starting
	 * positions. Also sets both kings.
	 */
	private void init() {
		for (int i = 0; i < 8; i++) {
			addPiece(new Pawn(i, 1, Piece.Team.black, this));
			addPiece(new Pawn(i, 6, Piece.Team.white, this));
		}

		for (int i = 0; i < 2; i++) {
			addPiece(new Rook(i * 7, 0, Piece.Team.black, this));
			addPiece(new Rook(i * 7, 7, Piece.Team.white, this));

			addPiece(new Bishop(i * 3 + 2, 0, Piece.Team.black, this));
			addPiece(new Bishop(i * 3 + 2, 7, Piece.Team.white, this));

			addPiece(new Knight(i * 5 + 1, 0, Piece.Team.black, this));
			addPiece(new Knight(i * 5 + 1, 7, Piece.Team.white, this));
		}

		black_king = new King(4, 0, Piece.Team.black, this);
		addPiece(black_king);
		addPiece(new Queen(3, 0, Piece.Team.black, this));

		white_king = new King(4, 7, Piece.Team.white, this);
		addPiece(white_king);
		addPiece(new Queen(3, 7, Piece.Team.white, this));
	}

	// ===== Getters & Setters ===== \\
	public void setPlayingTeam(Team team) {
		this.playingTeam = team;
	}

	public MinimaxAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setBackgroundColor(Color c) {
		this.backgroundColor = c;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public boolean isPlayingAgainstAI() {
		return playingAgainstAI;
	}

	public void setPlayingAgainstAI(boolean playingAgainstAI) {
		this.playingAgainstAI = playingAgainstAI;
	}
}