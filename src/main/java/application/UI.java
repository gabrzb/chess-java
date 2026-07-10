package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import chess.MatchStatus;
import chess.ai.Difficulty;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {
    public static final String ANSI_RESET   = "\u001B[0m";
    public static final String ANSI_YELLOW  = "\u001B[33m";
    public static final String ANSI_WHITE   = "\u001B[37m";
    public static final String ANSI_BLUE_BACKGROUND   = "\u001B[44m";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine();
            char col = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));

            return new ChessPosition(col, row);
        } catch (Exception ex) {
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8.");
        }
    }

    public static int readGameMode(Scanner sc) {
        while (true) {
            System.out.println("Game mode:");
            System.out.println("1 - Human vs Human");
            System.out.println("2 - Human vs AI");
            System.out.print("Choose: ");
            String option = sc.nextLine();

            if (option.equals("1") || option.equals("2")) {
                return Integer.parseInt(option);
            }

            System.out.println("Invalid option");
        }
    }

    public static Difficulty readDifficulty(Scanner sc) {
        while (true) {
            System.out.println("Difficulty:");
            System.out.println("1 - Easy");
            System.out.println("2 - Medium");
            System.out.println("3 - Hard");
            System.out.print("Choose: ");
            String option = sc.nextLine();

            switch (option) {
                case "1":
                    return Difficulty.EASY;
                case "2":
                    return Difficulty.MEDIUM;
                case "3":
                    return Difficulty.HARD;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static Color readPlayerColor(Scanner sc) {
        while (true) {
            System.out.println("Player color:");
            System.out.println("1 - White");
            System.out.println("2 - Black");
            System.out.print("Choose: ");
            String option = sc.nextLine();

            if (option.equals("1")) {
                return Color.WHITE;
            }
            if (option.equals("2")) {
                return Color.BLACK;
            }

            System.out.println("Invalid option");
        }
    }

    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPiece(captured);
        System.out.println();
        System.out.println("Turn: " + chessMatch.getTurn());

        if (chessMatch.getStatus() == MatchStatus.IN_PROGRESS) {
            System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());

            if (chessMatch.isCheck()) {
                System.out.println("CHECK!");
            }
        } else if (chessMatch.isCheckMate()) {
            System.out.println("CHECKMATE!");
            System.out.println("Winner: " + chessMatch.getCurrentPlayer());
        } else {
            System.out.println("STALEMATE!");
            System.out.println("Draw");
        }
    }

    public static void printBoard(ChessPiece[][] pieces) {
        for (int row = 0; row < pieces.length; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < pieces.length; col++) {
                printPiece(pieces[row][col], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int row = 0; row < pieces.length; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < pieces.length; col++) {
                printPiece(pieces[row][col], possibleMoves[row][col]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece, boolean background) {
        if (background) {
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            System.out.print("-" +  ANSI_RESET);
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            } else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    private static void printCapturedPiece(List<ChessPiece> captured) {
        List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE).toList();
        List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK).toList();

        System.out.println("Captured pieces: ");
        System.out.print("White: ");
        System.out.print(ANSI_WHITE);
        System.out.println(Arrays.toString(white.toArray()));
        System.out.print(ANSI_RESET);

        System.out.print("Black: ");
        System.out.print(ANSI_YELLOW);
        System.out.println(Arrays.toString(black.toArray()));
        System.out.print(ANSI_RESET);
    }
}
