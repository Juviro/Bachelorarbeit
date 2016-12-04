package core.geneticAlgorithm;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class GeneticAlgorithmMain {

    private static LinkedList<AI> AIs = new LinkedList<>();
    private static int lastGameFileIndex = 0;


    public static void main (String[] args) {
        processGames();
    }


    /**
     * Generates a generation, plays a tournament, saves the generation to a .csv and then starts again.
     */
    private static void processGames() {
        setWeights();
        AIs = Tournament.playTournament(AIs);
        saveGenerationLog();
        AIs.forEach(System.out::println);
        AIs = new LinkedList<>();
        processGames();
    }


    /**
     * Sets the weights for the eight AIs of a single generation.
     * At first, tries to find the last existing csv file to get the weights out of it.
     * If there is no previous .csv, generates random values for the AIs.
     */
    private static void setWeights() {
        // get the file of the last generation
        while(new File("./GeneticAlgorithm/gen" + (lastGameFileIndex + 1) + ".csv").isFile()) {
            lastGameFileIndex++;
        }
        // initialize AI with random values if there is no previous generation
        if(lastGameFileIndex == 0) {
            initializeAis();
        } else {
            generateAIs();
        }
    }

    /**
     * Generates a new generation of AIs. Ech generation consists of 8 AIs
     *
     * 2 AIs from the last generation with the highest fitness (parents).
     * 2 AIs are children from the parent AIs, one of them is mutated @see crossoverParents()
     * 2 AIs are generated by mutating the parents @see mutateParents()
     * 2 AIs are generated by mutating AIs from the last generation (excluding the chosen parents) @see generateMutants();
     */
    private static void generateAIs() {
        try {
            File file = new File("./GeneticAlgorithm/gen" + lastGameFileIndex + ".csv") ;
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            br.readLine();
            String parent1 = br.readLine();
            String parent2 = br.readLine();

            double[] weightsParent1 = Arrays.stream(Arrays.copyOfRange(parent1.split(";"), 5, 9)).mapToDouble(Double::parseDouble).toArray();
            double[] weightsParent2 = Arrays.stream(Arrays.copyOfRange(parent2.split(";"), 5, 9)).mapToDouble(Double::parseDouble).toArray();

            AIs.add(new AI(weightsParent1[0],weightsParent1[1], weightsParent1[2], weightsParent1[3], AI.aiType.parentAI));
            AIs.add(new AI(weightsParent2[0],weightsParent2[1], weightsParent2[2], weightsParent2[3], AI.aiType.parentAI));

            crossoverParents(weightsParent1, weightsParent2);
            mutateParents(weightsParent1, weightsParent2);
            generateMutants(br);


            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates two new AIs from a crossover of the parents, one of them has two random parameters from each parent, the other one has the other parameters.
     * The second AI is mutated with a mutation rate of 0.02
     *
     * @param weightsParent1 weights of parent 1
     * @param weightsParent2 weights of parent 2
     */
    private static void crossoverParents(double[] weightsParent1, double[] weightsParent2) {
        double mutationRate = 0.05;
        int param1 = 0;
        int param2 = 0;

        // create random numbers that indicate which ai gets their parameters from which ai
        while (param1 == param2) {
            param1 = (int) (Math.random() * 4);
            param2 = (int) (Math.random() * 4);
        }

        // set the weight values
        double[] weightsAI1 = new double[4];
        double[] weightsAI2 = new double[4];
        for (int i = 0; i < 4; i++) {
            if (param1 == i || param2 == i) {
                weightsAI1[i] = weightsParent1[i];
                weightsAI2[i] = mutateDouble(weightsParent2[i], mutationRate);
            } else {
                weightsAI1[i] = weightsParent2[i];
                weightsAI2[i] = mutateDouble(weightsParent1[i], mutationRate);
            }
        }
        AI ai1 = new AI(weightsAI1[0], weightsAI1[1], weightsAI1[2], weightsAI1[3], AI.aiType.childAI);
        AI ai2 = new AI(weightsAI2[0], weightsAI2[1], weightsAI2[2], weightsAI2[3], AI.aiType.childAIMutated);
        AIs.add(ai1);
        AIs.add(ai2);
    }

    /**
     * Creates two new AIs by mutating the parents with a mutation rate of 0.07.
     *
     * @param weightsParent1 weights of parent 1
     * @param weightsParent2 weights of parent 2
     */
    private static void mutateParents(double[] weightsParent1, double[] weightsParent2) {
        double mutationRate = 0.07;

        // mutate the weight values
        double[] weightsAI1 = new double[4];
        double[] weightsAI2 = new double[4];
        for (int i = 0; i < 4; i++) {
            weightsAI1[i] = mutateDouble(weightsParent1[i], mutationRate);
            weightsAI2[i] = mutateDouble(weightsParent2[i], mutationRate);
        }
        AI ai1 = new AI(weightsAI1[0], weightsAI1[1], weightsAI1[2], weightsAI1[3], AI.aiType.parentAIMutated);
        AI ai2 = new AI(weightsAI2[0], weightsAI2[1], weightsAI2[2], weightsAI2[3], AI.aiType.parentAIMutated);
        AIs.add(ai1);
        AIs.add(ai2);
    }


    /**
     * Selects two random AIs from the last generation, excluding the ones that have already been chosen.
     * Creates two new AIs from it, each parameter is mutated by a random mutation rate between 0.0 and 0.1.
     *
     * @param br BufferedReader that contains the information of the last generation.
     */
    private static void generateMutants(BufferedReader br) throws IOException {
        int param1 = 0;
        int param2 = 0;

        // create random numbers that indicate which ai gets their parameters from which ai
        while (param1 == param2) {
            param1 = (int) (Math.random() * 6);
            param2 = (int) (Math.random() * 6);
        }
        for (int i = 0; i < (Math.min(param1, param2)); i++) {
            br.readLine();
        }
        String ai = br.readLine();
        for (int i = 0; i < (Math.max(param1, param2) - (Math.min(param1, param2) + 1)); i++) {
            br.readLine();
        }
        String a2 = br.readLine();


        double[] ai1 = Arrays.stream(Arrays.copyOfRange(ai.split(";"), 5, 9)).mapToDouble(Double::parseDouble).toArray();
        double[] ai2 = Arrays.stream(Arrays.copyOfRange(a2.split(";"), 5, 9)).mapToDouble(Double::parseDouble).toArray();

        for (int i = 0; i < 4; i++) {
            double mutateRate1 = Math.random() * 0.1;
            double mutateRate2 = Math.random() * 0.1;
            mutateDouble(ai1[i], mutateRate1);
            mutateDouble(ai2[i], mutateRate2);
        }

        AIs.add(new AI(ai1[0],ai1[1], ai1[2], ai1[3], AI.aiType.mutantOther));
        AIs.add(new AI(ai2[0],ai2[1], ai2[2], ai2[3], AI.aiType.mutantOther));
    }





    /**
     * Mutate a given double with a given mutation rate.
     * @param d double
     * @param mutationRate mutation
     * @return mutated double
     */
    private static double mutateDouble(double d, double mutationRate) {
        // make sure the mutationRate is between 0 and 0.15
        mutationRate = Math.max(Math.min(mutationRate, 0.15), 0);
        // 68.2 * 15 = 0b1111111111
        d += 7.5;
        long mutant = (long) (d * 68.2);
        long position = 0b1000000000L;
        for (int i = 1; i < 11; i++) {
            // the chance for a bit to flip is mutationRate * number of iteration (maximum 90%)
            if (Math.random() < Math.min(mutationRate * i, 0.9)) {
                mutant = mutant ^ position;
            }
            position >>= 1;
        }
        double result = mutant / 68.2;
        result -= 7.5;
        return result;
    }


    /**
     * Save the results of the tournament to a new genX.csv file.
     */
    private static void saveGenerationLog() {
        String gameLog = createLog();
        FileWriter writer;
        try {
            writer = new FileWriter("./GeneticAlgorithm/gen" + (lastGameFileIndex + 1) + ".csv");
            writer.append(gameLog);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates a String that contains the data that will be written to the log file.
     * @return the log
     */
    private static String createLog() {
        Collections.sort(AIs, Collections.reverseOrder());
        String gameLog = "fitness;aiType;gamesWon;averageRedBulletDifference;averageBulletDifference;redBulletRating;bulletPredominanceRating;placementRating;stickRating\n";
        for (AI ai : AIs) {
            double redBulletRating = ai.getWeights()[0];
            double bulletPredominanceRating = ai.getWeights()[1];
            double placementRating = ai.getWeights()[2];
            double stickRating = ai.getWeights()[3];
            gameLog += ai.getFitness() + ";" + ai.getAitype() + ";" + ai.getNumberOfWins() + ";" + ai.getAverageRedBulletDifference() + ";" + ai.getAverageBulletRate() + ";" + redBulletRating + ";" + bulletPredominanceRating + ";" + placementRating + ";" + stickRating + "\n";
        }
        return gameLog;
    }

    /**
     * If this is the very first Generation, generate 8 AIs with random weights and add them to the list of AIs.
     */
    private static void initializeAis() {
        for (int i = 0; i < 8; i++) {
            double redBulletRating = Math.random() * 15 - 7.5;
            double bulletPredominance = Math.random() * 15 - 7.5;
            double placementRating = Math.random() * 15 - 7.5;
            double stickRating = Math.random() * 15 - 7.5;
            AI ai = new AI(redBulletRating, bulletPredominance, placementRating, stickRating, AI.aiType.randomAI);
            AIs.add(ai);
        }
    }
}
