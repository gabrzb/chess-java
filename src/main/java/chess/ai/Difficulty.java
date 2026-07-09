package chess.ai;

public enum Difficulty {
    EASY(1),
    MEDIUM(2),
    HARD(3);

    private final int depth;

    Difficulty(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
}
