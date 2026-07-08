package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private Board board;

    public ChessMatch() {
        final int DEFAULT_COLUMNS = 8;
        final int DEFAULT_ROWS = 8;

        turn = 1;
        currentPlayer = Color.WHITE;

        board = new Board(DEFAULT_ROWS, DEFAULT_COLUMNS);
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
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

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position pos = sourcePosition.toPosition();
        validateSourcePosition(pos);
        return board.piece(pos).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);

        nextTurn();

        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);

        board.placePiece(p, target);

        return capturedPiece;
    }

    private void validateSourcePosition(Position pos) {
        if (!board.thereIsAPiece(pos)) {
            throw new ChessException("There is no piece on source position");
        }

        if (!currentPlayer.equals(((ChessPiece)board.piece(pos)).getColor())) {
            throw new  ChessException("The chosen piece is not yours");
        }

        if (!board.piece(pos).isThereAnyPossibleMoves()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = currentPlayer.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void placeNewPiece(char col, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(col, row).toPosition());
    }
    private void initialSetup() {
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new Rook(board, Color.BLACK));
    }
}
