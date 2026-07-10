package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;
import chess.PieceType;

public class Bishop extends ChessPiece {

    public Bishop(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "B";
    }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRow()][getBoard().getCol()];

        Position p = new Position(0, 0);

        // northwest
        p.setValues(position.getRow() - 1, position.getCol() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
            p.setValues(p.getRow() - 1, p.getCol() - 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // northeast
        p.setValues(position.getRow() - 1, position.getCol() + 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
            p.setValues(p.getRow() - 1, p.getCol() + 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // southwest
        p.setValues(position.getRow() + 1, position.getCol() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
            p.setValues(p.getRow() + 1, p.getCol() - 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // southeast
        p.setValues(position.getRow() + 1, position.getCol() + 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
            p.setValues(p.getRow() + 1,  p.getCol() + 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        return mat;
    }
}
