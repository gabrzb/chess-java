package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    private final int DEFAULT_ROWS = 8;
    private final int DEFAULT_COLUMNS = 8;
    private Board board;

    public ChessMatch() {
        board = new Board(DEFAULT_ROWS, DEFAULT_COLUMNS);
        initialSetup();
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

    private void placeNewPiece(char col, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(col, row).toPosition());
    }
    private void initialSetup() {
        placeNewPiece('b', 6, new Rook(board, Color.WHITE));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
    }
}
