package chess;

import org.junit.jupiter.api.Test;

import static chess.ChessTestSupport.emptyMatch;
import static chess.ChessTestSupport.move;
import static chess.ChessTestSupport.place;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChessMatchStatusTest {
    // Check if isCheckMate returns true if there's a checkmate.
    @Test
    void checkmate() {
        ChessMatch match = new ChessMatch();

        assertFalse(match.isCheckMate());

        match.performChessMove(move("f2", "f3"));
        match.performChessMove(move("e7", "e5"));
        match.performChessMove(move("g2", "g4"));
        match.performChessMove(move("d8", "h4"));

        assertTrue(match.isCheckMate());
        assertEquals(MatchStatus.CHECKMATE, match.getStatus());
    }

    // Check if isStalemate returns true if there's a stalemate.
    @Test
    void stalemate() {
        ChessMatch match = emptyMatch();
        place(match, "e8", PieceType.KING, Color.BLACK);
        place(match, "e6", PieceType.KING, Color.WHITE);
        place(match, "d5", PieceType.QUEEN, Color.WHITE);

        match.performChessMove(move("d5", "d6"));

        assertFalse(match.isCheckMate());
        assertTrue(match.isStalemate());
        assertEquals(MatchStatus.STALEMATE, match.getStatus());
    }
}
