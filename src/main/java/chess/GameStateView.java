package chess;

import java.util.List;

public interface GameStateView {
    List<ChessMove> getLegalMoves(Color color);

    boolean isInCheck(Color color);

    ChessPiece pieceAt(ChessPosition position);

    ChessPiece[][] getPieces();
}
