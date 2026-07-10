package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import chess.ai.Difficulty;
import chess.ai.MinimaxAI;
import chess.MatchStatus;
import chess.player.MoveStrategy;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    void main () {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();
        int gameMode = UI.readGameMode(sc);
        boolean againstAi = gameMode == 2;
        Color humanColor = Color.WHITE;
        MoveStrategy ai = null;
        ChessMove lastAiMove = null;

        if (againstAi) {
            Difficulty difficulty = UI.readDifficulty(sc);
            humanColor = UI.readPlayerColor(sc);
            Color aiColor = humanColor == Color.WHITE ? Color.BLACK : Color.WHITE;
            ai = new MinimaxAI(aiColor, difficulty);
        }

        while (chessMatch.getStatus() == MatchStatus.IN_PROGRESS) {
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);

                if (lastAiMove != null) {
                    System.out.println("AI move: " + lastAiMove);
                }

                System.out.println();

                if (againstAi && chessMatch.getCurrentPlayer() != humanColor) {
                    System.out.println("AI is thinking...");
                    ChessMove aiMove = ai.chooseMove(chessMatch);

                    if (aiMove == null) {
                        break;
                    }

                    ChessPiece capturedPiece = chessMatch.performChessMove(aiMove);

                    if (capturedPiece != null) {
                        captured.add(capturedPiece);
                    }

                    lastAiMove = aiMove;
                    continue;
                }

                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);
                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

                if (capturedPiece != null) {
                    captured.add(capturedPiece);
                }

                lastAiMove = null;

                if (chessMatch.getPromoted() != null) {
                    promoteHumanPawn(sc, chessMatch);
                }
            } catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                System.out.print("Press enter to continue...");
                sc.nextLine();
            }
        }

        UI.clearScreen();
        UI.printMatch(chessMatch, captured);
    }

    private void promoteHumanPawn(Scanner sc, ChessMatch chessMatch) {
        while (true) {
            System.out.print("Enter piece for promotion (B/N/R/Q): ");
            String type = sc.nextLine().toUpperCase();

            if (type.equals("B") || type.equals("N") || type.equals("R") || type.equals("Q")) {
                chessMatch.replacePromotedPiece(type);
                break;
            } else {
                System.out.println("Invalid promotion type");
            }
        }
    }
}
