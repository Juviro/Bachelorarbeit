package ais.negamax;

import board.GameState;

class Ratings {

    /**
     * weigths to easily adjust the influence of the different rating forms
     */
    private static final double[] weights = {1, 0.5, -1};
    private static final int PLAYER_MULTIPLIER = 0;
    private static final int PREDOMINANCE_MULTIPLIER = 1;
    private static final int EDGE_RATING_MULTIPLIER = 2;

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
        double redBulletRating = bulletRating(state);
        double bulletPredominance = bulletPredominance(state.bitmaps);

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
        // all rating functions check for white ratings, so black is -1 * this rating
        int playerMultiplier = (color == 2 ? 1 : -1);

        // get all the different ratings
        double redBulletRating = bulletRating(state);
        double bulletPredominance = bulletPredominance(state.bitmaps);
        double edgeRating = noBulletsOnEdge(state.bitmaps[color]);

        // weight them and add them
        double rating = redBulletRating * weights[PLAYER_MULTIPLIER] + bulletPredominance * weights[PREDOMINANCE_MULTIPLIER] + edgeRating * weights[EDGE_RATING_MULTIPLIER];
        return rating * playerMultiplier;
    }

    /**
     * @param state current gameState
     * @return difference between captured red bullets by white and black.
     */
    private static int bulletRating(GameState state) {
        return state.capturedBulletsWhite - state.capturedBulletsBlack;
    }

    /**
     *
     * @param bitmaps board representations
     * @return difference between white and black bullets on the board
     */
    private static double bulletPredominance(long[] bitmaps) {
        return Long.bitCount(bitmaps[2]) - Long.bitCount(bitmaps[3]);
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
}
