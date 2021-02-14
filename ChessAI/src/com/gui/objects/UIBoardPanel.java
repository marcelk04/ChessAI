package com.gui.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;

import com.chess.Board;
import com.chess.ai.MoveMaker;
import com.chess.move.Move;
import com.chess.move.Move.PawnPromotion;
import com.chess.move.MoveStatus;
import com.chess.move.MoveTransition;
import com.chess.pieces.Piece;
import com.chess.pieces.Piece.PieceType;
import com.gui.UIUtils;
import com.gui.display.Display;
import com.gui.interfaces.Clickable;
import com.gui.listeners.MoveExecutionListener;
import com.main.PawnPromotionGUI;
import com.main.Utils;

public class UIBoardPanel extends UIObject implements Clickable {
	public static final Color STANDARD_LIGHT_COLOR = Color.white, STANDARD_DARK_COLOR = Color.lightGray,
			STANDARD_MOVE_COLOR = Color.orange, STANDARD_LAST_MOVE_COLOR = Color.green;

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
	private Display display;

	public UIBoardPanel(Display display) {
		this.display = display;
		colors = new Color[64];
		lightColor = STANDARD_LIGHT_COLOR;
		darkColor = STANDARD_DARK_COLOR;
		moveColor = STANDARD_MOVE_COLOR;
		lastMoveColor = STANDARD_LAST_MOVE_COLOR;
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
		Thread t = new Thread(() -> {
			if (!hovering || !enabled || board == null || moveMaker == null)
				return;

			int clickPosition = Utils.getIndex((e.getX() - x) / pieceWidth, (e.getY() - y) / pieceHeight);

			Piece clickedPiece = board.getPiece(clickPosition);

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

				if (selectedPiece != null) {
					List<Move> possibleMoves = board.findMoves(selectedPiece, clickPosition);

					if (possibleMoves != null) {
						Move moveToExecute = null;

						if (possibleMoves.size() == 1) {
							moveToExecute = possibleMoves.get(0);
						} else if (possibleMoves.size() == 4) {
							PieceType requestedPiece = PawnPromotionGUI
									.getPieceInput(board.getCurrentPlayer().getTeam(), display);

							if (requestedPiece != null) {
								for (Move m : possibleMoves) {
									if (((PawnPromotion) m).getPromotionPiece().getType() == requestedPiece) {
										moveToExecute = m;
										break;
									}
								}
							}
						}

						if (moveToExecute != null) {
							MoveTransition mt = board.getCurrentPlayer().makeMove(moveToExecute);

							if (mt.getMoveStatus() == MoveStatus.DONE) {
								if (meListener != null)
									meListener.onMoveExecution(mt);

								moveMaker.moveExecuted(mt);
								lastMove = moveToExecute;
								moveFound = true;

								fillColorArray();
							}
						}
					}
				}

				if (!moveFound)
					clearMoves();
			}

			repaint();
		});
		t.start();
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

		propertyChanged();
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