package chess.player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.move.Move;
import chess.move.Move.KingSideCastleMove;
import chess.move.Move.QueenSideCastleMove;
import chess.pieces.Piece;
import chess.pieces.PieceType;
import chess.pieces.Rook;
import chess.pieces.Team;

public class BlackPlayer extends Player {
	public BlackPlayer(Board board, Set<Move> playerLegals, Set<Move> opponentLegals) {
		super(board, playerLegals, opponentLegals);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return board.getBlackPieces();
	}

	@Override
	public Team getTeam() {
		return Team.black;
	}

	@Override
	public Player getOpponent() {
		return board.getWhitePlayer();
	}

	@Override
	protected Set<Move> calculateKingCastles(Set<Move> opponentLegals) {
		final Set<Move> kingCastles = new HashSet<Move>();

		if (canCastle() && !playerKing.gotMovedAtLeastOnce() && playerKing.getX() == 4 && playerKing.getY() == 0
				&& !kingInCheck) {
			Piece castleRook;

			// king side castles
			if (board.getPiece(5, 0) == null && board.getPiece(6, 0) == null) {
				castleRook = board.getPiece(7, 0);
				if (castleRook != null && castleRook.getType() == PieceType.ROOK && !castleRook.gotMovedAtLeastOnce()
						&& calculateAttacksOnTile(5, 0, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(6, 0, opponentLegals).isEmpty()) {
					kingCastles.add(new KingSideCastleMove(board, playerKing, 6, 0, (Rook) castleRook, 5, 0));
				}
			}

			// queen side castles
			if (board.getPiece(1, 0) == null && board.getPiece(2, 0) == null && board.getPiece(3, 0) == null) {
				castleRook = board.getPiece(0, 0);
				if (castleRook != null && castleRook.getType() == PieceType.ROOK && !castleRook.gotMovedAtLeastOnce()
						&& calculateAttacksOnTile(2, 0, opponentLegals).isEmpty()
						&& calculateAttacksOnTile(3, 0, opponentLegals).isEmpty()) {
					kingCastles.add(new QueenSideCastleMove(board, playerKing, 2, 0, (Rook) castleRook, 3, 0));
				}
			}
		}

		return kingCastles;
	}
}