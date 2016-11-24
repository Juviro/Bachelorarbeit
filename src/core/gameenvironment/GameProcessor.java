package core.gameenvironment;

import core.ais.NegamaxAI;
import core.ais.AISettings;
import core.board.GameState;
import core.board.Parser;

import java.io.FileWriter;
import java.io.IOException;

public class GameProcessor {

    private String gameLog = "";
    private int gameNumber;
    public GameState gameState;
    private NegamaxAI AIWhite;
    private NegamaxAI AIBlack;

    public long timeRemainingWhite;
    public long timeRemainingBlack;
    private long timeUsedForLastMove;

    public int numberOfVisitedNodesWhite = 0;
    public int numberOfVisitedNodesBlack = 0;

    public int winner = -1;

    /**
     *Starts a new game with the given options
     *  @param startingSetup representation of the core.board as a string
     * @param whiteStarts true if white starts
     * @param gameNumber current game number
     * @param logGames true if the games should get logged
     */
    public GameProcessor(String startingSetup, boolean whiteStarts, int gameNumber, long gameTime, AISettings settingsWhite, AISettings settingsBlack, boolean logGames) {
        this.gameNumber = gameNumber;

        // Initialize AIs
        AIWhite = new NegamaxAI(settingsWhite);
        AIBlack = new NegamaxAI(settingsBlack);

        // set game time
        AIWhite.timeTotal = gameTime;
        AIBlack.timeTotal = gameTime;

        // basic game setup
        long[] bitmaps = createBitmaps(startingSetup);
        int startingPlayer = (whiteStarts ? 2 : 3);
        timeRemainingWhite = gameTime;
        timeRemainingBlack = gameTime;

        // initialize the game
        gameState = new GameState(startingPlayer);
        gameState.bitmaps = bitmaps;
        int player = (whiteStarts ? 2 : 3);

        // log the starting setup
        firstLog(startingSetup, player);

        // start the game and play it until it's over
        while (gameState.gameWinner == -1) {
            gameState = performMove(gameState);
            if (logGames) {
                logState(gameState);
            }
//            if (gameState.turn > 200) {
//                break;
//            }
            for (int i = 0; i < 4; i++) {
                if (Long.numberOfLeadingZeros(gameState.bitmaps[i]) < 15) {
                    System.out.println(i);
                    System.out.println(gameState.bitmaps[i]);
                    gameState.printField();
                }
            }
        }

        if (logGames) {
            // save the log as a .csv file
            saveLog();
        }
        setNumberOfVisitedNodes();
        winner = gameState.gameWinner;
        System.out.println("Player " + winner + " won game " + gameNumber + "!");
    }

    /**
     * Performs a move and updates the remaining time.
     * @param state current state
     * @return the new state after the move
     */
    private GameState performMove(GameState state) {
        int player = state.activePlayer;
        GameState newGameState;
        long currentTime = System.currentTimeMillis();

        switch (player) {
            case 2: newGameState = performMoveNegamaxAI(state, timeRemainingWhite, AIWhite);
                break;
            case 3: newGameState = performMoveNegamaxAI(state, timeRemainingBlack, AIBlack);
                break;
            default: newGameState = state;
        }

        // Update the remaining time.
        timeUsedForLastMove = (System.currentTimeMillis() - currentTime);
        switch (player) {
            case 2: timeRemainingWhite -= timeUsedForLastMove; break;
            case 3: timeRemainingBlack -= timeUsedForLastMove; break;
        }

        // Check if a player has run out of time.
        if (timeRemainingWhite < 0) {
            newGameState.gameWinner = 3;
        } else if (timeRemainingBlack < 0) {
            newGameState.gameWinner = 2;
        }

        return newGameState;
    }


    // ########################################################## AIs ##############################################################

    /**
     * AI based on a negamax algorithm with alpha beta pruning, time management, dynamic depth for the negamax algorithm and improved rating function.
     *
     * @param state current state
     * @param timeRemaining time remaining
     * @return the new state after the move
     */
    private GameState performMoveNegamaxAI(GameState state, long timeRemaining, NegamaxAI ai){
        return ai.performMove(state,timeRemaining);
    }

    // ########################################################## logs ##############################################################

    /**
     * after every move, logs the current state
     * @param gameState current state
     */
    private void logState(GameState gameState) {
        gameLog += (gameState.turn + 1) + ";" + gameState.activePlayer + ";" + Parser.bitboardToString(gameState.bitmaps) + ";" + Parser.moveToString(gameState.lastMove) + ";" + gameState.capturedBulletsWhite + ";" + gameState.capturedBulletsBlack + ";" + timeRemainingWhite + ";" + timeRemainingBlack + ";" + timeUsedForLastMove + "\n";
    }

    /**
     * logs the starting setup for the game
     * @param position the starting position
     * @param player the starting player
     */
    private void firstLog(String position, int player){
        gameLog += "MoveNr;activePlayer;fieldString;lastMove;bulletsCapturedWhite;bulletsCapturedBlack;timeRemainingWhite;timeRemainingBlack;timeUsedForLastMove\n";
        gameLog += "1;" + player + ";" + position + ";;0;0;" + timeRemainingWhite + ";" + timeRemainingBlack + "\n";
    }

    /**
     * Save the String log to a .csv file.
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

    private void setNumberOfVisitedNodes() {
        numberOfVisitedNodesWhite = AIWhite.numberOfVisitedNodes;
        numberOfVisitedNodesBlack = AIBlack.numberOfVisitedNodes;
    }

    // ########################################################## setup ##############################################################

    /**
     * creates the bitmaps to initialize the first gameState
     * @param startingSetup starting setup as String, for example "2200033220103300111000111110001110033010223300022"
     * @return bitmaps (all bullets, red bullets, white bullets, black bullets)
     */
    private long[] createBitmaps(String startingSetup) {
        return Parser.stringToBitboard(startingSetup);
    }
}
