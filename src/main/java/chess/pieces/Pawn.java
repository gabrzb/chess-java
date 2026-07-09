package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    private final ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString () {
        return "P";
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

            // special move en passant
            if (position.getRow() == 3) {
                Position left = new Position(position.getRow(), position.getCol() - 1);

                if (
                        getBoard().positionExists(left) &&
                                isThereOpponentPiece(left) &&
                                getBoard().piece(left) == chessMatch.getEnPassantVulnerable()
                ) {
                    mat[position.getRow() - 1][left.getCol()] = true;
                }

                Position right = new Position(position.getRow(), position.getCol() + 1);

                if (
                        getBoard().positionExists(right) &&
                                isThereOpponentPiece(right) &&
                                getBoard().piece(right) == chessMatch.getEnPassantVulnerable()
                ) {
                    mat[position.getRow() - 1][right.getCol()] = true;
                }
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

            // special move en passant
            if (position.getRow() == 4) {
                Position left = new Position(position.getRow(), position.getCol() - 1);

                if (
                        getBoard().positionExists(left) &&
                                isThereOpponentPiece(left) &&
                                getBoard().piece(left) == chessMatch.getEnPassantVulnerable()
                ) {
                    mat[position.getRow() + 1][left.getCol()] = true;
                }

                Position right = new Position(position.getRow(), position.getCol() + 1);

                if (
                        getBoard().positionExists(right) &&
                                isThereOpponentPiece(right) &&
                                getBoard().piece(right) == chessMatch.getEnPassantVulnerable()
                ) {
                    mat[position.getRow() + 1][right.getCol()] = true;
                }
            }
        }

        return mat;
    }
}
