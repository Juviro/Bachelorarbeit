package core.tests;


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
        long bitboard = 0b1000001000000000000000000000000000000000001000001L;
        assertTrue(Ratings.centerOfMass(bitboard) == Math.sqrt(18));
    }
    @Test
    public void testCenterOfMass2() {
        long bitboard = 0b0000000000000000000000000000000000000000001000001L;
        assertTrue(Ratings.centerOfMass(bitboard) == Math.sqrt(18) - 3);
    }
    @Test
    public void testCenterOfMass3() {
        long bitboard = 0b0000000000000000000000000000000000000000000000001L;
        assertTrue(Ratings.centerOfMass(bitboard) == 0);
    }
    @Test
    public void testLibertyRating1() {
        long bitboard1 = 0b0000000000000000000000000000000001000001110000011L;
        long bitboard2 = 0b0000000000000000000000000000000000000000100000001L;
        long[] bitBoards = new long[2];
        bitBoards[0] = bitboard1;
        bitBoards[1] = bitboard2;
        assertTrue(Ratings.bulletLibertyRating(bitBoards, 1) == 0.5);
    }
    @Test
    public void testLibertyRating2() {
        long bitboard1 = 0b0000000000000000000000000000000001000001110000011L;
        long bitboard2 = 0b0000000000000000000000000000000000000000100000000L;
        long[] bitBoards = new long[2];
        bitBoards[0] = bitboard1;
        bitBoards[1] = bitboard2;
        assertTrue(Ratings.bulletLibertyRating(bitBoards, 1) == 0);
    }
    @Test
    public void testLibertyRating3() {
        long bitboard1 = 0b0000000000000001000000000000000001000001100000011L;
        long bitboard2 = 0b0000000000000001000000000000000000000000100000001L;
        long[] bitBoards = new long[2];
        bitBoards[0] = bitboard1;
        bitBoards[1] = bitboard2;
        assertTrue(Ratings.bulletLibertyRating(bitBoards, 1) == 1);
    }
}
