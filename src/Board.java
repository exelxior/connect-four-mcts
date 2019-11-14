import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private char[][] board; // Our Connect 4 game board
    private final int numOfColumns; // (7)
    private final int numOfRows;    // number of slots in a column (6)
    private int playerTurn;

    // Board contents
    public static final char EMPTY_SLOT = 'E';
    public static final char RED_MAKER = 'R';
    public static final char YELLOW_MARKER = 'Y';

    // Game states of the connect 4 board at any time
    public static final int IN_PROGRESS = 0;
    public static final int RED_WON = 1;
    public static final int YELLOW_WON = 2;
    public static final int DRAW = 3;

    // Player Turns
    public static final int RED_TURN = 1;
    public static final int YELLOW_TURN = 2;

    // Constructor #1: from inputs
    public Board(int numOfColumns, int numOfRows, int playerTurn) {
        this.numOfColumns = numOfColumns;
        this.numOfRows = numOfRows;
        this.playerTurn = playerTurn;

        // Initializing empty values for a new board
        this.board = new char[numOfColumns][numOfRows];
        for (int col = 0; col < numOfColumns; col++)
            for (int row = 0; row < numOfRows; row++)
                board[col][row] = Board.EMPTY_SLOT;
    }
    // Constructor #2: from inputted 2D array
    public Board(char[][] board, int playerTurn) {
        this.numOfColumns = board.length;
        this.numOfRows = board[0].length;
        this.playerTurn = playerTurn;

        // Copying values over
        this.board = new char[numOfColumns][numOfRows];
        for (int col = 0; col < numOfColumns; col++)
            for (int row = 0; row < numOfRows; row++)
                this.board[col][row] = board[col][row];
    }

    public int getPlayerTurn() { return playerTurn;
    }

    // return indices of columns we can play
    public List<Integer> getLegalMoveColumns() {
        List<Integer> playableColumns = new ArrayList<>();
        for (int col = 0; col < numOfColumns; col++) {
            for (int row = 0; row < numOfRows; row++) {
                if (board[col][row] == Board.EMPTY_SLOT) {
                    playableColumns.add(col);
                    break;
                }
            }
        }
        return playableColumns;
    }

    // a function to check if a player has won using the player's marker
    public boolean checkForWin(char playerMarker) {
        // check vertical
        for (int col = 0; col < numOfColumns - 3; col++)
            for (int row = 0; row < numOfRows; row++)
                for (int i = col; i < col + 4 && board[i][row] == playerMarker; i++)
                    if (i == col + 3) // if 4 same vertical moves
                        return true;

        // check horizontal
        for (int col = 0; col < numOfColumns; col++)
            for (int row = 0; row < numOfRows - 3; row++)
                for (int i = row; i < row + 4 && board[col][i] == playerMarker; i++)
                    if (i == row + 3) // if 4 same horizontal moves
                        return true;

        // check diagonal down right
        for (int col = 0; col < numOfColumns - 3; col++)
            for (int row = 0; row < numOfRows - 3; row++)
                for (int i = 0; i < 4 && board[col+i][row+i] == playerMarker; i++)
                    if (i == 3) // if 4 same down right moves
                        return true;

        // check diagonal down left
        for (int col = 0; col < numOfColumns - 3; col++)
            for (int row = 3; row < numOfRows; row++)
                for (int i = 0; i < 4 && board[col+i][row-i] == playerMarker; i++)
                    if (i == 3) // if 4 same down left moves
                        return true;
        return false;
    }

    // check to see whether the board is full or not
    public boolean isFull() {
        boolean isFull = true;
        for(int col = 0; col < numOfColumns; col++)
            for(int row = 0; row < numOfRows; row++)
                if (board[col][row] == EMPTY_SLOT) {
                    isFull = false;
                    break; // break out on the first empty slot
                }
        return isFull;
    }

    // check if a column on board is empty or not
    public boolean columnIsFull(int moveColumn) {
        boolean columnIsFull = true;
        if (moveColumn >= 0 && moveColumn < numOfColumns) {
            for (int row = 0; row < numOfRows; row++) {
                if (board[moveColumn][row] == Board.EMPTY_SLOT)
                    columnIsFull = false;
            }
        }
        return columnIsFull;
    }

    public boolean dropMarker (int moveColumn) {
        // stop If column is already full
        if (columnIsFull(moveColumn)) return false;

        // else find the lowest unoccupied slot (row number) to drop the marker
        int moveRow = 0; // to match [0-6] index

        while (moveRow < numOfRows && board[moveColumn][moveRow] != EMPTY_SLOT)
            moveRow++;

        if (playerTurn == Board.RED_TURN)
            board[moveColumn][moveRow] = Board.RED_MAKER;
        else
            board[moveColumn][moveRow] = Board.YELLOW_MARKER;

        return true;
    }

    // simple function to switch player turn
    public void alternatePlayerTurn() {
        switch (this.playerTurn) {
            case RED_TURN:
                this.playerTurn = YELLOW_TURN;
                break;
            case YELLOW_TURN:
                this.playerTurn = RED_TURN;
                break;
        }
    }

    // return a copy of the current board
    public Board copy() { return new Board(board, playerTurn); }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\n");

        // line at top
        result.append("  |--");
        result.append("---|--".repeat(Math.max(0, numOfColumns)));

        // to remove last extra character
        result = new StringBuilder(result.substring(0, result.length() - 2) + "\n");

        for (int row = 0; row < numOfRows; row++) {
            int currentRow = numOfRows - 1 - row;
            result.append(currentRow).append(" |  "); // row numbers

            for (int col = 0; col < numOfColumns; col++) { // print slot content
                if (board[col][currentRow] == Board.EMPTY_SLOT) // pretty printing " " for 'E'
                    result.append("   |  ");
                else
                    result.append(board[col][currentRow]).append("  |  ");
            }

            result = new StringBuilder(result.substring(0, result.length() - 1)); // to remove last extra character

            result.append("\n  |--"); // line at bottom
            result.append("---|--".repeat(Math.max(0, numOfColumns)));

            result = new StringBuilder(result.substring(0, result.length() - 2)); // to remove last extra character
            result.append("\n");
        }
        // column numbers
        result.append("     0     1     2     3     4     5     6  ");
        result.append("\n\n");
        return result.substring(0, result.length() - 1);
    }

    // return current game state: draw/playing/win
    public int checkBoardState() {
        return this.checkForWin(RED_MAKER) ? RED_WON
                : this.checkForWin(YELLOW_MARKER) ? YELLOW_WON
                : this.isFull() ? DRAW
                : IN_PROGRESS;
    }

    public void printBoardState() {
        switch (this.checkBoardState()) {
            case RED_WON:
                System.out.println("==> RED Won!");
                break;
            case YELLOW_WON:
                System.out.println("==> YELLOW Won!");
                break;
            case DRAW:
                System.out.println("==> TIE!");
                break;
            case IN_PROGRESS:
                System.out.println("==> GAME IN PROGRESS!");
                break;
        }
    }

    public void printPlayerTurn() {
        switch (playerTurn) {
            case Board.RED_TURN:
                System.out.println("RED PLAYER TURN");
                break;
            case Board.YELLOW_TURN:
                System.out.println("YELLOW PLAYER TURN");
                break;
        }
    }
}
