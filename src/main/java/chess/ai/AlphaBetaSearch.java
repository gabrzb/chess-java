package chess.ai;

import chess.ChessMove;
import chess.ChessPiece;
import chess.Color;
import chess.PieceType;
import chess.SearchPosition;

import java.util.Comparator;
import java.util.List;

public class AlphaBetaSearch {
    public static final int CHECKMATE_SCORE = 100000;

    private final BoardEvaluator evaluator;

    public AlphaBetaSearch(BoardEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public List<ScoredMove> rankMoves(SearchPosition position, Color aiColor, int depth, boolean orderMoves) {
        List<ChessMove> legalMoves = position.getLegalMoves(aiColor);
        List<ChessMove> moves = orderMoves ? orderedMoves(position, legalMoves) : legalMoves;

        return moves.stream()
                .map(move -> new ScoredMove(move, scoreMove(position, move, aiColor, depth, orderMoves)))
                .toList();
    }

    public int score(SearchPosition position, Color aiColor, Color player, int depth, boolean orderMoves) {
        return alphaBeta(position, aiColor, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, player, orderMoves);
    }

    private int scoreMove(SearchPosition position, ChessMove move, Color aiColor, int depth, boolean orderMoves) {
        SearchPosition.MoveState state = position.apply(move);
        int score = alphaBeta(
                position,
                aiColor,
                depth - 1,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                opponent(aiColor),
                orderMoves
        );
        position.undo(state);
        return score;
    }

    private int alphaBeta(SearchPosition position, Color aiColor, int depth, int alpha, int beta, Color player, boolean orderMoves) {
        List<ChessMove> legalMoves = position.getLegalMoves(player);

        if (depth == 0 || legalMoves.isEmpty()) {
            return terminalOrEvaluation(position, aiColor, player, depth, legalMoves);
        }

        List<ChessMove> moves = orderMoves ? orderedMoves(position, legalMoves) : legalMoves;

        if (player == aiColor) {
            int maxEval = Integer.MIN_VALUE;

            for (ChessMove move : moves) {
                SearchPosition.MoveState state = position.apply(move);
                int eval = alphaBeta(position, aiColor, depth - 1, alpha, beta, opponent(player), orderMoves);
                position.undo(state);

                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) {
                    break;
                }
            }

            return maxEval;
        }

        int minEval = Integer.MAX_VALUE;

        for (ChessMove move : moves) {
            SearchPosition.MoveState state = position.apply(move);
            int eval = alphaBeta(position, aiColor, depth - 1, alpha, beta, opponent(player), orderMoves);
            position.undo(state);

            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);

            if (beta <= alpha) {
                break;
            }
        }

        return minEval;
    }

    private int terminalOrEvaluation(SearchPosition position, Color aiColor, Color player, int depth, List<ChessMove> moves) {
        if (moves.isEmpty()) {
            if (position.isInCheck(player)) {
                return player == aiColor ? -CHECKMATE_SCORE - depth : CHECKMATE_SCORE + depth;
            }
            return 0;
        }

        return evaluator.evaluate(position, aiColor);
    }

    private List<ChessMove> orderedMoves(SearchPosition position, List<ChessMove> moves) {
        return moves.stream()
                .sorted(Comparator.comparingInt((ChessMove move) -> movePriority(position, move)).reversed())
                .toList();
    }

    private int movePriority(SearchPosition position, ChessMove move) {
        int priority = 0;
        ChessPiece sourcePiece = position.pieceAt(move.getSource());

        if (isCapture(position, move)) {
            priority += 1000;
        }
        if (isPromotion(position, move)) {
            priority += 800;
        }

        SearchPosition.MoveState state = position.apply(move);
        if (sourcePiece != null && position.isInCheck(opponent(sourcePiece.getColor()))) {
            priority += 300;
        }
        position.undo(state);

        return priority;
    }

    private boolean isCapture(SearchPosition position, ChessMove move) {
        ChessPiece sourcePiece = position.pieceAt(move.getSource());
        ChessPiece targetPiece = position.pieceAt(move.getTarget());
        return sourcePiece != null && targetPiece != null && targetPiece.getColor() != sourcePiece.getColor();
    }

    private boolean isPromotion(SearchPosition position, ChessMove move) {
        ChessPiece sourcePiece = position.pieceAt(move.getSource());

        if (sourcePiece == null || sourcePiece.getType() != PieceType.PAWN) {
            return false;
        }

        return sourcePiece.getColor() == Color.WHITE && move.getTarget().getRow() == 8 ||
                sourcePiece.getColor() == Color.BLACK && move.getTarget().getRow() == 1;
    }

    private Color opponent(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public record ScoredMove(ChessMove move, int score) {
    }
}
