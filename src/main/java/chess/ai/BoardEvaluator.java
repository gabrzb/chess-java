package chess.ai;

import chess.ChessPiece;
import chess.Color;
import chess.GameStateView;
import chess.PieceType;

import java.util.Map;

public class BoardEvaluator {
    private static final Map<PieceType, Integer> PIECE_VALUES = Map.of(
            PieceType.PAWN, 100,
            PieceType.KNIGHT, 320,
            PieceType.BISHOP, 330,
            PieceType.ROOK, 500,
            PieceType.QUEEN, 900,
            PieceType.KING, 0
    );

    public int evaluate(GameStateView match, Color aiColor) {
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

    private int materialScore(GameStateView match, Color aiColor) {
        int score = 0;
        ChessPiece[][] pieces = match.getPieces();

        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                ChessPiece piece = pieces[row][col];

                if (piece != null) {
                    int value = PIECE_VALUES.get(piece.getType()) + centralBonus(row, col);
                    score += piece.getColor() == aiColor ? value : -value;
                }
            }
        }

        return score;
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
