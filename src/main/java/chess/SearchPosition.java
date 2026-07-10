package chess;

import java.util.List;

public class SearchPosition implements GameStateView {
    private final ChessMatch match;

    public SearchPosition(ChessMatch match) {
        this.match = new ChessMatch(match);
    }

    public SearchPosition(SearchPosition position) {
        this.match = new ChessMatch(position.match);
    }

    public MoveState apply(ChessMove move) {
        return new MoveState(match.makeMoveForSearch(move));
    }

    public void undo(MoveState state) {
        match.undoMoveForSearch(state.state);
    }

    public SearchPosition copy() {
        return new SearchPosition(this);
    }

    public Color getCurrentPlayer() {
        return match.getCurrentPlayer();
    }

    public MatchStatus getStatus() {
        return match.getStatus();
    }

    public boolean isCheck() {
        return match.isCheck();
    }

    public boolean isCheckMate() {
        return match.isCheckMate();
    }

    public boolean isStalemate() {
        return match.isStalemate();
    }

    @Override
    public List<ChessMove> getLegalMoves(Color color) {
        return match.getLegalMoves(color);
    }

    @Override
    public boolean isInCheck(Color color) {
        return match.isInCheck(color);
    }

    @Override
    public ChessPiece pieceAt(ChessPosition position) {
        return match.pieceAt(position);
    }

    @Override
    public ChessPiece[][] getPieces() {
        return match.getPieces();
    }

    public static class MoveState {
        private final ChessMatch.SearchState state;

        private MoveState(ChessMatch.SearchState state) {
            this.state = state;
        }
    }
}
