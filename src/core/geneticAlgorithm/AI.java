package core.geneticAlgorithm;


import core.ais.AISettings;

import java.text.DecimalFormat;
import java.util.Arrays;

class AI implements Comparable<AI> {

    private int numberOfWins = 0;
    // average of own bullets - enemy bullets at the end of the game
    private double averageBulletDifference = 0;
    // average of red bullets captured - red bullets captured by the enemy
    private double averageRedBulletDifference = 0;
    private double[] weights = new double[6];
    private aiType aitype;
    private AISettings aiSettings;
    private int gamesWonSoFar = 0;

    enum aiType {
        randomAI, parentAI, childAI, childAIMutated, parentAIMutated, mutantOther, thrownIn
    }


    AI(double redBulletRating, double bulletPredominance, double placementRating, double turnRating, double massRating, double libertyRating, aiType aitype) {
        this.weights[0] = redBulletRating;
        this.weights[1] = bulletPredominance;
        this.weights[2] = placementRating;
        this.weights[3] = turnRating;
        this.weights[4] = massRating;
        this.weights[5] = libertyRating;
        this.aiSettings = new AISettings(true, true, true, true, weights);
        this.aitype = aitype;
    }

    AI(double redBulletRating, double bulletPredominance, double placementRating, double turnRating, double massRating, double libertyRating, aiType aitype, int gamesWonSoFar) {
        this.weights[0] = redBulletRating;
        this.weights[1] = bulletPredominance;
        this.weights[2] = placementRating;
        this.weights[3] = turnRating;
        this.weights[4] = massRating;
        this.weights[5] = libertyRating;
        this.aiSettings = new AISettings(true, true, true, true, weights);
        this.aitype = aitype;
        this.gamesWonSoFar = gamesWonSoFar;
    }

    AI(double[] weights, aiType aitype) {
        this.weights = weights;
        this.aiSettings = new AISettings(true, true, true, true, weights);
        this.aitype = aitype;
    }

    void setResults(boolean gameWon, int redBulletDifference, int bulletDifference) {
        if (gameWon) {
            numberOfWins++;
        }
        averageBulletDifference += (double) bulletDifference / 7;
        averageRedBulletDifference += (double) redBulletDifference / 7;
    }


    int getNumberOfWins() {
        return numberOfWins;
    }

    double getAverageBulletRate() {
        return averageBulletDifference;
    }

    public double getAverageRedBulletDifference() {
        return averageRedBulletDifference;
    }


    double[] getWeights() {
        return weights;
    }

    AISettings getAiSettings() {
        return aiSettings;
    }

    public aiType getAitype() {
        return aitype;
    }

    int getGamesWonSoFar() {
        return gamesWonSoFar;
    }

    void increaseNUmberOfWins() {
        gamesWonSoFar++;
    }

    /**
     * Calculates the fitness for the AI.
     * The fitness is a value between 0 and 1 and consists of 80% win-rate, 10% bullet difference and 10% red bullet difference.
     *
     * @return the calculated fitness
     */
    public double getFitness() {
        double winRate = (((double) numberOfWins / 7)) * 0.8;
        double bulletDifference = 0.05 + (averageBulletDifference / 8) * 0.05;
        double redBulletDifference = 0.05 + (averageRedBulletDifference / 7) * 0.05;

        return winRate + bulletDifference + redBulletDifference;
    }

    void setValue(int index, double value) {
        weights[index] = value;
    }

    @Override
    public int compareTo(AI o) {
        double comparedFitness = o.getFitness();
        if (this.getFitness() > comparedFitness) {
            return 1;
        } else if (this.getFitness() == comparedFitness) {
            return 0;
        } else {
            return -1;
        }
    }


    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public String toString() {
        return "AI{" +
                "fitness=" + df.format(getFitness()) +
                ", numberOfWins=" + numberOfWins +
                ", averageBulletDifference=" + df.format(averageBulletDifference) +
                ", averageRedBulletDifference=" + df.format(averageRedBulletDifference) +
                ", weights=" + Arrays.toString(weights) +
                ", aitype=" + aitype +
                ", gamesWonSoFar=" + gamesWonSoFar +
                '}';
    }
}
