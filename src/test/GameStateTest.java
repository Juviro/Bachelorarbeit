package test;

import board.GameState;
import board.Move;
import board.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameStateTest {
    GameState gameState;
    String startingSetup = "0022003220103300111300111110001112033010223300002";

    @Before
    public void setUp() throws Exception {
        gameState = new GameState(2);
        gameState.bitmaps = Parser.stringToBitboard(startingSetup);
        gameState.printField();
        gameState = gameState.executeMove(new Move(68719476736L, Move.MoveDirection.DOWN));
        gameState.printField();
        gameState.executeMove(new Move(2L, Move.MoveDirection.UP)).printField();
        System.out.println(gameState.lastMove.toString());
    }

    @Test
    public void testMoveRepetition() {
        assertFalse(gameState.noRepetitiveMove(2L, Move.MoveDirection.UP));
//        long position = 1;
//        for (long i = 0; i < 49; i++) {
//            System.out.println(position + ": " + gameState.noRepetitiveMove(position, Move.MoveDirection.UP));
//            position <<= 1;
//        }
    }
}