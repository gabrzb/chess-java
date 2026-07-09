package chess.ai;

import chess.ChessMatch;
import chess.ChessMove;
import chess.ChessPiece;
import chess.Color;
import chess.pieces.Pawn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ChessAI {
    private static final int CHECKMATE_SCORE = 100000;
    private static final int EASY_SCORE_MARGIN = 25;

    private final Color color;
    private final Difficulty difficulty;
    private final BoardEvaluator evaluator = new BoardEvaluator();
    private final Random random = new Random();

    public ChessAI(Color color, Difficulty difficulty) {
        this.color = color;
        this.difficulty = difficulty;
    }

    public ChessMove chooseMove(ChessMatch match) {
        List<ChessMove> legalMoves = match.getLegalMoves(color);
        List<ChessMove> moves = difficulty == Difficulty.HARD ? orderedMoves(match, legalMoves) : legalMoves;

        if (moves.isEmpty()) {
            return null;
        }

        int bestScore = Integer.MIN_VALUE;
        List<ChessMove> bestMoves = new ArrayList<>();

        for (ChessMove move : moves) {
            ChessMatch.SearchState state = match.makeMoveForSearch(move);
            int score = alphaBeta(
                    match,
                    difficulty.getDepth() - 1,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    opponent(color)
            );
            match.undoMoveForSearch(state);

            if (score > bestScore) {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (score >= bestScore - EASY_SCORE_MARGIN) {
                bestMoves.add(move);
            }
        }

        if (difficulty == Difficulty.EASY) {
            return bestMoves.get(random.nextInt(bestMoves.size()));
        }

        return bestMoves.get(0);
    }

    private int alphaBeta(ChessMatch match, int depth, int alpha, int beta, Color player) {
        List<ChessMove> moves = match.getLegalMoves(player);

        if (depth == 0 || moves.isEmpty()) {
            return terminalOrEvaluation(match, player, depth, moves);
        }

        List<ChessMove> orderedMoves = difficulty == Difficulty.HARD ? orderedMoves(match, moves) : moves;

        if (player == color) {
            int maxEval = Integer.MIN_VALUE;

            for (ChessMove move : orderedMoves) {
                ChessMatch.SearchState state = match.makeMoveForSearch(move);
                int eval = alphaBeta(match, depth - 1, alpha, beta, opponent(player));
                match.undoMoveForSearch(state);

                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) {
                    break;
                }
            }

            return maxEval;
        }

        int minEval = Integer.MAX_VALUE;

        for (ChessMove move : orderedMoves) {
            ChessMatch.SearchState state = match.makeMoveForSearch(move);
            int eval = alphaBeta(match, depth - 1, alpha, beta, opponent(player));
            match.undoMoveForSearch(state);

            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);

            if (beta <= alpha) {
                break;
            }
        }

        return minEval;
    }

    private int terminalOrEvaluation(ChessMatch match, Color player, int depth, List<ChessMove> moves) {
        if (moves.isEmpty() && match.isInCheck(player)) {
            return player == color ? -CHECKMATE_SCORE - depth : CHECKMATE_SCORE + depth;
        }

        return evaluator.evaluate(match, color);
    }

    private List<ChessMove> orderedMoves(ChessMatch match, List<ChessMove> moves) {
        return moves.stream()
                .sorted(Comparator.comparingInt((ChessMove move) -> movePriority(match, move)).reversed())
                .toList();
    }

    private int movePriority(ChessMatch match, ChessMove move) {
        int priority = 0;
        ChessPiece sourcePiece = match.pieceAt(move.getSource());

        if (isCapture(match, move)) {
            priority += 1000;
        }
        if (isPromotion(match, move)) {
            priority += 800;
        }

        ChessMatch.SearchState state = match.makeMoveForSearch(move);
        if (sourcePiece != null && match.isInCheck(opponent(sourcePiece.getColor()))) {
            priority += 300;
        }
        match.undoMoveForSearch(state);

        return priority;
    }

    private boolean isCapture(ChessMatch match, ChessMove move) {
        ChessPiece sourcePiece = match.pieceAt(move.getSource());
        ChessPiece targetPiece = match.pieceAt(move.getTarget());
        return sourcePiece != null && targetPiece != null && targetPiece.getColor() != sourcePiece.getColor();
    }

    private boolean isPromotion(ChessMatch match, ChessMove move) {
        ChessPiece sourcePiece = match.pieceAt(move.getSource());

        if (!(sourcePiece instanceof Pawn)) {
            return false;
        }

        return sourcePiece.getColor() == Color.WHITE && move.getTarget().getRow() == 8 ||
                sourcePiece.getColor() == Color.BLACK && move.getTarget().getRow() == 1;
    }

    private Color opponent(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
}
