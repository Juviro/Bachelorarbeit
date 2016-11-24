package core.geneticAlgorithm;

import java.io.*;
import java.math.BigInteger;

public class TournamentEnvironment {

    private final static String WEIGHTS_FILE = "./GeneticAlgorithm/generations.csv";
    private static double[][] weightArray = new double[8][4];
    private static boolean isFirstGeneration = false;


    public static void main (String[] args) {
        mutateDouble(6, 0.1f);
//        setWeights();
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 4; j++) {
//                System.out.println(weightArray[i][j]);
//            }
//        }
//        System.out.println("test1 = " + Long.toBinaryString(Double.doubleToRawLongBits(weightArray[7][3])));
//        double test = Double.longBitsToDouble(new BigInteger(Long.toBinaryString(Double.doubleToRawLongBits(weightArray[7][3])), 2).longValue());
//        System.out.println("test2 = " + test);
    }

    private static double mutateDouble(double d, float mutationRate) {
        // make sure the mutationRate is between 0 and 1
        mutationRate = Math.max(Math.min(mutationRate, 1), 0);
        // 68.2 * 15 = 1111111111
        long mutant = (long) (d * 68.2);
        System.out.println("mutant = " + Long.toBinaryString(mutant));
        long position = 0b1000000000L;
        for (int i = 1; i < 11; i++) {
            // the chance for a bit to flip 7% * number of iteration
            if (Math.random() < 0.07 * i) {
                mutant = mutant ^ position;
            }
            position >>= 1;
        }

        System.out.println("mutant = " + Long.toBinaryString(mutant));
        System.out.println(mutant / 68.2);
        return mutant / 68.2;
    }

    private static void setWeights() {
        setLastParentWeights();
        if (!isFirstGeneration) {
            setChildWeights();
            setMutantWeights();
        }
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
                initializeKis();
                isFirstGeneration = true;
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
     * If this is the first Generation, generate random AI wieghts.
     */
    private static void initializeKis() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                weightArray[i][j] = Math.random() * 15;
            }
        }
    }

}
