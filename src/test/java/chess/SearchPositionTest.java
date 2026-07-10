package chess;

import org.junit.jupiter.api.Test;

import static chess.ChessTestSupport.emptyMatch;
import static chess.ChessTestSupport.move;
import static chess.ChessTestSupport.place;

class SearchPositionTest {
    // Verify if a common move could be applied and undone without modifying the search state.
    @Test
    void commonMove() {
        SearchPosition position = new SearchPosition(new ChessMatch());
        MatchStateAssert.StateSnapshot before = MatchStateAssert.snapshot(position);

        SearchPosition.MoveState state = position.apply(move("e2", "e4"));
        position.undo(state);

        MatchStateAssert.assertSameState(before, position);
    }

    // Verify that a capture is correctly restored on undo.
    @Test
    void capture() {
        ChessMatch match = emptyMatch();
        place(match, "e1", PieceType.KING, Color.WHITE);
        place(match, "e8", PieceType.KING, Color.BLACK);
        place(match, "a1", PieceType.ROOK, Color.WHITE);
        place(match, "a8", PieceType.KNIGHT, Color.BLACK);
        SearchPosition position = new SearchPosition(match);
        MatchStateAssert.StateSnapshot before = MatchStateAssert.snapshot(position);

        SearchPosition.MoveState state = position.apply(move("a1", "a8"));
        position.undo(state);

        MatchStateAssert.assertSameState(before, position);
    }

    // Verify that castling is correctly restored on undo.
    @Test
    void castling() {
        ChessMatch match = emptyMatch();
        place(match, "e1", PieceType.KING, Color.WHITE);
        place(match, "h1", PieceType.ROOK, Color.WHITE);
        place(match, "e8", PieceType.KING, Color.BLACK);
        SearchPosition position = new SearchPosition(match);
        MatchStateAssert.StateSnapshot before = MatchStateAssert.snapshot(position);

        SearchPosition.MoveState state = position.apply(move("e1", "g1"));
        position.undo(state);

        MatchStateAssert.assertSameState(before, position);
    }

    // Verify that en passant is correctly restored on undo.
    @Test
    void enPassant() {
        ChessMatch match = new ChessMatch();
        match.performChessMove(move("e2", "e4"));
        match.performChessMove(move("a7", "a6"));
        match.performChessMove(move("e4", "e5"));
        match.performChessMove(move("d7", "d5"));
        SearchPosition position = new SearchPosition(match);
        MatchStateAssert.StateSnapshot before = MatchStateAssert.snapshot(position);

        SearchPosition.MoveState state = position.apply(move("e5", "d6"));
        position.undo(state);

        MatchStateAssert.assertSameState(before, position);
    }

    // Verify that automatic promotion to queen is correctly restored on undo.
    @Test
    void promotion() {
        ChessMatch match = emptyMatch();
        place(match, "e1", PieceType.KING, Color.WHITE);
        place(match, "h8", PieceType.KING, Color.BLACK);
        place(match, "a7", PieceType.PAWN, Color.WHITE);
        SearchPosition position = new SearchPosition(match);
        MatchStateAssert.StateSnapshot before = MatchStateAssert.snapshot(position);

        SearchPosition.MoveState state = position.apply(move("a7", "a8"));
        position.undo(state);

        MatchStateAssert.assertSameState(before, position);
    }
}
