package chess;

import boardgame.Position;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MatchStateAssert {
    private MatchStateAssert() {
    }

    public static void assertSameState(Object expected, Object actual) {
        assertEquals(snapshot(expected), snapshot(actual));
    }

    public static void assertSameState(StateSnapshot expected, Object actual) {
        assertEquals(expected, snapshot(actual));
    }

    public static StateSnapshot snapshot(Object state) {
        ChessMatch match = unwrapMatch(state);
        List<String> boardPieces = new ArrayList<>();
        ChessPiece[][] pieces = match.getPieces();

        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                ChessPiece piece = pieces[row][col];
                if (piece != null) {
                    boardPieces.add(row + "," + col + ":" + describe(piece));
                }
            }
        }

        return new StateSnapshot(
                match.getTurn(),
                match.getCurrentPlayer(),
                match.isCheck(),
                match.isCheckMate(),
                match.getStatus(),
                describe(match.getEnPassantVulnerable()),
                describe(match.getPromoted()),
                boardPieces,
                pieceList(match, "piecesOnTheBoard"),
                pieceList(match, "capturedPieces")
        );
    }

    private static ChessMatch unwrapMatch(Object state) {
        if (state instanceof ChessMatch match) {
            return match;
        }
        if (state instanceof SearchPosition position) {
            return (ChessMatch) field(position, "match");
        }
        throw new IllegalArgumentException("Unsupported state: " + state.getClass());
    }

    private static List<String> pieceList(ChessMatch match, String fieldName) {
        @SuppressWarnings("unchecked")
        List<Object> pieces = (List<Object>) field(match, fieldName);
        return pieces.stream()
                .map(piece -> describe((ChessPiece) piece))
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    private static String describe(ChessPiece piece) {
        if (piece == null) {
            return null;
        }

        Position boardPosition = (Position) fieldInHierarchy(piece, "position");
        String positionText = boardPosition == null ? "-" : ChessPosition.fromPosition(boardPosition).toString();
        return piece.getType() + ":" + piece.getColor() + ":" + piece.getMoveCount() + ":" + positionText;
    }

    private static Object field(Object target, String name) {
        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(target);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static Object fieldInHierarchy(Object target, String name) {
        Class<?> type = target.getClass();

        while (type != null) {
            try {
                Field field = type.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }

        throw new AssertionError("Field not found: " + name);
    }

    public record StateSnapshot(
            int turn,
            Color currentPlayer,
            boolean check,
            boolean checkMate,
            MatchStatus status,
            String enPassantVulnerable,
            String promoted,
            List<String> boardPieces,
            List<String> piecesOnTheBoard,
            List<String> capturedPieces
    ) {
    }
}
