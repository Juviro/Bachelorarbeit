package core.tests;

import core.board.GameState;
import core.board.Move;
import core.board.Parser;
import core.geneticAlgorithm.GeneticAlgorithmMain;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static core.board.Move.MoveDirection.DOWN;
import static org.junit.Assert.assertTrue;

public class GameStateTest {
    private GameState gameState;
    private String startingSetup = "2200030220103300111030111110001110233010223300020";

    @Before
    public void setUp() throws Exception {
        gameState = new GameState(2);
        gameState.bitmaps = Parser.stringToBitboard(startingSetup);
        //gameState.printField();
    }

    @Test
    public void testExecuteMove() {
        Move move = new Move(16384L, DOWN, false);
        GameState gameStateCopy = gameState.executeMove(move);
        gameStateCopy.printField();
        assertTrue(gameStateCopy.bitmaps[2] != gameState.bitmaps[2]);
    }

    @Test
    public void testAvgDiff() {
        int diff = 0;
        for (int i = 0; i < 100000; i++) {
            int num = (int) (Math.random() * 1024);
            num -= 512;
            int mutant = (int) GeneticAlgorithmMain.mutateDouble(num, 0.01);
            diff += Math.abs(num - mutant);
            //System.out.println(num + " -> " + mutant );
        }
        System.out.println("avg diff = " + diff / 100000);
    }

    @Test public void testPossibleMoves() {
        String position = "2200000010130003211300021130201003230110123000020";
        gameState = new GameState(2);
        gameState.bitmaps = Parser.stringToBitboard(position);
        //gameState.printField();
        //LinkedList<Move> moves = gameState.getAllMoves(2);
        // moves.forEach(System.out::println);
        Move move = new Move(1048576L, DOWN, true);
        gameState = gameState.executeMove(move);
        gameState.printStats();
        gameState.printField();
    }

    @Test public void testIsOnEdge() {
        assertTrue(GameState.isOnEdge(64, DOWN));
    }
}