package gameenvironment;

import board.GameState;
import board.Move;

public class Ratings {
    public static double rateState(GameState state, int color) {
        int multiplier = (color == 2 ? 1 : -1);
        double rating = (double) state.capturedBulletsWhite - state.capturedBulletsBlack;
        return rating * multiplier;
    }
}
