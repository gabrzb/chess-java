package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;


public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private final Board board;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    private final List<Piece> piecesOnTheBoard = new ArrayList<>();
    private final List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        final int DEFAULT_COLUMNS = 8;
        final int DEFAULT_ROWS = 8;

        turn = 1;
        currentPlayer = Color.WHITE;

        board = new Board(DEFAULT_ROWS, DEFAULT_COLUMNS);
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return !checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessPiece pieceAt(ChessPosition position) {
        return (ChessPiece) board.piece(position.toPosition());
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getCol()][board.getRow()];

        for (int row = 0; row < board.getCol(); row++) {
            for (int col = 0; col < board.getRow(); col++) {
                mat[row][col] = (ChessPiece) board.piece(row, col);
            }
        }

        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position pos = sourcePosition.toPosition();
        validateSourcePosition(pos);
        return board.piece(pos).possibleMoves();
    }

    public List<ChessMove> getLegalMoves(Color color) {
        List<ChessMove> legalMoves = new ArrayList<>();
        boolean previousCheck = check;
        check = testCheck(color);

        List<Piece> pieces = piecesOnTheBoard.stream()
                .filter(x -> ((ChessPiece) x).getColor().equals(color))
                .toList();

        for (Piece piece : pieces) {
            boolean[][] mat = piece.possibleMoves();
            Position source = ((ChessPiece) piece).getChessPosition().toPosition();

            for (int row = 0; row < board.getRow(); row++) {
                for (int col = 0; col < board.getCol(); col++) {
                    if (mat[row][col]) {
                        Position target = new Position(row, col);
                        Piece capturedPiece = makeMove(source, target);
                        boolean leavesKingInCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);

                        if (!leavesKingInCheck) {
                            legalMoves.add(new ChessMove(
                                    ChessPosition.fromPosition(source),
                                    ChessPosition.fromPosition(target)
                            ));
                        }
                    }
                }
            }
        }

        check = previousCheck;
        return legalMoves;
    }

    public boolean isInCheck(Color color) {
        return testCheck(color);
    }

    public ChessPiece performChessMove(ChessMove move) {
        return performChessMove(move.getSource(), move.getTarget());
    }

    public SearchState makeMoveForSearch(ChessMove move) {
        Position source = move.getSource().toPosition();
        Position target = move.getTarget().toPosition();
        Color previousCurrentPlayer = currentPlayer;
        int previousTurn = turn;
        boolean previousCheck = check;
        boolean previousCheckMate = checkMate;
        ChessPiece previousEnPassantVulnerable = enPassantVulnerable;
        ChessPiece previousPromoted = promoted;

        Piece capturedPiece = makeMove(source, target);
        ChessPiece movedPiece = (ChessPiece) board.piece(target);
        ChessPiece promotedPawn = null;
        ChessPiece promotedPiece = null;

        promoted = null;

        if (movedPiece instanceof Pawn) {
            if (
                    movedPiece.getColor() == Color.WHITE && target.getRow() == 0 ||
                            movedPiece.getColor() == Color.BLACK && target.getRow() == 7
            ) {
                promotedPawn = movedPiece;
                promoted = movedPiece;
                promotedPiece = replacePromotedPiece("Q");
            }
        }

        ChessPiece pieceAfterMove = (ChessPiece) board.piece(target);
        Color movedColor = pieceAfterMove.getColor();
        check = testCheck(opponent(movedColor));
        checkMate = testCheckMate(opponent(movedColor));

        if (movedPiece instanceof Pawn &&
                (
                        target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2
                )
        ) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return new SearchState(
                source,
                target,
                capturedPiece,
                previousCurrentPlayer,
                previousTurn,
                previousCheck,
                previousCheckMate,
                previousEnPassantVulnerable,
                previousPromoted,
                promotedPawn,
                promotedPiece
        );
    }

    public void undoMoveForSearch(SearchState state) {
        if (state.promotedPiece != null) {
            Piece p = board.removePiece(state.target);
            piecesOnTheBoard.remove(p);
            board.placePiece(state.promotedPawn, state.target);
            piecesOnTheBoard.add(state.promotedPawn);
        }

        enPassantVulnerable = state.previousEnPassantVulnerable;
        undoMove(state.source, state.target, state.capturedPiece);

        currentPlayer = state.previousCurrentPlayer;
        turn = state.previousTurn;
        check = state.previousCheck;
        checkMate = state.previousCheckMate;
        enPassantVulnerable = state.previousEnPassantVulnerable;
        promoted = state.previousPromoted;
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // special move promotion
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if (
                    movedPiece.getColor() == Color.WHITE && target.getRow() == 0 ||
                            movedPiece.getColor() == Color.BLACK && target.getRow() == 7
            ) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        // special move en passant
        if (movedPiece instanceof Pawn &&
                (
                        target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2
                )
        ) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no promoted piece");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    public static class SearchState {
        private final Position source;
        private final Position target;
        private final Piece capturedPiece;
        private final Color previousCurrentPlayer;
        private final int previousTurn;
        private final boolean previousCheck;
        private final boolean previousCheckMate;
        private final ChessPiece previousEnPassantVulnerable;
        private final ChessPiece previousPromoted;
        private final ChessPiece promotedPawn;
        private final ChessPiece promotedPiece;

        private SearchState(
                Position source,
                Position target,
                Piece capturedPiece,
                Color previousCurrentPlayer,
                int previousTurn,
                boolean previousCheck,
                boolean previousCheckMate,
                ChessPiece previousEnPassantVulnerable,
                ChessPiece previousPromoted,
                ChessPiece promotedPawn,
                ChessPiece promotedPiece
        ) {
            this.source = source;
            this.target = target;
            this.capturedPiece = capturedPiece;
            this.previousCurrentPlayer = previousCurrentPlayer;
            this.previousTurn = previousTurn;
            this.previousCheck = previousCheck;
            this.previousCheckMate = previousCheckMate;
            this.previousEnPassantVulnerable = previousEnPassantVulnerable;
            this.previousPromoted = previousPromoted;
            this.promotedPawn = promotedPawn;
            this.promotedPiece = promotedPiece;
        }
    }

    private ChessPiece newPiece(String type, Color color) {
        return switch (type) {
            case "B" -> new Bishop(board, color);
            case "N" -> new Knight(board, color);
            case "Q" -> new Queen(board, color);
            default -> new Rook(board, color);
        };
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);

        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        // special move castling kingside rook
        if (p instanceof King && target.getCol() == source.getCol() + 2) {
            Position sourceT = new Position(source.getRow(), source.getCol() + 3);
            Position targetT = new Position(target.getRow(), source.getCol() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);

            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // special move castling queenside rook
        if (p instanceof King && target.getCol() == source.getCol() - 2) {
            Position sourceT = new Position(source.getRow(), source.getCol() - 4);
            Position targetT = new Position(target.getRow(), source.getCol() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);

            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // special move en passant
        if (p instanceof Pawn) {
            if (source.getCol() != target.getCol() && capturedPiece == null) {
                Position pawnPosition;

                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getCol());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getCol());
                }

                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        // special move castling kingside rook
        if (p instanceof King && target.getCol() == source.getCol() + 2) {
            Position sourceT = new Position(source.getRow(), source.getCol() + 3);
            Position targetT = new Position(target.getRow(), source.getCol() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);

            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // special move castling queenside rook
        if (p instanceof King && target.getCol() == source.getCol() - 2) {
            Position sourceT = new Position(source.getRow(), source.getCol() - 4);
            Position targetT = new Position(target.getRow(), source.getCol() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);

            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // special move en passant
        if (p instanceof Pawn) {
            if (source.getCol() != target.getCol() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition;

                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getCol());
                } else {
                    pawnPosition = new Position(4, target.getCol());
                }

                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    private void validateSourcePosition(Position pos) {
        if (!board.thereIsAPiece(pos)) {
            throw new ChessException("There is no piece on source position");
        }

        if (!currentPlayer.equals(((ChessPiece)board.piece(pos)).getColor())) {
            throw new  ChessException("The chosen piece is not yours");
        }

        if (!board.piece(pos).isThereAnyPossibleMoves()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = currentPlayer.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color) {
        return color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king (Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor().equals(color)).toList();

        for (Piece piece : list) {
            if (piece instanceof King) {
                return (ChessPiece) piece;
            }
        }

        throw new IllegalStateException("There is no " +  color + " king on the board");
    }

    private boolean testCheck (Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor().equals(opponent(color))).toList();

        for  (Piece piece : opponentPieces) {
            boolean[][] mat = piece.possibleMoves();

            if (mat[kingPosition.getRow()][kingPosition.getCol()]) {
                return true;
            }
        }

        return false;
    }

    private boolean testCheckMate (Color color) {
        if (!testCheck(color)) {
            return false;
        }

        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor().equals(color)).toList();

        for (Piece piece : list) {
            boolean[][] mat = piece.possibleMoves();

            for (int i = 0; i < board.getRow(); i++) {
                for (int j = 0; j < board.getCol(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece)piece).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);

                        undoMove(source, target, capturedPiece);

                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private void placeNewPiece(char col, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(col, row).toPosition());
        piecesOnTheBoard.add(piece);
    }
    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));

        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));

        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }
}
