package board;

/**
 * Created by Juviro on 27.08.2016.
 */
public class Parser {

    public static long[] arrayToBitboards(char[][] array) {
        long[] bitmaps = new long[GameState.NUMBER_OF_BITMAPS];

        return bitmaps;
    }

    public static char[][] bitboardsToArray(GameState state) {
        char[][] board = new char[7][7];
        long position = 0x1000000000000L;
        for (int i = 0; i < GameState.NUMBER_OF_FIELDS; i++) {
            if (state.isOnPosition(position, 1)) {
                board[i / 7][i % 7] = 'R';
            } else if (state.isOnPosition(position, 2)) {
                board[i / 7][i % 7] = 'W';
            } else if (state.isOnPosition(position, 3)) {
                board[i / 7][i % 7] = 'B';
            } else {
                board[i / 7][i % 7] = '0';
            }
            position >>>= 1;
        }
        return board;
    }
}
