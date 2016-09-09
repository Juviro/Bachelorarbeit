package ais.negamax;

import board.GameState;

class Ratings {

    /**
     * weights to easily adjust the influence of the different rating forms
     */
    private static final double[] weights = {1, 0.5, -1, 1};
    private static final int PLAYER_MULTIPLIER = 0;
    private static final int PREDOMINANCE_MULTIPLIER = 1;
    private static final int EDGE_RATING_MULTIPLIER = 2;
    private static final int STICK_RATING_MULTIPLIER = 3;

    /**
     * bullet rating
     * bullet predominance
     *
     * @param state current gameState
     * @param color active player
     * @return heuristic rating value
     */
    static double rateState(GameState state, int color) {
        // all rating functions check for white ratings, so black is -1 * this rating
        int playerMultiplier = (color == 2 ? 1 : -1);

        // get all the different ratings
        double redBulletRating = bulletRating(state, color);
        double bulletPredominance = bulletPredominance(state.bitmaps, color);

        // weight them and add them
        double rating = redBulletRating * weights[PLAYER_MULTIPLIER] + bulletPredominance * weights[PREDOMINANCE_MULTIPLIER];
        return rating * playerMultiplier;
    }

    /**
     * bullet rating
     * bullet predominance
     * edge rating
     *
     * @param state current gameState
     * @param color active player
     * @return heuristic rating value
     */
    static double rateStateV2(GameState state, int color) {
        // get all the different ratings
        double redBulletRating = bulletRating(state, color);
        double bulletPredominance = bulletPredominance(state.bitmaps, color);
        double edgeRating = noBulletsOnEdge(state.bitmaps[color]);
        double stickRating = stickToLastBullet(state.bitmaps, color);

        // weight them and add them
        return redBulletRating * weights[PLAYER_MULTIPLIER] + bulletPredominance * weights[PREDOMINANCE_MULTIPLIER] + edgeRating * weights[EDGE_RATING_MULTIPLIER] + stickRating * weights[STICK_RATING_MULTIPLIER];
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
     * @param bitmaps board representations
     * @param color current player
     * @return difference between white and black bullets on the board
     */
    private static double bulletPredominance(long[] bitmaps, int color) {
        int playerMultiplier = (color == 2 ? 1 : -1);
        return (Long.bitCount(bitmaps[2]) - Long.bitCount(bitmaps[3])) * playerMultiplier;
    }

    /**
     * Returns the % amount of bullets the active player controls that are on the edge of the board.
     * @param bitmap bitmap board representation of the active player
     * @return bullets on the edge / bullets total
     */
    private static double noBulletsOnEdge(long bitmap) {
        long edgePositions = 560802875597055L;
        int bulletsOnEdge = Long.bitCount(edgePositions & bitmap);
        return (double) bulletsOnEdge / (double) Long.bitCount(bitmap);
    }

    /**
     * If only one bullet remains, make sure to place a bullet next to it.
     * @param bitmaps board representations
     * @param color active player
     * @return 0 if there is more than 1 red bullet, -1 if no [color] bullet is in range of the last red bullet, and 1 if at least one is.
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
     * @param position position
     * @param bitboard bitboard
     * @return true if the position is next to at least one position from the bitboard
     */
    private static boolean isNeighbor(long position, long bitboard){
        position <<= 4;
        for (int i = 1; i < 9; i++) {
            if ((position & bitboard) != 0) {
                return true;
            }
            position >>= 1;
        }
        return false;
    }
}
