package application;

import chess.ChessPiece;

public class UI {
    public static void printBoard(ChessPiece[][] pieces) {
        for (int row = 0; row < pieces.length; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < pieces.length; col++) {
                printPiece(pieces[row][col]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece) {
        if (piece != null) {
            System.out.print(piece);
        } else {
            System.out.print("-");
        }
        System.out.print(" ");
    }
}
