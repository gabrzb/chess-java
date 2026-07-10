package chess.ai;

import chess.Color;

@Deprecated(since = "1.0", forRemoval = false)
public class ChessAI extends MinimaxAI {
    public ChessAI(Color color, Difficulty difficulty) {
        super(color, difficulty);
    }
}
