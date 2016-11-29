package core.geneticAlgorithm;

import java.io.*;

public class GeneticAlgorithmMain {

    private final static String WEIGHTS_FILE = "./GeneticAlgorithm/generations.csv";
    private static double[][] weightArray = new double[8][4];
    private static int lastGameFileIndex = 0;


    public static void main (String[] args) {
//        mutateDouble(15, 0.1f);

//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 4; j++) {
//                System.out.println(weightArray[i][j]);
//            }
//        }
        processGames();
    }


    private static void processGames() {
        setWeights();
        double[] tournamentResults = Tournament.playTournament(weightArray);
        // TODO process and save data and restart

        // fitness;redBulletRating;bulletPredominanceRating;placementRating;stickRating
    }

    private static void setWeights() {
        // get the file of the last generation
        while(new File("./GeneticAlgorithm/gen" + (lastGameFileIndex + 1) + ".csv").isFile()) {
            lastGameFileIndex++;
        }
        // initialize AIs with random values if there is no previous generation
        if(lastGameFileIndex == 0) {
            initializeAis();
        } else {

        }
    }



    private static double mutateDouble(double d, float mutationRate) {
        // make sure the mutationRate is between 0 and 0.15
        mutationRate = Math.max(Math.min(mutationRate, 0.15f), 0);
        // 68.2 * 15 = 0b1111111111
        long mutant = (long) (d * 68.2);
        long position = 0b1000000000L;
        for (int i = 1; i < 11; i++) {
            // the chance for a bit to flip is mutationRate * number of iteration (maximum 100%)
            if (Math.random() < mutationRate * i) {
                mutant = mutant ^ position;
            }
            position >>= 1;
        }
        return mutant / 68.2;
    }





    /**
     * Loads the rating values of the last two winning AIs from the generations.csv into the weight array.
     */
    private static void setLastParentWeights() {
        try {
            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(WEIGHTS_FILE));
            Object[] lines = lineNumberReader.lines().toArray();
            int arrayLength = lines.length;
            if (arrayLength == 1) {
                initializeAis();
            } else {
                String penultimateLine = lines[arrayLength - 2].toString();
                String lastLine = lines[arrayLength - 1].toString();
                String[] lineArray = (penultimateLine + ";" + lastLine).split(";");
                int l = 2;
                for (int i = 0; i < 8; i++) {
                    if (i == 4) {
                        l += 2;
                    }
                    weightArray[i/4][i%4] = Double.parseDouble(lineArray[l]);
                    l++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setMutantWeights() {

    }

    private static void setChildWeights() {
    }

    /**
     * If this is the first Generation, generate random AI weights.
     */
    private static void initializeAis() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                weightArray[i][j] = Math.random() * 15;
            }
        }
    }

}
