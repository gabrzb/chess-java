package chess;

public class ChessMove {
    private final ChessPosition source;
    private final ChessPosition target;

    public ChessMove(ChessPosition source, ChessPosition target) {
        this.source = source;
        this.target = target;
    }

    public ChessPosition getSource() {
        return source;
    }

    public ChessPosition getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return source + " -> " + target;
    }
}
