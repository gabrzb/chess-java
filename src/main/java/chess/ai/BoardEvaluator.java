package chess.ai;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;
import chess.pieces.Bishop;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class BoardEvaluator {
    public int evaluate(ChessMatch match, Color aiColor) {
        Color opponent = opponent(aiColor);
        int score = materialScore(match, aiColor);
        score += 5 * (match.getLegalMoves(aiColor).size() - match.getLegalMoves(opponent).size());

        if (match.isInCheck(opponent)) {
            score += 40;
        }
        if (match.isInCheck(aiColor)) {
            score -= 40;
        }

        return score;
    }

    private int materialScore(ChessMatch match, Color aiColor) {
        int score = 0;
        ChessPiece[][] pieces = match.getPieces();

        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                ChessPiece piece = pieces[row][col];

                if (piece != null) {
                    int value = pieceValue(piece) + centralBonus(row, col);
                    score += piece.getColor() == aiColor ? value : -value;
                }
            }
        }

        return score;
    }

    private int pieceValue(ChessPiece piece) {
        if (piece instanceof Pawn) {
            return 100;
        }
        if (piece instanceof Knight) {
            return 320;
        }
        if (piece instanceof Bishop) {
            return 330;
        }
        if (piece instanceof Rook) {
            return 500;
        }
        if (piece instanceof Queen) {
            return 900;
        }
        return 0;
    }

    private int centralBonus(int row, int col) {
        if (row >= 3 && row <= 4 && col >= 3 && col <= 4) {
            return 20;
        }
        if (row >= 2 && row <= 5 && col >= 2 && col <= 5) {
            return 10;
        }
        return 0;
    }

    private Color opponent(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
}
