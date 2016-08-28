package gameEnvironment;

import board.GameState;
import board.Move;

import java.util.LinkedList;

/**
 * Created by Juviro on 27.08.2016.
 */
public class GameProcessor {

    public GameProcessor(String startingSetup, boolean whiteStarts) {
        long[] bitmaps = createBitmaps(startingSetup);
        int startingPlayer = (whiteStarts ? 2 : 3);

        // initialize the game
        GameState gameState = new GameState(bitmaps, startingPlayer);

        // start the game and play it, untill it's over
        while (gameState.gameWinner == -1) {
            switch (gameState.activePlayer) {
                case 2: performMoveWhite(gameState); break;
                case 3: performMoveBlack(gameState); break;
            }
            System.out.println(gameState.activePlayer);

            gameState.printStats();
            gameState.printField();
        }
    }

    private void performMoveWhite(GameState state) {
        performMoveDummyKi(state);
    }

    private void performMoveBlack(GameState state) {
        performMoveDummyKi(state);
    }



    private void performMoveDummyKi(GameState state) {
        LinkedList<Move> moves = state.getAllMoves(state.activePlayer);
        if(moves.size() > 0) {
            Move move = moves.get((int) (Math.random() * moves.size()));
            state.performMove(move);
        } else {
            System.out.println("NO MORE MOVES POSSIBLE");
        }
    }



    //TODO: read startingSetup
    private long[] createBitmaps(String startingSetup) {
        long[] bitmaps = new long[GameState.NUMBER_OF_BITMAPS];

        bitmaps[0] = 0x18F59C7C735E3L;
        bitmaps[1] = 0x41C7C70400L;
        bitmaps[2] = 0xC1800003060L;
        bitmaps[3] = 0x1830000000183L;

        return bitmaps;
    }
}
