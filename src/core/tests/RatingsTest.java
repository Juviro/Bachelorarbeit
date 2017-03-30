package core.tests;


import core.ais.AISettings;
import core.ais.NegamaxAI;
import core.board.GameState;
import core.board.Parser;
import org.junit.Test;
import core.ais.Ratings;

import static org.junit.Assert.*;

public class RatingsTest {


    /**
     *  0000000
     *  0000000
     *  0000000
     *  1000000
     *  0000000
     *  0000000
     *  0000000
     */
    private long position = 0b0000000000000000000000010000000000000000000000000L;

    @Test
    public void testIsNeighbour1() {
        long neighbour = 0b0000000000000000000000100000000000000000000000000L;
        assertTrue(Ratings.isNeighbor(position, neighbour));
    }

    @Test
    public void testIsNeighbour2() {
        long neighbour = 0b0000000000000000000001000000000000000000000000000L;
        assertFalse(Ratings.isNeighbor(position, neighbour));
    }

    @Test
    public void testIsNeighbour3() {
        long neighbour = 0b0000000000000000000010000000000000000000000000000L;
        assertFalse(Ratings.isNeighbor(position, neighbour));
    }

    @Test
    public void testIsNeighbour4() {
        long neighbour = 0B0000000000000000100000000000000000000000000000000L;
        assertTrue(Ratings.isNeighbor(position, neighbour));
    }
    @Test
    public void testIsNeighbour5() {
        long neighbour = 0b0000000000000001000000000000000000000000000000000L;
        assertTrue(Ratings.isNeighbor(position, neighbour));
    }
    @Test
    public void testCenterOfMass1() {
        long bitboard1 = 0b1000001000000000000000000000000000000000001000001L;
        long bitboard2 = 0b1000001000000000000000000000000000000000001000001L;
        assertTrue(Ratings.centerOfMass(bitboard1, bitboard2) == 0);
    }
    @Test
    public void testCenterOfMass2() {
        long bitboard1 = 0b1000001000000000000000000000000000000000001000001L;
        long bitboard2 = 0b1000000000000000000000000000000000000000000000000L;
        assertTrue(Ratings.centerOfMass(bitboard1, bitboard2) == Math.sqrt(18));
    }
    @Test
    public void testCenterOfMass3() {
        long bitboard1 = 0b1000001000000000000000000000000000000000001000001L;
        long bitboard2 = 0b1000001000000000000000000000000000000000001000001L;
        assertTrue(Ratings.centerOfMass(bitboard1, bitboard2) == 0);
    }
    @Test
    public void testLibertyRating1() {
        long bitboard1 = 0b1000000000000000000000000000000001000001110000011L;
        long bitboard2 = 0b0000000000000000000000000000000000000000100000001L;
        long bitboard3 = 0b1000000000000000000000000000000000000000000000001L;
        long[] bitBoards = new long[3];
        bitBoards[0] = bitboard1;
        bitBoards[1] = bitboard2;
        bitBoards[2] = bitboard3;
        assertTrue(Ratings.bulletLibertyRating(bitBoards, 1, 2) == 0.5);
    }
    @Test
    public void testLibertyRating2() {
        long bitboard1 = 0b1000000000000000000000000000000001000001110000011L;
        long bitboard2 = 0b0000000000000000000000000000000000000000100000000L;
        long bitboard3 = 0b1000000000000000000000000000000000000000000000000L;
        long[] bitBoards = new long[3];
        bitBoards[0] = bitboard1;
        bitBoards[1] = bitboard2;
        bitBoards[2] = bitboard3;
        assertTrue(Ratings.bulletLibertyRating(bitBoards, 1, 2) == 0);
    }
    @Test
    public void testLibertyRating3() {
        long bitboard1 = 0b1000000000000001000000000000000001000001100000011L;
        long bitboard2 = 0b0000000000000001000000000000000000000000100000001L;
        long bitboard3 = 0b1000000000000001000000000000000000000000000000000L;
        long[] bitBoards = new long[3];
        bitBoards[0] = bitboard1;
        bitBoards[1] = bitboard2;
        bitBoards[2] = bitboard3;
        assertTrue(Ratings.bulletLibertyRating(bitBoards, 1, 2) == 1);
    }

    @Test
    public void testRating() {
        String startingSetup = "0020000030110000112020001012030001000000220000022";
        GameState state = new GameState(3);
        state.bitmaps =  Parser.stringToBitboard(startingSetup);
        System.out.println(state.activePlayer);
        NegamaxAI ai = new NegamaxAI(new AISettings(true, true, false, true));
        ai.negamax(state, 8, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        System.out.println("ai.bestMoves = " + ai.bestMoves);
    }

    @Test
    public void testAlphaBetaCut() {
        String startingSetup = "0020000030110000112020001011030001000000220000022";
        GameState state = new GameState(3);
        state.bitmaps =  Parser.stringToBitboard(startingSetup);
        NegamaxAI aiEnabled = new NegamaxAI(new AISettings(true, true, false, true));
        NegamaxAI aiDisabled = new NegamaxAI(new AISettings(false, false, false, true));
        long timeStart = System.currentTimeMillis();
        aiEnabled.negamax(state, 8, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        System.out.println("timeUsedEnabled = " + (System.currentTimeMillis() - timeStart));
        timeStart = System.currentTimeMillis();
        aiDisabled.negamax(state, 8, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        System.out.println("timeUsedDisabled = " + (System.currentTimeMillis() - timeStart));
        System.out.println("aiEnabled = " + aiEnabled.numberOfRatedStates);
        System.out.println("aiDisabled = " + aiDisabled.numberOfRatedStates);
        System.out.println("aiEnabled.bestMoves = " + aiEnabled.bestMoves);
        System.out.println("aiDisabled.bestMoves = " + aiDisabled.bestMoves);
    }
}
