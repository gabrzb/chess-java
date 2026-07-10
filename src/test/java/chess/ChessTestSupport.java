package chess;

public final class ChessTestSupport {
    private ChessTestSupport() {
    }

    public static ChessMove move(String source, String target) {
        return new ChessMove(position(source), position(target));
    }

    public static ChessPosition position(String value) {
        return new ChessPosition(value.charAt(0), Integer.parseInt(value.substring(1)));
    }

    public static ChessMatch emptyMatch() {
        return new ChessMatch(false);
    }

    public static void place(ChessMatch match, String position, PieceType type, Color color) {
        match.placeNewPiece(position.charAt(0), Integer.parseInt(position.substring(1)), type, color);
    }
}
