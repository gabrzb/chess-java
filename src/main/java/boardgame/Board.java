package boardgame;

public class Board {
    private int row;
    private int col;
    private Piece[][] pieces;

    public Board(int row, int col) {
        this.row = row;
        this.col = col;
        pieces = new Piece[row][col];
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Piece piece(int row, int col) {
        return pieces[row][col];
    }

    public Piece piece (Position pos) {
        return pieces[pos.getRow()][pos.getCol()];
    }

    public void placePiece(Piece piece, Position pos) {
        pieces[pos.getRow()][pos.getCol()] = piece;
        piece.position = pos;
    }
}
