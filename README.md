# Chess Project

A terminal-based chess game written in Java. The project models a complete chess board, validates legal moves, tracks turns and captured pieces, supports special chess rules, and can run human-vs-human or human-vs-AI matches.

## Features

- Two-player chess match in the console
- Single-player chess match against an AI opponent
- Three AI difficulty levels using Minimax with Alpha-Beta pruning
- Standard 8x8 board with coordinates from `a1` to `h8`
- Legal move validation for all chess pieces
- Highlighting of possible moves after selecting a source piece
- Turn control between white and black players
- Captured pieces display
- Check, checkmate, and stalemate detection
- Special moves:
  - Kingside and queenside castling
  - En passant
  - Pawn promotion to bishop, knight, rook, or queen
- Domain-specific exception handling for invalid board and chess actions
- Automated tests for AI search simulation and match status behavior

## Tech Stack

- Java 25
- Maven project structure
- JUnit 5 for automated tests
- Console-based user interface with ANSI colors

## Project Structure

```text
src/main/java
+-- application
|   +-- Main.java          # Application entry point and game loop
|   +-- UI.java            # Console rendering and input handling
+-- boardgame
|   +-- Board.java         # Generic board model
|   +-- BoardException.java
|   +-- Piece.java         # Generic piece abstraction
|   +-- Position.java      # Matrix position representation
+-- chess
    +-- ChessMatch.java    # Chess match rules and state
    +-- ChessMove.java     # Chess move value object
    +-- ChessPiece.java    # Chess-specific piece abstraction
    +-- ChessPosition.java # Chess notation position conversion
    +-- Color.java
    +-- ChessException.java
    +-- GameStateView.java # Read-only view consumed by AI/evaluation
    +-- MatchStatus.java   # IN_PROGRESS, CHECKMATE, STALEMATE
    +-- PieceType.java     # KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
    +-- SearchPosition.java # Isolated search copy with apply/undo
    +-- player
    |   +-- MoveStrategy.java # Strategy abstraction for move selection
    +-- ai
    |   +-- AlphaBetaSearch.java # Minimax and Alpha-Beta pruning
    |   +-- BoardEvaluator.java  # AI board scoring
    |   +-- ChessAI.java         # Compatibility wrapper for MinimaxAI
    |   +-- Difficulty.java      # AI difficulty levels
    |   +-- MinimaxAI.java       # AI player strategy
    +-- pieces
        +-- Bishop.java
        +-- King.java
        +-- Knight.java
        +-- Pawn.java
        +-- Queen.java
        +-- Rook.java
```

Tests live under:

```text
src/test/java
+-- chess
|   +-- ChessMatchStatusTest.java
|   +-- SearchPositionTest.java
|   +-- MatchStateAssert.java
|   +-- ChessTestSupport.java
+-- chess/ai
    +-- AlphaBetaSearchTest.java
    +-- MinimaxAITest.java
```

## Requirements

- JDK 25 or newer
- Maven, if you want to build through `pom.xml`

This project is configured with:

```xml
<maven.compiler.source>25</maven.compiler.source>
<maven.compiler.target>25</maven.compiler.target>
```

## How to Run

### Option 1: Compile and run with Java tools

From the project root:

```powershell
javac -d target/classes (Get-ChildItem -Path src/main/java -Recurse -Filter *.java).FullName
java -cp target/classes application.Main
```

### Option 2: Build with Maven

If Maven is installed:

```bash
mvn clean package
java -cp target/classes application.Main
```

## How to Test

If Maven is installed:

```bash
mvn clean test
```

The test suite covers:

- AI move selection without mutating the original `ChessMatch`
- `SearchPosition.apply(...)` and `undo(...)` for normal moves, captures, castling, en passant, and promotion
- Checkmate and stalemate status behavior
- Terminal Alpha-Beta scoring for checkmate and stalemate
- Legal move selection across all difficulty levels
- White AI opening move selection

## How to Play

At startup, choose the game mode:

```text
Game mode:
1 - Human vs Human
2 - Human vs AI
```

In AI mode, choose the difficulty and your color:

```text
Difficulty:
1 - Easy
2 - Medium
3 - Hard

Player color:
1 - White
2 - Black
```

AI difficulty levels:

- Easy: depth 1 search, with some random choice among similarly scored moves
- Medium: depth 2 search, deterministic best move
- Hard: depth 3 search, with basic move ordering for captures, promotions, and checks

The game asks for a source position and then a target position.

Example opening move:

```text
Source: e2
Target: e4
```

After choosing a source piece, the board is redrawn with possible target squares highlighted.

Board coordinates follow standard chess notation:

- Columns: `a` to `h`
- Rows: `1` to `8`
- White pieces start at rows `1` and `2`
- Black pieces start at rows `7` and `8`

Piece symbols:

| Symbol | Piece  |
|--------|--------|
| `K`    | King   |
| `Q`    | Queen  |
| `R`    | Rook   |
| `B`    | Bishop |
| `N`    | Knight |
| `P`    | Pawn   |

When a human pawn reaches the last rank, choose the promotion piece:

```text
Enter piece for promotion (B/N/R/Q):
```

AI pawn promotion is automatic and always promotes to queen.

## Game Rules Implemented

The engine prevents illegal moves, including moves that leave the current player's king in check. A match ends when checkmate or stalemate is detected.

Implemented rule coverage includes:

- Normal movement for king, queen, rook, bishop, knight, and pawn
- Captures
- Check validation
- Checkmate validation
- Stalemate validation
- Castling restrictions based on king/rook movement and board occupancy
- En passant availability after a two-square pawn advance
- Pawn promotion
- AI legal move generation reuses the same match rules as human moves

## AI Architecture

The AI is split into small responsibilities:

- `MoveStrategy` is the player abstraction used by the application loop.
- `MinimaxAI` stores the AI color and difficulty, then starts the search.
- `AlphaBetaSearch` contains Minimax, Alpha-Beta pruning, terminal scoring, and move ordering.
- `SearchPosition` creates an isolated copy of `ChessMatch` for search simulation, so `chooseMove(...)` does not modify the real match.
- `BoardEvaluator` evaluates a `GameStateView` and uses `PieceType` values instead of concrete piece-class checks.
- `ChessAI` remains as a deprecated compatibility wrapper around `MinimaxAI`.

The easy AI keeps a small random policy among similarly scored moves. Medium and hard choose the best scored move, with hard also enabling move ordering.

## Developer Notes

- The Maven wrapper files (`mvnw`, `mvnw.cmd`) are not included, so Maven must be installed globally to use Maven commands.
- The console UI uses ANSI escape codes for colors and screen clearing. On some terminals, ANSI support may need to be enabled.
- The AI code lives under `chess.ai`; board primitives and piece movement rules stay outside that package.
- Rollback operations used by AI search are package-private and are accessed through `SearchPosition`, not through the normal public game API.

## Verification

The source code was compiled successfully with:

```powershell
javac -d target/classes (Get-ChildItem -Path src/main/java -Recurse -Filter *.java).FullName
```

The application entry point can be launched with:

```powershell
java -cp target/classes application.Main
```

`mvn clean test` requires Maven to be installed and available on `PATH`.
