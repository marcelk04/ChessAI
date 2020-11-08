package chess.player;

import java.util.Collection;
import java.util.Set;

import chess.Board;
import chess.move.Move;
import chess.pieces.Piece;
import chess.pieces.Team;

public class WhitePlayer extends Player{
	public WhitePlayer(Board board, Set<Move> playerLegals, Set<Move> opponentLegals) {
		super(board, playerLegals, opponentLegals);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return board.getWhitePieces();
	}

	@Override
	public Team getTeam() {
		return Team.white;
	}

	@Override
	public Player getOpponent() {
		return board.getBlackPlayer();
	}
}