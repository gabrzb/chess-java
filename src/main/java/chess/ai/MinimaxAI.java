package chess.ai;

import chess.ChessMatch;
import chess.ChessMove;
import chess.Color;
import chess.GameStateView;
import chess.SearchPosition;
import chess.player.MoveStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinimaxAI implements MoveStrategy {
    private static final int EASY_SCORE_MARGIN = 25;

    private final Color color;
    private final Difficulty difficulty;
    private final AlphaBetaSearch search;
    private final Random random = new Random();

    public MinimaxAI(Color color, Difficulty difficulty) {
        this(color, difficulty, new AlphaBetaSearch(new BoardEvaluator()));
    }

    MinimaxAI(Color color, Difficulty difficulty, AlphaBetaSearch search) {
        this.color = color;
        this.difficulty = difficulty;
        this.search = search;
    }

    @Override
    public ChessMove chooseMove(GameStateView state) {
        SearchPosition position = searchPosition(state);
        List<AlphaBetaSearch.ScoredMove> scoredMoves = search.rankMoves(
                position,
                color,
                difficulty.getDepth(),
                difficulty == Difficulty.HARD
        );

        if (scoredMoves.isEmpty()) {
            return null;
        }

        int bestScore = Integer.MIN_VALUE;
        List<ChessMove> bestMoves = new ArrayList<>();

        for (AlphaBetaSearch.ScoredMove scoredMove : scoredMoves) {
            if (scoredMove.score() > bestScore) {
                bestScore = scoredMove.score();
                bestMoves.clear();
                bestMoves.add(scoredMove.move());
            } else if (difficulty == Difficulty.EASY && scoredMove.score() >= bestScore - EASY_SCORE_MARGIN) {
                bestMoves.add(scoredMove.move());
            }
        }

        if (difficulty == Difficulty.EASY) {
            return bestMoves.get(random.nextInt(bestMoves.size()));
        }

        return bestMoves.get(0);
    }

    private SearchPosition searchPosition(GameStateView state) {
        if (state instanceof ChessMatch match) {
            return new SearchPosition(match);
        }
        if (state instanceof SearchPosition position) {
            return position.copy();
        }
        throw new IllegalArgumentException("Unsupported game state implementation: " + state.getClass().getName());
    }
}
