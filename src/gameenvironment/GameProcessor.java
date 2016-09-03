package gameenvironment;

import ais.DummyAI;
import ais.NegamaxAI;
import board.GameState;
import board.Parser;

import java.io.FileWriter;
import java.io.IOException;

class GameProcessor {

    // headline for the logging
    private String gameLog = "MoveNr.;activePlayer;fieldString;lastMove;bulletsCapturedWhite;bulletsCapturedBlack\n";

    private int gameNumber;

    private NegamaxAI negamaxAI = new NegamaxAI();

    GameProcessor(String startingSetup, boolean whiteStarts, int gameNumber) {
        this.gameNumber = gameNumber;
        long[] bitmaps = createBitmaps(startingSetup);
        int startingPlayer = (whiteStarts ? 2 : 3);

        // initialize the game
        GameState gameState = new GameState(startingPlayer);
        gameState.bitmaps = bitmaps;

        int player = (whiteStarts ? 2 : 3);
        firstLog(startingSetup, player);

        // start the game and play it, until it's over
        while (gameState.gameWinner == -1) {
            switch (gameState.activePlayer) {
                case 2: gameState = performMoveWhite(gameState); break;
                case 3: gameState = performMoveBlack(gameState); break;
            }
            logState(gameState);
        }

        // save the log as a .csv file
        saveLog();
        System.out.println("Player " + gameState.gameWinner + " won the Game!");
    }

    /**
     * performs a move for white
     * @param state current state
     * @return the new state after the move
     */
    private GameState performMoveWhite(GameState state) {
        return performMoveNegamaxAI(state);
    }

    /**
     * performs a move for black
     * @param state current state
     * @return the new state after the move
     */
    private GameState performMoveBlack(GameState state) {
        return performMoveDummyKi(state);
    }


    /**
     * dummy AI for test purposes. Executes a random legal move
     *
     * @param state current state
     * @return the new state after the move
     */
    private GameState performMoveDummyKi(GameState state) {
        return DummyAI.performMove(state);
    }

    /**
     * AI based on a negamax algorithm
     *
     * @param state current state
     * @return the new state after the move
     */
    private GameState performMoveNegamaxAI(GameState state){
        return negamaxAI.performMove(state);
    }

    /**
     * after every move, logs the current state
     * @param gameState current state
     */
    private void logState(GameState gameState) {
        gameLog += (gameState.turn + 1) + ";" + gameState.activePlayer + ";" + Parser.bitboardToString(gameState.bitmaps) + ";" + Parser.moveToString(gameState.lastMove) + ";" + gameState.capturedBulletsWhite + ";" + gameState.capturedBulletsBlack + "\n";
    }

    /**
     * logs the starting setup for the game
     * @param position the starting position
     * @param player the starting player
     */
    private void firstLog(String position, int player){
        gameLog += "1;" + player + ";" + position + ";;0;0\n";
    }

    /**
     * save the String log to a .csv
     */
    private void saveLog() {
        FileWriter writer;
        try {
            writer = new FileWriter("logs/game" + gameNumber + ".csv");
            writer.append(gameLog);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the bitmaps to initialize the first gameState
     * @param startingSetup starting setup as String, for example "2200033220103300111000111110001110033010223300022"
     * @return bitmaps (all bullets, red bullets, white bullets, black bullets)
     */
    private long[] createBitmaps(String startingSetup) {
        return Parser.stringToBitboard(startingSetup);
    }
}
