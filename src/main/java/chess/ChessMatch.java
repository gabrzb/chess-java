package chess;

import boardgame.Board;

public class ChessMatch {
    private final int DEFAULT_ROWS = 8;
    private final int DEFAULT_COLUMNS = 8;
    private Board board;

    public ChessMatch() {
        board = new Board(DEFAULT_ROWS, DEFAULT_COLUMNS);
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getCol()][board.getRow()];

        for (int row = 0; row < board.getCol(); row++) {
            for (int col = 0; col < board.getRow(); col++) {
                mat[row][col] = (ChessPiece) board.piece(row, col);
            }
        }

        return mat;
    }
}
