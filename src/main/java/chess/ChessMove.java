package chess;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessMove chessMove)) {
            return false;
        }
        return Objects.equals(source, chessMove.source) && Objects.equals(target, chessMove.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }

    @Override
    public String toString() {
        return source + " -> " + target;
    }
}
