package chess.ai;

import chess.ChessMatch;
import chess.Color;
import chess.PieceType;
import chess.SearchPosition;
import org.junit.jupiter.api.Test;

import static chess.ChessTestSupport.emptyMatch;
import static chess.ChessTestSupport.move;
import static chess.ChessTestSupport.place;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlphaBetaSearchTest {
    private final AlphaBetaSearch search = new AlphaBetaSearch(new BoardEvaluator());

    // Verifica que xeque-mate recebe pontuacao terminal favoravel.
    @Test
    void checkmateScore() {
        ChessMatch match = new ChessMatch();
        match.performChessMove(move("f2", "f3"));
        match.performChessMove(move("e7", "e5"));
        match.performChessMove(move("g2", "g4"));
        match.performChessMove(move("d8", "h4"));

        int score = search.score(new SearchPosition(match), Color.BLACK, Color.WHITE, 1, true);

        assertTrue(score >= AlphaBetaSearch.CHECKMATE_SCORE);
    }

    // Verifica que afogamento recebe pontuacao neutra.
    @Test
    void stalemateScore() {
        ChessMatch match = emptyMatch();
        place(match, "e8", PieceType.KING, Color.BLACK);
        place(match, "e6", PieceType.KING, Color.WHITE);
        place(match, "d6", PieceType.QUEEN, Color.WHITE);

        int score = search.score(new SearchPosition(match), Color.WHITE, Color.BLACK, 1, true);

        assertEquals(0, score);
    }
}
