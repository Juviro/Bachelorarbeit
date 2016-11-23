package tests;


import org.junit.Test;
import ais.Ratings;

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
}
