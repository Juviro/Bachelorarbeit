package core.ais;

import core.board.GameState;

public class Ratings {

    /**
     * weights to easily adjust the influence of the different rating forms
     */
    private double[] weights = {1, 0.5, 1, 0.5, 0.5, 1};
    private static final int PLAYER_MULTIPLIER = 0;
    private static final int PREDOMINANCE_MULTIPLIER = 1;
    private static final int PLACEMENT_RATING_MULTIPLIER = 2;
    private static final int STICK_RATING_MULTIPLIER = 3;
    private static final int CENTER_OF_MASS_MULTIPLIER = 4;
    private static final int LIBERTY_RATING_MULTIPLIER = 5;

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
     * @param color color of the active player
     * @return heuristic rating value
     */
    double rateState(GameState state, int color) {
        int enemyColor = ((color + 1) % 2) + 2;
        // get all the different ratings
        double redBulletRating = (weights[PLAYER_MULTIPLIER] != 0 ? bulletRating(state, color) : 0);
        double bulletPredominance = (weights[PREDOMINANCE_MULTIPLIER] != 0 ? bulletPredominance(state.bitmaps, color) : 0);
        double placementRating = (weights[PLACEMENT_RATING_MULTIPLIER] != 0 ? bulletPlacementRating(state.bitmaps[color], state.bitmaps[enemyColor]) : 0);
        double stickRating = (weights[STICK_RATING_MULTIPLIER] != 0 ? stickToLastBullet(state.bitmaps, color) : 0);
        double centerOfMassRating = (weights[CENTER_OF_MASS_MULTIPLIER] != 0 ? centerOfMass(state.bitmaps[color], state.bitmaps[enemyColor]) : 0);
        double libertyRating = (weights[LIBERTY_RATING_MULTIPLIER] != 0 ? bulletLibertyRating(state.bitmaps, color, enemyColor) : 0);


        // weight and add them
        return redBulletRating * weights[PLAYER_MULTIPLIER] + bulletPredominance * weights[PREDOMINANCE_MULTIPLIER] + placementRating * weights[PLACEMENT_RATING_MULTIPLIER] + stickRating * weights[STICK_RATING_MULTIPLIER] + centerOfMassRating * weights[CENTER_OF_MASS_MULTIPLIER] + libertyRating * weights[LIBERTY_RATING_MULTIPLIER];
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
     * Rates the position of the own bullets and the enemy bullets on the board. The closer to the mid the bullets are, the better.
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
     * @param ownBitmap bitmap board representation of the active player
     * @param enemyBitboard bitmap board representation of the inactive player
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
        double ownBoardRating = (double) (bulletsOnRankOne + bulletsOnRankTwo * 2 + bulletsOnRankThree * 3) / (numberOfBullets * 1.5);

        bulletsOnRankOne = Long.bitCount(positionsRankOne & enemyBitboard);
        bulletsOnRankTwo = Long.bitCount(positionsRankTwo & enemyBitboard);
        bulletsOnRankThree = Long.bitCount(positionsRankThree & enemyBitboard);
        numberOfBullets = Long.bitCount(enemyBitboard);
        double enemyBoardRating = (double) (bulletsOnRankOne + bulletsOnRankTwo * 2 + bulletsOnRankThree * 3) / (numberOfBullets * 1.5);

        return ownBoardRating - enemyBoardRating;
    }

    /**
     * Calculates the distance of the center of mass for the own bullets to the center of the board.
     * The distance is between 0 and sqrt(18) and the function returns sqrt(18) - the distance.
     *
     * @param ownBitmap Representation of the own bullets on the board.
     * @param enemyBitmap Representation of the enemy bullets on the board.
     * @return enemy distance to center - own distance to center
     */
    public static double centerOfMass(long ownBitmap, long enemyBitmap) {
        long position = 1;
        int sumXOwn = 0;
        int sumYOwn = 0;
        int sumXEnemy = 0;
        int sumYEnemy = 0;
        int numberOfOwnBullets = Long.bitCount(ownBitmap);
        int numberOfEnemyBullets = Long.bitCount(enemyBitmap);
        for (int i = 7; i > 0; i--) {
            for (int j = 7; j > 0; j--) {
                if ((ownBitmap & position) != 0) {
                    sumXOwn += j;
                    sumYOwn += i;
                }
                if ((enemyBitmap & position) != 0) {
                    sumXEnemy += j;
                    sumYEnemy += i;
                }
                position <<= 1;
            }
        }
        double distanceToCenterOwn = Math.sqrt(Math.pow(4 - (double) sumXOwn / numberOfOwnBullets, 2) + Math.pow(4 - (double) sumYOwn / numberOfOwnBullets, 2));
        double distanceToCenterEnemy = Math.sqrt(Math.pow(4 - (double) sumXEnemy / numberOfEnemyBullets, 2) + Math.pow(4 - (double) sumYEnemy / numberOfEnemyBullets, 2));
        return distanceToCenterEnemy - distanceToCenterOwn;
    }


    /**
     * Calculates the percentage value of own bullets with at least one liberty minus the same for the enemy.
     *
     * @param bitmaps board representation
     * @param color own color
     * @param enemyColor enemy color
     * @return % of own bullets that have at least one liberty - % of enemy bullets that have at least one liberty
     */
    public static double bulletLibertyRating(long[] bitmaps, int color, int enemyColor) {
        long position = 1;
        int numberOfOwnBullets = Long.bitCount(bitmaps[color]);
        int numberOfOwnBulletsWithLiberties = 0;
        int numberOfEnemyBullets = Long.bitCount(bitmaps[color]);
        int numberOfEnemyBulletsWithLiberties = 0;

        for (int i = 7; i > 0; i--) {
            for (int j = 7; j > 0; j--) {
                if ((bitmaps[color] & position) != 0) {
                    // if the bullet is on the edge, there is always at least one liberty
                    if (i == 7 || i == 1 || j == 7 || j == 1) {
                        numberOfOwnBulletsWithLiberties++;
                    } else if ((position >> 1 & bitmaps[0]) == 0 || (position >> 7 & bitmaps[0]) == 0 || (position << 1 & bitmaps[0]) == 0 || (position << 7 & bitmaps[0]) == 0) {
                        numberOfOwnBulletsWithLiberties++;
                    }
                } else if ((bitmaps[enemyColor] & position) != 0) {
                    // if the bullet is on the edge, there is always at least one liberty
                    if (i == 7 || i == 1 || j == 7 || j == 1) {
                        numberOfEnemyBulletsWithLiberties++;
                    } else if ((position >> 1 & bitmaps[0]) == 0 || (position >> 7 & bitmaps[0]) == 0 || (position << 1 & bitmaps[0]) == 0 || (position << 7 & bitmaps[0]) == 0) {
                        numberOfEnemyBulletsWithLiberties++;
                    }
                }
                position <<= 1;
            }
        }

        double ownPercentage = numberOfOwnBulletsWithLiberties / numberOfOwnBullets;
        double enemyPercentage = numberOfEnemyBulletsWithLiberties / numberOfEnemyBullets;

        return ownPercentage - enemyPercentage;
    }


    /**
     * If only one bullet remains, make sure to place an own bullet next to it.
     * @param bitmaps board representations
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