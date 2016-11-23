package board;

import java.util.LinkedList;

import static board.Move.MoveDirection.*;

public class GameState {

    /**
     * board positions, represented by the last 49 digits of a binary long
     * board position = 64 - NumberOrLeadingZeros(positionAsLong)
     * the '1' in '00000100' would be at position 3
     *
     *  A   B   C   D   E   F   G
     *
     *  49  48  47  46  45  44  43    1
     *  42  41  40  39  38  37  36    2
     *  35  34  33  32  31  30  29    3
     *  28  27  26  25  24  23  22    4
     *  21  20  19  18  17  16  15    5
     *  14  13  12  11  10   9   8    6
     *   7   6   5   4   3   2   1    7
     *
     *
     *
     * Number of all bitboards.
     * 0: all bullets
     * 1: red bullets
     * 2: white bullets
     * 3: black bullets
     */
    static final int NUMBER_OF_BITMAPS = 4;

    static final int NUMBER_OF_FIELDS = 49;

    /**
     * 2 for white, 3 for black
     */
    public int activePlayer;

    /**
     * number of the current turn; starts with 1
     */
    public int turn = 0;


    /**
     *  Number of the player who won the game, -1 until there is no winner.
     */

    public int gameWinner = -1;


    /**
     * All bitboards represented as array(long).
     */
    public long[] bitmaps;

    /**
     * captured red bullets
     */
    public int capturedBulletsWhite;
    public int capturedBulletsBlack;

    /**
     * the last move that has been performed
     */

    public Move lastMove;

    /**
     * Constructor.
     */
    public GameState(int activePlayer) {
        bitmaps = new long[NUMBER_OF_BITMAPS];
        this.activePlayer = activePlayer;
        capturedBulletsBlack = 0;
        capturedBulletsWhite = 0;
    }

    /**
     * copy current gameState
     * @return the copy
     */
    public GameState copy() {
        GameState newState = new GameState(activePlayer);
        System.arraycopy(this.bitmaps, 0, newState.bitmaps, 0, bitmaps.length);
        newState.capturedBulletsBlack = this.capturedBulletsBlack;
        newState.capturedBulletsWhite = this.capturedBulletsWhite;
        newState.turn = this.turn;
        return newState;
    }

    /**
     * Execute a move on a gameState.
     *
     * @param move the move to be executed.
     * @return new gameState after the move is performed.
     */

    public GameState executeMove(Move move) {
        GameState newState = this.copy();
        newState.turn++;
        // switch the player after each move (will get switched back if the move was a capture move)
        newState.switchPlayer();

        // color of the player that performed the move
        int bulletColor = getColorAtPosition(move.positionFrom);

        // delete bullet from start position
        newState.bitmaps[bulletColor] ^= move.positionFrom;
        newState.bitmaps[0] ^= move.positionFrom;

        int colorOfCurrentBullet;
        int colorOfLastBullet = bulletColor;

        long currentPosition = move.positionTo;
        move.affectedBullets = 0;
        while(true) {
            // counts how many bullets have been moved at this turn
            move.affectedBullets++;
            // get the color of the bullet we're looking at
            colorOfCurrentBullet = getColorAtPosition(currentPosition);

            // remove the bullet that was at the position we're looking at (if it had one)
            if (colorOfCurrentBullet > 0) {
                newState.bitmaps[colorOfCurrentBullet] = newState.bitmaps[colorOfCurrentBullet] ^= currentPosition;
            } else {
                newState.bitmaps[0] = newState.bitmaps[0] |= currentPosition;
            }

            // place the bullet at at the position we're looking at
            newState.bitmaps[colorOfLastBullet] |= currentPosition;

            if (isOnEdge(currentPosition, move.direction)) {
                if (colorOfCurrentBullet == 1) {
                    newState.redBulletCaptured(bulletColor);
                }
                if (colorOfCurrentBullet >= 1) {
                    move.affectedBullets++;
                    newState.switchPlayer();
                }
                break;
            } else if (colorOfCurrentBullet == 0) {
                break;
            }
            currentPosition = shift(currentPosition, move.direction);
            colorOfLastBullet = colorOfCurrentBullet;
        }
        // Check win conditions (7 red bullets captured or enemy doesn't have bullets on the board remaining).
        if (newState.capturedBulletsBlack > 6 || newState.capturedBulletsWhite > 6 || newState.bitmaps[2] == 0  || newState.bitmaps[3] == 0) {
            newState.gameWinner = ((newState.capturedBulletsBlack > 6 || newState.bitmaps[2] == 0) ? 3 : 2);
        }
        newState.lastMove = move;
        return newState;
    }

    private long shift(long currentPosition, Move.MoveDirection direction) {
        switch (direction) {
            case UP: return currentPosition << 7;
            case DOWN: return currentPosition >> 7;
            case LEFT: return currentPosition << 1;
            case RIGHT: return currentPosition >> 1;
        }
        return 0x0L;
    }

    /**
     * switches the current active player
     */
    private void switchPlayer() {
        activePlayer = (activePlayer == 2 ? 3 : 2);
    }

    /**
     *
     * @param color the color of the player who captured the red bullet
     */
    private void redBulletCaptured(int color) {
        switch (color) {
            case 2: this.capturedBulletsWhite++;break;
            case 3: this.capturedBulletsBlack++;break;
        }
    }

    /**
     * @param color 2 for white, 3 for black
     * @return all possible moves for the current player
     */
    public LinkedList<Move> getAllMoves(int color) {
        LinkedList<Move> moves = new LinkedList<>();
        long bitboard = bitmaps[color];
        long position = 1;
        while (bitboard != 0) {
            if ((bitboard & 0x1L) != 0) {
                moves.addAll(possibleMoves(position, color));
            }
            bitboard >>= 1;
            position <<= 1;
        }
        return moves;
    }

    /**
     * returns a list of all possible moves for one bullet
     * returns an empty list if no moves are possible
     *
     * @param position the position of the bullet
     * @param color    color of the bullet (2: white, 3: black)
     * @return LinkedList of all possible moves
     */
    // TODO: check repetitive moves; TODO: junit tests schreiben
    // TODO: eigene kugeln werden runtergeschmissen; fixed, checken; scheint nicht zu gehen; TODO: junit tests schreiben
    private LinkedList<Move> possibleMoves(long position, int color) {
        LinkedList<Move> moves = new LinkedList<>();
        // check up
        //  check if field in the opposite direction is empty
        if ((position & (bitmaps[0] << 7)) == 0 && noRepetitiveMove(position, UP)) {
            // find the last bullet that would been moved
            long currentPosition = position;
            int lastColor = 0;
            while (getColorAtPosition(currentPosition) != 0) {
                lastColor = getColorAtPosition(currentPosition);
                currentPosition <<= 7;
            }
            // check if the move would throw a bullet over the edge
            if (Long.numberOfLeadingZeros(currentPosition) <= 15){
                if (lastColor != color) {
                    moves.add(new Move(position, UP, true));
                }
            } else {
                moves.add(new Move(position, UP));
            }
        }
        // check down
        //  check if field in the opposite direction is empty
        if ((position & (bitmaps[0] >> 7)) == 0 && noRepetitiveMove(position, DOWN)) {
            // find the last bullet that would been moved
            long currentPosition = position;
            int lastColor = 0;
            while (getColorAtPosition(currentPosition) != 0) {
                lastColor = getColorAtPosition(currentPosition);
                currentPosition >>= 7;
            }
            if (currentPosition == 0){
                if (lastColor != color) {
                    moves.add(new Move(position, DOWN, true));
                }
            } else {
                moves.add(new Move(position, DOWN));
            }
        }
        // check left
        //  check if field in the opposite direction is empty
        if ((isOnEdge(position, RIGHT) || (bitmaps[0] & (position >> 1)) == 0) && noRepetitiveMove(position, LEFT)) {
            // find the last bullet that would been moved
            long currentPosition = position;
            while (!isOnEdge(currentPosition, LEFT) && getColorAtPosition(currentPosition) != 0) {
                currentPosition <<= 1;
            }
            if (getColorAtPosition(currentPosition) == 0) {
                moves.add(new Move(position, LEFT));
            } else if (isOnEdge(currentPosition, LEFT) && getColorAtPosition(currentPosition) != color) {
                moves.add(new Move(position, LEFT, true));
            }
        }
        // check right
        //  check if field in the opposite direction is empty
        if ((isOnEdge(position, LEFT) || (bitmaps[0] & (position << 1)) == 0) && noRepetitiveMove(position, RIGHT)) {
            // find the last bullet that would been moved
            long currentPosition = position;
            while (!isOnEdge(currentPosition, RIGHT) && getColorAtPosition(currentPosition) != 0) {
                currentPosition >>= 1;
            }
            if (getColorAtPosition(currentPosition) == 0) {
                moves.add(new Move(position, RIGHT));
            } else if (isOnEdge(currentPosition, RIGHT) && getColorAtPosition(currentPosition) != color) {
                moves.add(new Move(position, RIGHT, true));
            }
        }
        return moves;
    }

    /**
     * check if a move is repetitive
     *
     * @param position position from which the possible move will start
     * @param direction direction of the possible move
     * @return true if the move is not repetitive
     * */

    public boolean noRepetitiveMove(long position, Move.MoveDirection direction) {
        if (lastMove == null) {
            return true;
        }
        switch(direction){
            case UP: return !((position << (7 * lastMove.affectedBullets) == lastMove.positionFrom) && lastMove.direction == DOWN);
            case DOWN: return !((position >> (7 * lastMove.affectedBullets) == lastMove.positionFrom) && lastMove.direction == UP);
            case LEFT: return !((position << lastMove.affectedBullets == lastMove.positionFrom) && lastMove.direction == RIGHT);
            case RIGHT: return !((position >> lastMove.affectedBullets == lastMove.positionFrom) && lastMove.direction == LEFT);
        }
        return false;
    }

    /**
     *
     * @param position position to check the color
     * @return the color of the bullet at the give position, or 0 if there is no bullet at that position.
     */
    private int getColorAtPosition(long position) {
        for (int i = 1; i < NUMBER_OF_BITMAPS; i++) {
            if ((position & bitmaps[i]) != 0) {
                return i;
            }
        }
        return 0;
    }

    /**
     * check if a position is on a specific outer row
     *
     * @param position position
     * @param direction 'UP' stands for the top row, and so on
     * @return true if the position is on the edge of the board
     */
    public static boolean isOnEdge(long position, Move.MoveDirection direction) {
        switch(direction) {
            case UP: return (Long.numberOfLeadingZeros(position) > 14 && Long.numberOfLeadingZeros(position) < 22);
            case DOWN: return (Long.numberOfLeadingZeros(position) > 57);
            case LEFT: return ((Long.numberOfLeadingZeros(position) - 15) % 7 == 0);
            case RIGHT: return ((Long.numberOfLeadingZeros(position) - 15) % 7 == 6);
        }
        return false;
    }

    /**
     * Check if a bullet of a give color is on the give position.
     *
     * @param position position
     * @param color 2 for white, 3 for black
     * @return true if the position has a bullet of the given color on it
     */
    boolean isOnPosition(long position, int color) {
        return (position & bitmaps[color]) != 0;
    }

    /**
     * Prints the current board to the console.
     */
    public void printField() {
        System.out.println("");
        System.out.println("-------------");
        char[][] board = Parser.bitboardsToArray(this);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("-------------");
        System.out.println("");
    }

    /**
     * Prints current stats to the console.
     */
    public void printStats() {
        System.out.println("");
        System.out.println("capturedBulletsWhite = " + capturedBulletsWhite);
        System.out.println("capturedBulletsBlack = " + capturedBulletsBlack);
        System.out.println("activePlayer = " + activePlayer);
        System.out.println("gameWinner = " + gameWinner);
        if (lastMove != null) {
            System.out.println("lastMove = " + lastMove.toString());
        }
        System.out.println("");
    }
}
