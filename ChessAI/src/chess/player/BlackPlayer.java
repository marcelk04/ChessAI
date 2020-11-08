package chess.player;

import java.util.Collection;
import java.util.Set;

import chess.Board;
import chess.move.Move;
import chess.pieces.Piece;
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
}