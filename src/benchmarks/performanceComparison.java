package benchmarks;

import ais.other.AINames;
import gameenvironment.GameProcessor;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Compares the different states for the negamax AIs.
 */
public class PerformanceComparison {

    public static void main (String[] args) {
        compareAIs(AINames.AIs.NEGAMAXV3, AINames.AIs.NEGAMAXV2);
    }


    private static void compareAIs (AINames.AIs aiWhite, AINames.AIs aiBlack) {

        int wonGamesWhite = 0;
        int wonGamesBlack = 0;
        long timeUsedWhite = 0;
        long timeUsedBlack = 0;
        int numberOfVisitedNodesWhite = 0;
        int numberOfVisitedNodesBlack = 0;
        int turns = 0;


        boolean whiteStarts = true;
        int numberOfGames = 100;
        String startingSetup = "2200033220103300111000111110001110033010223300022";
        long gameTime = 20000;
        String gameLog = "Game number;turns;winner;captured bullets white;captured bullets black;remaining bullets white; remaining bullets black;time remaining white; time remaining black; nodes visited white; nodes visited black\n";
        for (int i = 0; i < numberOfGames; i++) {
            GameProcessor currentGame = new GameProcessor(startingSetup, whiteStarts, (i + 1),gameTime, aiWhite, aiBlack, true);
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

        String logStats = ";" + aiWhite + ";" + aiBlack + "\n";
        logStats += "Games won total;"+  wonGamesWhite + ";" + wonGamesBlack + "\n";
        logStats += "Games won percent;"+  ((float) wonGamesWhite / (float) (wonGamesBlack + wonGamesWhite)) + ";" + ((float) wonGamesBlack / (float) (wonGamesBlack + wonGamesWhite)) + "\n";
        logStats += "Time used total;" +  timeUsedWhite * numberOfGames + ";" + timeUsedBlack * numberOfGames + "\n";
        logStats += "Time used/game;" + timeUsedWhite  + ";" + timeUsedBlack  + "\n";
        logStats += "Nodes visited total;" +  numberOfVisitedNodesWhite * numberOfGames + ";" + numberOfVisitedNodesBlack * numberOfGames + "\n";
        logStats += "Nodes visited/gamed;" + numberOfVisitedNodesWhite  + ";" + numberOfVisitedNodesBlack + "\n";
        logStats += "\n\naverage turns;" + (turns / numberOfGames) + "\n";

        String completeLog = gameLog + "\n\n\n" + logStats;

        saveLog(completeLog, aiWhite, aiBlack);
    }


    /**
     * save the String log to a .csv
     */
    private static void saveLog(String gameLog, AINames.AIs aiWhite, AINames.AIs aiBlack) {
        String ais = aiWhite + " vs " + aiBlack;
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
