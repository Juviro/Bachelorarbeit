package core.benchmarks;

import core.ais.AISettings;
import core.gameenvironment.GameProcessor;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Compares the different states for the negamax AI.
 */
public class PerformanceComparison {

    public static void main (String[] args) {
        int numberOfGames = 100;

        AISettings settingsWhite = new AISettings(true, true, true, true);
        AISettings settingsBlack = new AISettings(true, true, true, true);
        //AISettings settingsBlack = new AISettings(true, true, true, true, new double[]{367., 215., 303., 0., 135., 150.});

        compareAIs(settingsWhite, settingsBlack, numberOfGames, true);
    }


    private static void compareAIs (AISettings settingsWhite, AISettings settingsBlack, int numberOfGames, boolean whiteStarts) {

        int wonGamesWhite = 0;
        int wonGamesBlack = 0;
        long timeUsedWhite = 0;
        long timeUsedBlack = 0;
        int numberOfVisitedNodesWhite = 0;
        int numberOfVisitedNodesBlack = 0;
        int turns = 0;
        String startingSetup = "2200033220103300111000111110001110033010223300022";
        long gameTime = 20000;
        String gameLog = "Game number;turns;winner;captured bullets white;captured bullets black;remaining bullets white; remaining bullets black;time remaining white; time remaining black; nodes visited white; nodes visited black\n";
        for (int i = 0; i < numberOfGames; i++) {
            GameProcessor currentGame = new GameProcessor(startingSetup, whiteStarts, (i + 1), gameTime, settingsWhite, settingsBlack, true);
            whiteStarts = !whiteStarts;
            switch(currentGame.winner) {
                case 2: wonGamesWhite++;break;
                case 3: wonGamesBlack++;break;
            }
            int remainingBulletsWhite = Long.bitCount(currentGame.gameState.bitmaps[2]);
            int remainingBulletsBlack = Long.bitCount(currentGame.gameState.bitmaps[3]);
            timeUsedWhite += ((gameTime - currentGame.timeRemainingWhite) / numberOfGames);
            timeUsedBlack += ((gameTime - currentGame.timeRemainingBlack) / numberOfGames);
            numberOfVisitedNodesWhite += (currentGame.numberOfVisitedNodesWhite / numberOfGames);
            numberOfVisitedNodesBlack += (currentGame.numberOfVisitedNodesBlack / numberOfGames);
            turns += currentGame.gameState.turn;

            if (currentGame.timeRemainingWhite < 0) {
                System.out.println("white lost via time");
            } else if (currentGame.timeRemainingBlack < 0) {
                System.out.println("black lost via time");
            }

            gameLog += (i + 1) + ";" + currentGame.gameState.turn + ";" + currentGame.gameState.gameWinner + ";" + currentGame.gameState.capturedBulletsWhite + ";" + currentGame.gameState.capturedBulletsBlack + ";" + remainingBulletsWhite + ";" + remainingBulletsBlack + ";";
            gameLog += currentGame.timeRemainingWhite + ";" + currentGame.timeRemainingBlack + ";" +  currentGame.numberOfVisitedNodesWhite + ";" + currentGame.numberOfVisitedNodesBlack + "\n";
        }



        String logStats = "Player;white;black\n";
        logStats += "Games won total;"+  wonGamesWhite + ";" + wonGamesBlack + "\n";
        logStats += "Games won percent;"+  ((float) wonGamesWhite / (float) (wonGamesBlack + wonGamesWhite)) + ";" + ((float) wonGamesBlack / (float) (wonGamesBlack + wonGamesWhite)) + "\n";
        logStats += "Time used/game;" + timeUsedWhite  + ";" + timeUsedBlack  + "\n";
        logStats += "Nodes visited/gamed;" + numberOfVisitedNodesWhite  + ";" + numberOfVisitedNodesBlack + "\n";
        logStats += "alphaBetaPruningEnabled;" + settingsWhite.alphaBetaPruningEnabled  + ";" + settingsBlack.alphaBetaPruningEnabled + "\n";
        logStats += "moveSortEnabled;" + settingsWhite.moveSortEnabled  + ";" + settingsBlack.moveSortEnabled + "\n";
        logStats += "quiescenceEnabled;" + settingsWhite.quiescenceEnabled  + ";" + settingsBlack.quiescenceEnabled + "\n";
        logStats += "timeManagementEnabled;" + settingsWhite.timeManagementEnabled  + ";" + settingsBlack.timeManagementEnabled + "\n";
        logStats += "\n\naverage turns;" + (turns / numberOfGames) + "\n";

        String completeLog = gameLog + "\n\n\n" + logStats;


        // save the settings as a string
        String whiteSettings = "";
        String blackSettings = "";

        whiteSettings += (settingsWhite.alphaBetaPruningEnabled ? "1" : "0");
        whiteSettings += (settingsWhite.moveSortEnabled ? "1" : "0");
        whiteSettings += (settingsWhite.quiescenceEnabled ? "1" : "0");
        whiteSettings += (settingsWhite.timeManagementEnabled ? "1" : "0");

        blackSettings += (settingsBlack.alphaBetaPruningEnabled ? "1" : "0");
        blackSettings += (settingsBlack.moveSortEnabled ? "1" : "0");
        blackSettings += (settingsBlack.quiescenceEnabled ? "1" : "0");
        blackSettings += (settingsBlack.timeManagementEnabled ? "1" : "0");

        saveLog(completeLog, whiteSettings, blackSettings);
    }


    /**
     * save the String log to a .csv
     */
    private static void saveLog(String gameLog, String whiteSettings, String blackSettings) {
        String ais = whiteSettings + " vs " + blackSettings;
        FileWriter writer;
        try {
            writer = new FileWriter("logs/benchmark " + ais + ".csv");
            writer.append(gameLog);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
