package ui.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;

import algorithm.MoveMaker;
import chess.Board;
import chess.move.Move;
import chess.move.MoveStatus;
import chess.move.MoveTransition;
import chess.pieces.Piece;
import main.Utils;
import ui.UIUtils;
import ui.interfaces.Clickable;
import ui.listeners.MoveExecutionListener;

public class UIBoardPanel extends UIObject implements Clickable {
	private boolean hovering = false;
	private Board board;
	private int pieceWidth, pieceHeight;
	private Piece selectedPiece;
	private List<Move> selectedPiece_moves;
	private MoveExecutionListener meListener;
	private Color lightColor, darkColor, moveColor, lastMoveColor;
	private Color[] colors;
	private MoveMaker moveMaker;
	private Move lastMove;

	public UIBoardPanel() {
		colors = new Color[64];
		lightColor = Color.white;
		darkColor = Color.lightGray;
		moveColor = Color.orange;
		lastMoveColor = Color.green;
		fillColorArray();
	}

	@Override
	public void render(Graphics g) {
		for (int i = 0; i < 64; i++) {
			int x = Utils.getX(i);
			int y = Utils.getY(i);

			g.setColor(colors[i]);
			g.fillRect(this.x + x * pieceWidth, this.y + y * pieceHeight, pieceWidth, pieceHeight);

			if (board != null) {
				Piece p = board.getPiece(i);
				if (p != null) {
					p.render(g, this.x + x * pieceWidth, this.y + y * pieceHeight, pieceWidth, pieceHeight);
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
		if (!hovering || board == null || moveMaker == null)
			return;

		int piecePosition = Utils.getIndex((e.getX() - this.x) / pieceWidth, (e.getY() - this.y) / pieceHeight);

		Piece clickedPiece = board.getPiece(piecePosition);

		if (clickedPiece != null && clickedPiece.getTeam() == board.getCurrentPlayer().getTeam()) {
			clearMoves();

			selectedPiece = clickedPiece;
			selectedPiece_moves = board.getPossibleMoves(selectedPiece);

			for (Move m : selectedPiece_moves) {
				int pieceDestination = m.getPieceDestination();

				colors[pieceDestination] = UIUtils.mixColors(colors[pieceDestination], moveColor);
			}
		} else {
			boolean moveFound = false;

			if (selectedPiece_moves != null) {
				for (Move m : selectedPiece_moves) {
					if (m.getPieceDestination() == piecePosition) {
						MoveTransition mt = board.getCurrentPlayer().makeMove(m);

						if (mt.getMoveStatus() == MoveStatus.DONE) {
							if (meListener != null)
								meListener.onMoveExecution(mt);

							moveMaker.moveExecuted(mt);
							lastMove = m;

							moveFound = true;

							fillColorArray();
							break;
						}
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
		for (int i = 0; i < 64; i++) {
			if ((Utils.getX(i) + Utils.getY(i)) % 2 == 0)
				colors[i] = lightColor;
			else
				colors[i] = darkColor;
		}

		if (lastMove != null) {
			colors[lastMove.getCurrentPiecePosition()] = UIUtils.mixColors(colors[lastMove.getCurrentPiecePosition()],
					lastMoveColor);
			colors[lastMove.getPieceDestination()] = UIUtils.mixColors(colors[lastMove.getPieceDestination()],
					lastMoveColor);
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

	public Color getLastMoveColor() {
		return lastMoveColor;
	}

	public MoveMaker getMoveMaker() {
		return moveMaker;
	}

	public Move getLastMove() {
		return lastMove;
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

	public void setLastMoveColor(Color lastMoveColor) {
		this.lastMoveColor = lastMoveColor;
	}

	public void setMoveMaker(MoveMaker moveMaker) {
		this.moveMaker = moveMaker;

		if (moveMaker != null)
			setBoard(moveMaker.getBoard());
	}

	public void setLastMove(Move lastMove) {
		this.lastMove = lastMove;
		fillColorArray();
	}
}