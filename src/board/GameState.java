package board;

import java.util.LinkedList;

import static board.Move.MoveDirection.*;

/**
 * Created by Juviro on 12.08.2016.
 */
public class GameState {

    /**
     * board positions, represented by the last 49 digits of a binary long
     * board position = 64 - NumberOrLeadingZeros(positionAsLong)
     * the '1' in '00000100' would be at position 3
     *
     *  A   B   C   D   E   F   G
     *
     *  49  48  47  46  45  44  43    7
     *  42  41  40  39  38  37  36    6
     *  35  34  33  32  31  30  29    5
     *  28  27  26  25  24  23  22    4
     *  21  20  19  18  17  16  15    3
     *  14  13  12  11  10   9   8    2
     *   7   6   5   4   3   2   1    1
     */


    /**
     * Number of all bitboards.
     * 0: all bullets
     * 1: red bullets
     * 2: white bullets
     * 3: black bullets
     */
    public static final int NUMBER_OF_BITMAPS = 4;

    public static final int NUMBER_OF_FIELDS = 49;


    public static final int PLAYER_WHITE = 2;

    public static final int PLAYER_BLACK = 3;


    public int activePlayer;


    /**
     *  true if one player has captured at least 7 bullets
     */

    public int gameWinner = -1;


    /**
     * All bitboards representad as array(long).
     */
    long[] bitmaps;

    /**
     * captured red bullets
     */
    public int capturedBulletsWhite = 0;
    public int capturedBulletsBlack = 0;

    /**
     * Constructor.
     */
    public GameState(long[] bitmaps, int startingPlayer) {
        this.bitmaps = bitmaps;
        this.activePlayer = startingPlayer;
    }

    /**
     * copy current gameState
     * @return the copy
     */
    public GameState copy() {
        final GameState newState = new GameState(bitmaps, activePlayer);
        return newState;
    }

    /**
     * execute a move on a gameState
     *
     * @param move
     * @return new gameState after the move is performed
     */

    public GameState performMove(Move move) {
        GameState newState = this.copy();
        // after a move, the player switches. If the move was a capture move, the player will switch back
        switchPlayer();
        int bulletColor = getColorAtPosition(move.getPositionFrom());

        // delete bullet from old position
        newState.bitmaps[bulletColor] ^= move.getPositionFrom();
        newState.bitmaps[0] ^= move.getPositionFrom();

        // move all other effected bullets
        long currentPosition = move.getPositionTo();
        // the color of the bullet we looked at at the iteration before
        int colorOfLastBullet = bulletColor;
        int colorOfCurrentBullet;

        // iterate over every field that may be affected, starting with the field positionTo
        while(colorOfLastBullet != 0) {
            // get the color of the bullet we're looking at
            colorOfCurrentBullet = getColorAtPosition(currentPosition);

            // remove the bullet that was at the position we're looking at
            newState.bitmaps[colorOfCurrentBullet] = newState.bitmaps[colorOfCurrentBullet] ^= currentPosition;

            // place the bullet at at the position we're looking at
            newState.bitmaps[colorOfLastBullet] = newState.bitmaps[colorOfLastBullet] |= currentPosition;
            newState.bitmaps[0] = newState.bitmaps[0] |= currentPosition;

            if (move.getDirection() == UP) {
                // check if the bullet we're looking at will remain on the board
                if (Long.numberOfLeadingZeros(currentPosition) < (7 + (64 - 49))) {
                    if (colorOfCurrentBullet == 1) {
                        newState.redBulletCaptured(bulletColor);
                    }
                    switchPlayer();;
                }
                currentPosition <<= 7;
            } else if (move.getDirection() == DOWN) {
                // check if the bullet we're looking at will remain on the board
                if (Long.numberOfLeadingZeros(currentPosition) > (64 - 7)) {
                    if (colorOfCurrentBullet == 1) {
                        newState.redBulletCaptured(bulletColor);
                    }
                    switchPlayer();
                }
                currentPosition >>= 7;
            } else if (move.getDirection() == LEFT) {
                // check if the bullet we're looking at will remain on the board
                if ((Long.numberOfLeadingZeros(currentPosition) - 15) % 7 == 0) {
                    if (colorOfCurrentBullet == 1) {
                        newState.redBulletCaptured(bulletColor);
                    }
                    switchPlayer();
                    colorOfCurrentBullet = 0;
                }
                currentPosition <<= 1;
            } else if (move.getDirection() == RIGHT) {
                // check if the bullet we're looking at will remain on the board
                if ((Long.numberOfLeadingZeros(currentPosition) - 15) % 7 == 6) {
                    if (colorOfCurrentBullet == 1) {
                        newState.redBulletCaptured(bulletColor);
                    }switchPlayer();
                    colorOfCurrentBullet = 0;
                }
                currentPosition >>= 1;
            }

            // save the color of the current bullet for the next iteration
            colorOfLastBullet = colorOfCurrentBullet;
        }

        if (newState.capturedBulletsBlack > 6 || newState.capturedBulletsWhite > 6) {
            gameWinner = (newState.capturedBulletsBlack > 6 ? 3 : 2);
        }
        return newState;
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
            case 2: capturedBulletsWhite++;break;
            case 3: capturedBulletsBlack++;break;
        }
    }

    /**
     * @param color
     * @return all possible moves for the current player
     */
    public LinkedList<Move> getAllMoves(int color) {
        LinkedList<Move> moves = new LinkedList<Move>();
        long bitboard = bitmaps[color];
        long position = 1;
        while (bitboard != 0) {
            if ((bitboard & 0x1L) != 0) {
                moves.addAll(possibleMoves(position, color));
            }
            bitboard >>= 1;
            position <<= 1;
        }
        moves.forEach(System.out::println);
        return moves;
    }

    /**
     * returns a list of all possible moves for one bullet
     * empty list if no moves are possible
     *
     * @param position the position of the bullet
     * @param color    color of the bullet (2: white, 3: black)
     * @return
     */
    // TODO: remove repititive moves
    private LinkedList<Move> possibleMoves(long position, int color) {
        LinkedList<Move> moves = new LinkedList<>();
        // check up
        //  check if field in the opposite direction is empty
        if ((position & (bitmaps[0] << 7)) == 0) {
            // find the last bullet that would been moved
            long currentPosition = position;
            int lastColor = 0;
            while (getColorAtPosition(currentPosition) != 0) {
                lastColor = getColorAtPosition(currentPosition);
                currentPosition <<= 7;
            }
            if (Long.numberOfLeadingZeros(currentPosition) <= 15){
                if (lastColor != color) {
                    moves.add(new Move(position, UP));
                }
            } else {
                moves.add(new Move(position, UP));
            }
        }
        // check down
        //  check if field in the opposite direction is empty
        if ((position & (bitmaps[0] >> 7)) == 0) {
            // find the last bullet that would been moved
            long currentPosition = position;
            int lastColor = 0;
            while (getColorAtPosition(currentPosition) != 0) {
                lastColor = getColorAtPosition(currentPosition);
                currentPosition >>= 7;
            }
            if (currentPosition == 0){
                if (lastColor != color) {
                    moves.add(new Move(position, DOWN));
                }
            } else {
                moves.add(new Move(position, DOWN));
            }
        }
        // check left
        //  check if field in the opposite direction is empty
        if (isOnEdge(position, RIGHT) || (bitmaps[0] & (position >> 1)) == 0) {
            // find the last bullet that would been moved
            long currentPosition = position;
            int lastColor = getColorAtPosition(currentPosition);
            while (!isOnEdge(currentPosition, LEFT) && getColorAtPosition(currentPosition) != 0) {
                lastColor = getColorAtPosition(currentPosition);
                currentPosition <<= 1;
            }
            if (getColorAtPosition(currentPosition) == 0 || (isOnEdge(currentPosition, LEFT) && lastColor != color)) {
                moves.add(new Move(position, LEFT));
            }
        }
        // check right
        //  check if field in the opposite direction is empty
        if (isOnEdge(position, LEFT) || (bitmaps[0] & (position << 1)) == 0) {
            // find the last bullet that would been moved
            long currentPosition = position;
            int lastColor = getColorAtPosition(currentPosition);
            while (!isOnEdge(currentPosition, RIGHT) && getColorAtPosition(currentPosition) != 0) {
                lastColor = getColorAtPosition(currentPosition);
                currentPosition >>= 1;
            }
            if (getColorAtPosition(currentPosition) == 0 || (isOnEdge(currentPosition, RIGHT) && lastColor != color)) {
                moves.add(new Move(position, RIGHT));
            }
        }
        return moves;
    }

    public int getColorAtPosition(long position) {
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
     * @param position
     * @param direction 'UP' stands for the top row, and so on
     * @return
     */
    public boolean isOnEdge(long position, Move.MoveDirection direction) {
        switch(direction) {
            case UP: return (Long.numberOfLeadingZeros(position) > 15 && Long.numberOfLeadingZeros(position) < 23);
            case DOWN: return (Long.numberOfLeadingZeros(position) > 55);
            case LEFT: return ((Long.numberOfLeadingZeros(position) - 15) % 7 == 0);
            case RIGHT: return ((Long.numberOfLeadingZeros(position) - 15) % 7 == 6);
        }
        return false;
    }

    /**
     * check if a bullet of a give color is on the give position
     *
     * @param position
     * @param color
     * @return
     */
    public boolean isOnPosition(long position, int color) {
        return (position & bitmaps[color]) != 0;
    }

    /**
     * prints the current board
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

    public void printStats() {
        System.out.println("");
        System.out.println("capturedBulletsWhite = " + capturedBulletsWhite);
        System.out.println("capturedBulletsBlack = " + capturedBulletsBlack);
        System.out.println("activePlayer = " + activePlayer);
        System.out.println("gameWinner = " + gameWinner);
        System.out.println("");
    }
}
