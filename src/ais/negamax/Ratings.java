package ais.negamax;

import board.GameState;

public class Ratings {

    /**
     * weigths to easily adjust the influence of the different rating forms
     */
    private static final double[] weights = {1, 0.5};
    private static final int PLAYER_MULTIPLIER = 0;
    private static final int PREDOMINANCE_MULTIPLIER = 1;
    private static final int CAPTUREMOVE_MULTIPLIER = 1;

    public static double rateState(GameState state, int color) {
        // all rating functions check for white ratings, so black is -1 * this rating
        int playerMultiplier = (color == 2 ? 1 : -1);

        // get all the different ratings
        double redBulletRating = bulletRating(state);
        double bulletPredominance = bulletPredominance(state.bitmaps);

        // weight them and add them
        double rating = redBulletRating * weights[PLAYER_MULTIPLIER] + bulletPredominance * weights[PREDOMINANCE_MULTIPLIER];
        return rating * playerMultiplier;
    }

    private static int bulletRating(GameState state) {
        return state.capturedBulletsWhite - state.capturedBulletsBlack;
    }

    private static double bulletPredominance(long[] bitmaps) {
        return Long.bitCount(bitmaps[2]) - Long.bitCount(bitmaps[3]);
    }
}
