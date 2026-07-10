package chess;

import boardgame.Position;

import java.util.Objects;

public class ChessPosition {
    private final char col;
    private final int row;

    public ChessPosition(char col, int row) {
        if (col < 'a' || col > 'h' || row < 1 || row > 8) {
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
        }

        this.col = col;
        this.row = row;
    }

    public char getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    protected Position toPosition() {
        return new Position(8 - row, col - 'a');
    }

    protected static ChessPosition fromPosition(Position pos) {
        return new ChessPosition((char) ('a' + pos.getCol()), 8 - pos.getRow());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPosition that)) {
            return false;
        }
        return col == that.col && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public String toString() {
        return "" + col + row;
    }
}
