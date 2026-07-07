package boardgame;

public class Board {
    private int row;
    private int col;
    private Piece[][] pieces;

    public Board(int row, int col) {
        if (row < 1 || col < 1) {
            throw new BoardException("Error creating Board: there must be at least 1 row and 1 column");
        }

        this.row = row;
        this.col = col;
        pieces = new Piece[row][col];
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece piece(int row, int col) {
        if (!positionExists(row, col)) {
            throw new BoardException("Position not on the board");
        }

        return pieces[row][col];
    }

    public Piece piece (Position pos) {
        if (!positionExists(pos)) {
            throw new BoardException("Position not on the board");
        }

        return pieces[pos.getRow()][pos.getCol()];
    }

    public void placePiece(Piece piece, Position pos) {
        if (thereIsAPiece(pos)) {
            throw new BoardException("There is already a piece on position " + pos);
        }

        pieces[pos.getRow()][pos.getCol()] = piece;
        piece.position = pos;
    }

    public Piece removePiece(Position pos) {
        if (!positionExists(pos)) {
            throw new BoardException("Position not on the board");
        }

        if (piece(pos) == null) {
            return null;
        }

        Piece aux = piece(pos);
        aux.position = null;
        pieces[pos.getRow()][pos.getCol()] = null;
        return aux;
    }

    private boolean positionExists(int row, int col) {
        return row >= 0 && row < this.row && col >= 0 && col < this.col;
    }

    public boolean positionExists(Position pos) {
        return positionExists(pos.getRow(), pos.getCol());
    }

    public boolean thereIsAPiece(Position pos) {
        if (!positionExists(pos)) {
            throw new BoardException("Position not on the board");
        }

        return piece(pos) != null;
    }
}
