package core.geneticAlgorithm;



import core.ais.AISettings;
import core.gameenvironment.GameProcessor;

import java.util.LinkedList;

class Tournament {

    private final static String startingSetup = "2200033220103300111000111110001110033010223300022";
    private final static long gameTime = 20000;
    private static int tournamentNumber = 1;


    /**
     * Plays a round-robin tournament and returns the same linked list of AIs that includes the tournament results.
     *
     * @return LinkedList<AI> with the AIS.
     * @param ais AIs.
     */
    static LinkedList<AI> playTournament(LinkedList<AI> ais) {
        int gameNumber = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = i + 1; j < 8; j++) {
                AISettings settingsWhite = ais.get(i).getAiSettings();
                AISettings settingsBlack = ais.get(j).getAiSettings();
                GameProcessor currentGame = new GameProcessor(startingSetup, true, ++gameNumber, gameTime, settingsWhite, settingsBlack, true);
                // save the results to the AI object
                ais.get(i).setResults((currentGame.winner == 2), currentGame.gameState.capturedBulletsWhite - currentGame.gameState.capturedBulletsBlack, Long.bitCount(currentGame.gameState.bitmaps[2]) - Long.bitCount(currentGame.gameState.bitmaps[3]));
                ais.get(j).setResults((currentGame.winner == 3), currentGame.gameState.capturedBulletsBlack - currentGame.gameState.capturedBulletsWhite, Long.bitCount(currentGame.gameState.bitmaps[3]) - Long.bitCount(currentGame.gameState.bitmaps[2]));
            }
        }
        tournamentNumber++;
        return ais;
    }


}
