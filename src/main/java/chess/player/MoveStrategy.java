package chess.player;

import chess.ChessMove;
import chess.GameStateView;

public interface MoveStrategy {
    ChessMove chooseMove(GameStateView state);
}
