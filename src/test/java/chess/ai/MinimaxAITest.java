package chess.ai;

import chess.ChessMatch;
import chess.ChessMove;
import chess.Color;
import chess.MatchStateAssert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinimaxAITest {
    // Verifica que escolher uma jogada nao altera a partida original.
    @Test
    void noMutation() {
        ChessMatch match = new ChessMatch();
        MatchStateAssert.StateSnapshot before = MatchStateAssert.snapshot(match);

        new MinimaxAI(Color.WHITE, Difficulty.MEDIUM).chooseMove(match);

        MatchStateAssert.assertSameState(before, match);
    }

    // Verifica que a IA retorna apenas jogadas legais.
    @Test
    void legalMove() {
        ChessMatch match = new ChessMatch();
        ChessMove move = new MinimaxAI(Color.BLACK, Difficulty.MEDIUM).chooseMove(match);

        assertNotNull(move);
        assertTrue(match.getLegalMoves(Color.BLACK).contains(move));
    }

    // Verifica que todos os niveis de dificuldade conseguem escolher uma jogada.
    @Test
    void difficulties() {
        ChessMatch match = new ChessMatch();

        for (Difficulty difficulty : Difficulty.values()) {
            ChessMove move = new MinimaxAI(Color.WHITE, difficulty).chooseMove(match);

            assertNotNull(move, difficulty.name());
            assertTrue(match.getLegalMoves(Color.WHITE).contains(move), difficulty.name());
        }
    }

    // Verifica que a IA jogando de brancas consegue escolher o primeiro lance.
    @Test
    void whiteOpening() {
        ChessMatch match = new ChessMatch();
        ChessMove move = new MinimaxAI(Color.WHITE, Difficulty.EASY).chooseMove(match);

        assertNotNull(move);
        assertTrue(match.getLegalMoves(Color.WHITE).contains(move));
    }
}
