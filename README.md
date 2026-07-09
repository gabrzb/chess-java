# Chess Project

A terminal-based chess game written in Java. The project models a complete chess board, validates legal moves, tracks turns and captured pieces, and supports special chess rules such as castling, en passant, pawn promotion, check, and checkmate.

## Features

- Two-player chess match in the console
- Standard 8x8 board with coordinates from `a1` to `h8`
- Legal move validation for all chess pieces
- Highlighting of possible moves after selecting a source piece
- Turn control between white and black players
- Captured pieces display
- Check and checkmate detection
- Special moves:
  - Kingside and queenside castling
  - En passant
  - Pawn promotion to bishop, knight, rook, or queen
- Domain-specific exception handling for invalid board and chess actions

## Tech Stack

- Java 25
- Maven project structure
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
    +-- ChessPiece.java    # Chess-specific piece abstraction
    +-- ChessPosition.java # Chess notation position conversion
    +-- Color.java
    +-- ChessException.java
    +-- pieces
        +-- Bishop.java
        +-- King.java
        +-- Knight.java
        +-- Pawn.java
        +-- Queen.java
        +-- Rook.java
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

## How to Play

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

When a pawn reaches the last rank, choose the promotion piece:

```text
Enter piece for promotion (B/N/R/Q):
```

## Game Rules Implemented

The engine prevents illegal moves, including moves that leave the current player's king in check. A match ends when checkmate is detected.

Implemented rule coverage includes:

- Normal movement for king, queen, rook, bishop, knight, and pawn
- Captures
- Check validation
- Checkmate validation
- Castling restrictions based on king/rook movement and board occupancy
- En passant availability after a two-square pawn advance
- Pawn promotion

## Development Notes

- The repository currently does not include an automated test suite.
- The Maven wrapper files (`mvnw`, `mvnw.cmd`) are not included, so Maven must be installed globally to use Maven commands.
- The console UI uses ANSI escape codes for colors and screen clearing. On some terminals, ANSI support may need to be enabled.

## Verification

The source code was compiled successfully with:

```powershell
javac -d target/classes (Get-ChildItem -Path src/main/java -Recurse -Filter *.java).FullName
```

The application entry point was also launched successfully with:

```powershell
java -cp target/classes application.Main
```
