package core.ais;

import core.board.GameState;

public class Ratings {

    /**
     * weights to easily adjust the influence of the different rating forms
     */
    private double[] weights = {1, 0.5, 1, 0.5};
    private static final int PLAYER_MULTIPLIER = 0;
    private static final int PREDOMINANCE_MULTIPLIER = 1;
    private static final int PLACEMENT_RATING_MULTIPLIER = 2;
    private static final int STICK_RATING_MULTIPLIER = 3;

    Ratings(double[] weights) {
        this.weights = weights;
    }

    /**
     * bullet rating
     * bullet predominance
     * edge rating
     * stick to last red bullet rating
     *
     * @param state current gameState
     * @param color active player
     * @return heuristic rating value
     */
    double rateState(GameState state, int color) {
        int enemyColor = ((color + 1) % 2) + 2;
        // get all the different ratings
        double redBulletRating = bulletRating(state, color);
        double bulletPredominance = bulletPredominance(state.bitmaps, color);
        double placementRating = bulletPlacementRating(state.bitmaps[color], state.bitmaps[enemyColor]);
        double stickRating = stickToLastBullet(state.bitmaps, color);

        // weight them and add them
        return redBulletRating * weights[PLAYER_MULTIPLIER] + bulletPredominance * weights[PREDOMINANCE_MULTIPLIER] + placementRating * weights[PLACEMENT_RATING_MULTIPLIER] + stickRating * weights[STICK_RATING_MULTIPLIER];
    }

    /**
     * @param state current gameState
     * @param color current player
     * @return difference between captured red bullets by white and black.
     */
    private static int bulletRating(GameState state, int color) {
        int playerMultiplier = (color == 2 ? 1 : -1);
        return (state.capturedBulletsWhite - state.capturedBulletsBlack) * playerMultiplier;
    }

    /**
     *
     * @param bitmaps core.board representations
     * @param color current player
     * @return difference between white and black bullets on the core.board
     */
    private static double bulletPredominance(long[] bitmaps, int color) {
        int playerMultiplier = (color == 2 ? 1 : -1);
        return (Long.bitCount(bitmaps[2]) - Long.bitCount(bitmaps[3])) * playerMultiplier;
    }

    /**
     * Rates the position of the own bullets and the enemy bullets on the core.board. The closer to the mid the bullets are, the better.
     * The rating is based on this board:
     *
     * 1 1 1 1 1 1 1
     * 1 2 2 2 2 2 1
     * 1 2 3 3 3 2 1
     * 1 2 3 3 3 2 1
     * 1 2 3 3 3 2 1
     * 1 2 2 2 2 2 1
     * 1 1 1 1 1 1 1
     *
     * @param ownBitmap bitmap core.board representation of the active player
     * @param enemyBitboard bitmap core.board representation of the inactive player
     * @return rating of own Bullets / (number of own bullets * 1.5) - rating of enemy Bullets / (number of enemy bullets * 1.5)
     */
    private static double bulletPlacementRating(long ownBitmap, long enemyBitboard) {
        long positionsRankOne = 560802875597055L;
        long positionsRankTwo = 2139502452480L;
        long positionsRankThree = 7575371776L;
        int bulletsOnRankOne = Long.bitCount(positionsRankOne & ownBitmap);
        int bulletsOnRankTwo = Long.bitCount(positionsRankTwo & ownBitmap);
        int bulletsOnRankThree = Long.bitCount(positionsRankThree & ownBitmap);
        int numberOfBullets = Long.bitCount(ownBitmap);
        return ((bulletsOnRankOne + bulletsOnRankTwo * 2 + bulletsOnRankThree * 3) / (numberOfBullets * 1.5));
    }

    /**
     * If only one bullet remains, make sure to place an own bullet next to it.
     * @param bitmaps core.board representations
     * @param color active player
     * @return 0 if there is more than 1 red bullet, -1 if no [color] bullet is right next to the last red bullet, and 1 if at least one is.
     */
    private static double stickToLastBullet(long[] bitmaps, int color) {
        if (Long.bitCount(bitmaps[1]) == 1) {
            if (isNeighbor(bitmaps[1], bitmaps[color])) {
                return 1;
            }
            return -1;
        }
        return 0;
    }

    /**
     * Checks if a position is next to a bullet in a bitmap.
     * @param position position
     * @param bitboard bitboard
     * @return true if the position is next to at least one position from the bitboard
     */
    public static boolean isNeighbor(long position, long bitboard){
        // neighbour on the left side
        if (!((Long.numberOfLeadingZeros(position) - 15) % 7 == 0)) {
            if (((position << 1 & bitboard) != 0) || ((position << 8 & bitboard) != 0) || ((position >> 6 & bitboard) != 0)) {
                return true;
            }
        }
        // neighbour on the right side
        if (!((Long.numberOfLeadingZeros(position) - 15) % 7 == 6)) {
            if (((position >> 1 & bitboard) != 0) || ((position >> 8 & bitboard) != 0) || ((position << 6 & bitboard) != 0)) {
                return true;
            }
        }

        // check bullets above end below, return false if it's not
        return ((position >> 7 & bitboard) != 0) || ((position << 7 & bitboard) != 0);
    }
}