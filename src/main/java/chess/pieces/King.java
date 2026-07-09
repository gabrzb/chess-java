package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    private final ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean canMove(Position pos) {
        ChessPiece p = (ChessPiece) getBoard().piece(pos);
        return p == null || p.getColor() != getColor();
    }

    private boolean testRookCastling(Position pos) {
        ChessPiece p = (ChessPiece) getBoard().piece(pos);
        return p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRow()][getBoard().getCol()];

        Position p = new Position(0, 0);

        // above
        p.setValues(position.getRow() - 1, position.getCol());
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // left
        p.setValues(position.getRow(), position.getCol() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // right
        p.setValues(position.getRow(), position.getCol() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // below
        p.setValues(position.getRow() + 1, position.getCol());
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // northwest
        p.setValues(position.getRow() - 1, position.getCol() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // northeast
        p.setValues(position.getRow() - 1, position.getCol() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // southwest
        p.setValues(position.getRow() + 1, position.getCol() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // southeast
        p.setValues(position.getRow() + 1, position.getCol() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // special move castling
        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            // castling kingside rook
            Position posT1 = new Position(position.getRow(), position.getCol() + 3);
            if (testRookCastling(posT1)) {
                Position p1 = new Position(position.getRow(), position.getCol() + 1);
                Position p2 = new Position(position.getRow(), position.getCol() + 2);

                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
                    mat[position.getRow()][position.getCol() + 2] = true;
                }
            }
            // castling queenside rook
            Position posT2 = new Position(position.getRow(), position.getCol() - 4);
            if (testRookCastling(posT2)) {
                Position p1 = new Position(position.getRow(), position.getCol() - 1);
                Position p2 = new Position(position.getRow(), position.getCol() - 2);
                Position p3 = new Position(position.getRow(), position.getCol() - 3);

                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
                    mat[position.getRow()][position.getCol() - 2] = true;
                }
            }

        }

        return mat;
    }
}
