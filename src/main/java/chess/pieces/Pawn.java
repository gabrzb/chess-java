package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    public Pawn(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRow()][getBoard().getCol()];
        Position p = new Position(0, 0);

        if (getColor().equals(Color.WHITE)) {
            // 1 up
            p.setValues(position.getRow() - 1, position.getCol());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getCol()] = true;
            }

            // 2 up
            p.setValues(position.getRow() - 2, position.getCol());
            Position p2 = new Position(position.getRow() - 1, position.getCol());
            if (
                    getBoard().positionExists(p) &&
                            !getBoard().thereIsAPiece(p) &&
                            getBoard().positionExists(p2) &&
                            !getBoard().thereIsAPiece(p2) &&
                            getMoveCount() == 0
            ) {
                mat[p.getRow()][p.getCol()] = true;
            }

            // diagonal left
            p.setValues(position.getRow() - 1, position.getCol() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getCol()] = true;
            }

            // diagonal right
            p.setValues(position.getRow() - 1, position.getCol() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getCol()] = true;
            }

        } else {
            // 1 down
            p.setValues(position.getRow() + 1, position.getCol());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getCol()] = true;
            }

            // 2 down
            p.setValues(position.getRow() + 2, position.getCol());
            Position p2 = new Position(position.getRow() + 1, position.getCol());
            if (
                    getBoard().positionExists(p) &&
                            !getBoard().thereIsAPiece(p) &&
                            getBoard().positionExists(p2) &&
                            !getBoard().thereIsAPiece(p2) &&
                            getMoveCount() == 0
            ) {
                mat[p.getRow()][p.getCol()] = true;
            }

            // diagonal left
            p.setValues(position.getRow() + 1, position.getCol() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getCol()] = true;
            }

            // diagonal right
            p.setValues(position.getRow() + 1, position.getCol() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getCol()] = true;
            }
        }

        return mat;
    }

    public String toString () {
        return "P";
    }

}
