package tests;

import board.GameState;
import board.Move;
import board.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static board.Move.MoveDirection.DOWN;
import static org.junit.Assert.*;

public class GameStateTest {
    private GameState gameState;
    private String startingSetup = "2200030220103300111030111110001110233010223300020";

    @Before
    public void setUp() throws Exception {
        gameState = new GameState(2);
        gameState.bitmaps = Parser.stringToBitboard(startingSetup);
        gameState.printField();
    }

//    @Test
//    public void testExecuteMove() {
//        Move move = new Move(16384L, DOWN, false);
//        GameState gameStateCopy = gameState.executeMove(move);
//        gameStateCopy.printField();
//        assertTrue(gameStateCopy.bitmaps[2] != gameState.bitmaps[2]);
//    }

    @Test
    public void stuff() {
        int test = -4;
        System.out.println(test % 7);
    }
}