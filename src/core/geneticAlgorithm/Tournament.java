package core.geneticAlgorithm;


import core.ais.AISettings;

class Tournament {

    private static AISettings[] AIs = new AISettings[8];

    /**
     * Plays a round-robin tournament and stores the fitness values in double[].
     *
     * @return double[] with the fitness values.
     * @param weights weights for the AIs.
     */
    static double[] playTournament(double[][] weights) {
        initAis(weights);
        return null;
    }

    /**
     * Creates the AI Objects.
     * @param weights weights of all AIs.
     */
    private static void initAis(double[][] weights) {
        for (int i = 0; i < 8; i++) {
            double[] aiWeights = new double[4];
            System.arraycopy(weights[i], 0, aiWeights, 0, 4);
            AIs[i] = new AISettings(true, true, true, true, aiWeights);
        }
    }
}
