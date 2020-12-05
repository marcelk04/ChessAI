package chess.player;

import java.util.ArrayList;
import java.util.List;

import chess.Board;
import chess.move.Move;
import chess.move.Move.KingSideCastleMove;
import chess.move.Move.QueenSideCastleMove;
import chess.pieces.Piece;
import chess.pieces.PieceType;
import chess.pieces.Rook;
import chess.pieces.Team;

public class WhitePlayer extends Player {
	public WhitePlayer(Board board, List<Move> playerLegals, List<Move> opponentLegals) {
		super(board, playerLegals, opponentLegals);
	}

	@Override
	public List<Piece> getActivePieces() {
		return board.getWhitePieces();
	}

	@Override
	public Team getTeam() {
		return Team.WHITE;
	}

	@Override
	public Player getOpponent() {
		return board.getBlackPlayer();
	}

	@Override
	protected List<Move> calculateKingCastles(List<Move> opponentLegals) {
		final List<Move> kingCastles = new ArrayList<Move>();

		if (canCastle() && !playerKing.gotMovedAtLeastOnce() && playerKing.getX() == 4 && playerKing.getY() == 7
				&& !kingInCheck) {
			Piece castleRook;

			// king side castles
			if (board.getPiece(5, 7) == null && board.getPiece(6, 7) == null) {
				castleRook = board.getPiece(7, 7);
				if (castleRook != null && castleRook.getType() == PieceType.ROOK && !castleRook.gotMovedAtLeastOnce()
						&& calculateAttacksOnTile(5, 7, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(6, 7, opponentLegals).isEmpty()) {
					kingCastles.add(new KingSideCastleMove(board, playerKing, 6, 7, (Rook) castleRook, 5, 7));
				}
			}

			// queen side castles
			if (board.getPiece(1, 7) == null && board.getPiece(2, 7) == null && board.getPiece(3, 7) == null) {
				castleRook = board.getPiece(0, 7);
				if (castleRook != null && castleRook.getType() == PieceType.ROOK && !castleRook.gotMovedAtLeastOnce()
						&& calculateAttacksOnTile(2, 7, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(3, 7, opponentLegals).isEmpty()) {
					kingCastles.add(new QueenSideCastleMove(board, playerKing, 2, 7, (Rook) castleRook, 3, 7));
				}
			}
		}

		return kingCastles;
	}
}