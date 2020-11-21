package ui.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Set;

import chess.Board;
import chess.move.Move;
import chess.move.MoveTransition;
import chess.pieces.Piece;
import ui.interfaces.Clickable;
import ui.listeners.MoveExecutionListener;

public class UIBoardPanel extends UIObject implements Clickable {
	private boolean hovering = false;
	private Board board;
	private int pieceWidth, pieceHeight;
	private Piece selectedPiece;
	private Set<Move> selectedPiece_moves;
	private MoveExecutionListener meListener;
	private Color lightColor, darkColor, moveColor;
	private Color[][] colors;

	public UIBoardPanel() {
		colors = new Color[8][8];
		lightColor = Color.white;
		darkColor = Color.lightGray;
		moveColor = Color.yellow;
		fillColorArray();
	}

	@Override
	public void render(Graphics g) {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				g.setColor(colors[x][y]);
				g.fillRect(this.x + x * pieceWidth, this.y + y * pieceHeight, pieceWidth, pieceHeight);

				if (board != null) {
					Piece p = board.getPiece(x, y);
					if (p != null) {
						p.render(g, this.x + x * pieceWidth, this.y + y * pieceHeight, pieceWidth, pieceHeight);
					}
				}
			}
		}

		if (border != null) {
			g.setColor(border);
			g.drawRect(x, y, width, height);
		}
	}

	@Override
	public void onMouseMove(MouseEvent e) {
		if (boundsContain(e.getX(), e.getY()))
			hovering = true;
		else
			hovering = false;
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		if (!hovering || board == null)
			return;

		int clickedX = e.getX();
		int clickedY = e.getY();

		int pieceX = (clickedX - this.x) / pieceWidth;
		int pieceY = (clickedY - this.y) / pieceHeight;

		Piece clickedPiece = board.getPiece(pieceX, pieceY);

		if (clickedPiece != null && clickedPiece.getTeam() == board.getCurrentPlayer().getTeam()) {
			clearMoves();

			selectedPiece = clickedPiece;
			selectedPiece_moves = selectedPiece.getMoves(board);

			for (Move m : selectedPiece_moves) {
				int destinationX = m.getPieceDestinationX();
				int destinationY = m.getPieceDestinationY();

				Color oldColor = colors[destinationX][destinationY];
				Color newColor = new Color((oldColor.getRed() + moveColor.getRed()) / 2,
						(oldColor.getGreen() + moveColor.getGreen()) / 2,
						(oldColor.getBlue() + moveColor.getBlue()) / 2);

				colors[destinationX][destinationY] = newColor;
			}
		} else {
			boolean moveFound = false;

			if (selectedPiece_moves != null) {
				for (Move m : selectedPiece_moves) {
					if (m.getPieceDestinationX() == pieceX && m.getPieceDestinationY() == pieceY) {
						MoveTransition mt = board.getCurrentPlayer().makeMove(m);

						if (meListener != null)
							meListener.onMoveExecution(mt);

						moveFound = true;
						break;
					}
				}
			}

			if (!moveFound)
				clearMoves();
		}
	}

	private void clearMoves() {
		selectedPiece = null;
		selectedPiece_moves = null;
	
		fillColorArray();
	}

	private void fillColorArray() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if ((x + y) % 2 == 0) {
					colors[x][y] = lightColor;
				} else {
					colors[x][y] = darkColor;
				}
			}
		}
	}

	// ===== Getters ===== \\
	public Board getBoard() {
		return board;
	}

	public MoveExecutionListener getMoveExecutionListener() {
		return meListener;
	}

	public Color getLightColor() {
		return lightColor;
	}

	public Color getDarkColor() {
		return darkColor;
	}

	public Color getMoveColor() {
		return moveColor;
	}

	// ===== Setters ===== \\
	@Override
	public void setBounds(int x, int y, int width, int height) {
		pieceWidth = width / 8;
		pieceHeight = height / 8;

		super.setBounds(x, y, width, height);
	}

	public void setBoard(Board board) {
		this.board = board;

		clearMoves();
	}

	public void setMoveExecutionListener(MoveExecutionListener meListener) {
		this.meListener = meListener;
	}

	public void setLightColor(Color lightColor) {
		this.lightColor = lightColor;
		clearMoves();
	}

	public void setDarkColor(Color darkColor) {
		this.darkColor = darkColor;
		clearMoves();
	}

	public void setMoveColor(Color moveColor) {
		this.moveColor = moveColor;
		clearMoves();
	}
}