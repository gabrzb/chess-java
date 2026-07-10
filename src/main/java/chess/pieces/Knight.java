package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;
import chess.PieceType;

public class Knight extends ChessPiece {
    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String  toString() {
        return "N";
    }

    @Override
    public PieceType getType() {
        return PieceType.KNIGHT;
    }

    private boolean canMove(Position pos) {
        ChessPiece p = (ChessPiece) getBoard().piece(pos);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRow()][getBoard().getCol()];

        Position p = new Position(0, 0);

        // above -
        p.setValues(position.getRow() - 1, position.getCol() - 2);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // left -
        p.setValues(position.getRow() - 2, position.getCol() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // right -
        p.setValues(position.getRow() - 2, position.getCol() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // below -
        p.setValues(position.getRow() - 1, position.getCol() + 2);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // northwest -
        p.setValues(position.getRow() + 1, position.getCol() + 2);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // northeast -
        p.setValues(position.getRow() + 2, position.getCol() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // southwest -
        p.setValues(position.getRow() + 2, position.getCol() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // southeast
        p.setValues(position.getRow() + 1, position.getCol() - 2);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        return mat;
    }
}
