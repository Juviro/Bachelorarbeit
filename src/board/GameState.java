package board;

/**
 * Created by Juviro on 12.08.2016.
 */
public class GameState {

    /**
     * Number of all bitboards.
     */
    public static final int NUMBER_OF_BITMAPS = 4;

    /**
     * All bitboards representad as array(long).
     */
    long[] bitmaps;

    /**
     * Constructor.
     */
    public GameState() {
        bitmaps = new long[NUMBER_OF_BITMAPS];
    }

    /**
     * copy current Gamestate
     * @return the copy
     */
    public GameState copy() {
        final GameState newState = new GameState();
        for (int i = 0; i < bitmaps.length; i++) {
            newState.bitmaps[i] = this.bitmaps[i];
        }
        return newState;
    }

    /**
     *
     * @param move
     * @return
     */

    public GameState performMove(Move move) {
        GameState newState = this.copy();

        return newState;
    }

}
