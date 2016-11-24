package core.tests;

import core.board.GameState;
import core.board.Move;
import core.board.Parser;
import org.junit.Before;
import org.junit.Test;

import static core.board.Move.MoveDirection.DOWN;
import static org.junit.Assert.assertTrue;

public class GameStateTest {
    private GameState gameState;
    private String startingSetup = "2200030220103300111030111110001110233010223300020";

    @Before
    public void setUp() throws Exception {
        gameState = new GameState(2);
        gameState.bitmaps = Parser.stringToBitboard(startingSetup);
        gameState.printField();
    }

    @Test
    public void testExecuteMove() {
        Move move = new Move(16384L, DOWN, false);
        GameState gameStateCopy = gameState.executeMove(move);
        gameStateCopy.printField();
        assertTrue(gameStateCopy.bitmaps[2] != gameState.bitmaps[2]);
    }
}