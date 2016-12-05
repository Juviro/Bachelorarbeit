package core.geneticAlgorithm;


import core.ais.AISettings;

import java.util.Arrays;

class AI implements Comparable<AI> {

    private int numberOfWins = 0;
    // average of own bullets - enemy bullets at the end of the game
    private double averageBulletDifference = 0;
    // average of red bullets captured - red bullets captured by the enemy
    private double averageRedBulletDifference = 0;
    private double[] weights = new double[4];
    private aiType aitype;
    private AISettings aiSettings;

    /*
    // TODO überarbeiten
     Enum to distinguish the different AI types.
     randomAI: completely random values, only appears in the very first generation.
     parentAI: the AIs with the highest fitness from the last generation.
     mutantWinner: mutations of the parentAIs.
     mutantOther: mutation of AIs from the last generation that did not win.
      */
    enum aiType {
        randomAI, parentAI, childAI, childAIMutated, parentAIMutated, mutantOther
    }


    AI(double redBulletRating, double bulletPredominance, double placementRating, double stickRating, aiType aitype) {
        this.weights[0] = redBulletRating;
        this.weights[1] = bulletPredominance;
        this.weights[2] = placementRating;
        this.weights[3] = stickRating;
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

    @Override
    public String toString() {
        return "AI{" +
                "fitness=" + getFitness() +
                "numberOfWins=" + numberOfWins +
                ", averageBulletDifference=" + averageBulletDifference +
                ", averageRedBulletDifference=" + averageRedBulletDifference +
                ", weights=" + Arrays.toString(weights) +
                ", aitype=" + aitype +
                '}';
    }
}
