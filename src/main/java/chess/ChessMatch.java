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

    private void initialSetup() {
        board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
        board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
        board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
    }
}
