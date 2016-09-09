package gameenvironment;

import ais.dummy.DummyAI;
import ais.negamax.NegamaxAIv1;
import ais.negamax.NegamaxAIv2;
import ais.negamax.NegamaxAIv3;
import ais.negamax.NegamaxAIv4;
import ais.other.AINames;
import board.GameState;
import board.Parser;

import java.io.FileWriter;
import java.io.IOException;

public class GameProcessor {

    // headline for the logging
    private String gameLog = "MoveNr.;activePlayer;fieldString;lastMove;bulletsCapturedWhite;bulletsCapturedBlack;timeRemainingWhite;timeRemainingBlack;timeUsedForLastMove\n";

    private int gameNumber;

    public GameState gameState;

    private NegamaxAIv1 negamaxAIV1 = new NegamaxAIv1();
    private NegamaxAIv2 negamaxAIV2 = new NegamaxAIv2();
    private NegamaxAIv3 negamaxAIV3 = new NegamaxAIv3();
    private NegamaxAIv4 negamaxAIV4 = new NegamaxAIv4();

    public long timeRemainingWhite;
    public long timeRemainingBlack;
    private long timeUsedForLastMove;

    public int numberOfVisitedNodesWhite = 0;
    public int numberOfVisitedNodesBlack = 0;

    private AINames.AIs AIWhite;
    private AINames.AIs AIBlack;

    public int winner = -1;

    /**
     *Starts a new game with the given options
     *  @param startingSetup representation of the board as a string
     * @param whiteStarts true if white starts
     * @param gameNumber current game number
     * @param logGames true if the games should get logged
     */
    public GameProcessor(String startingSetup, boolean whiteStarts, int gameNumber, long gameTime, AINames.AIs AIWhite, AINames.AIs AIBlack, boolean logGames) {
        this.gameNumber = gameNumber;
        this.AIWhite = AIWhite;
        this.AIBlack = AIBlack;

        negamaxAIV3.timeTotal = gameTime;
        negamaxAIV4.timeTotal = gameTime;

        long[] bitmaps = createBitmaps(startingSetup);
        int startingPlayer = (whiteStarts ? 2 : 3);
        timeRemainingWhite = gameTime;
        timeRemainingBlack = gameTime;

        // initialize the game
        gameState = new GameState(startingPlayer);
        gameState.bitmaps = bitmaps;

        int player = (whiteStarts ? 2 : 3);
        firstLog(startingSetup, player);

        // start the game and play it, until it's over
        while (gameState.gameWinner == -1) {
            gameState = performMove(gameState);
            if (logGames) {
                logState(gameState);
            }
            if (gameState.turn > 200) {
                break;
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
        AINames.AIs currentAI = (player == 2 ? AIWhite : AIBlack);
        long timeRemaining = 0;
        switch (player) {
            case 2: timeRemaining = timeRemainingWhite;break;
            case 3: timeRemaining = timeRemainingBlack;break;
        }

        long currentTime = System.currentTimeMillis();
        switch (currentAI) {
            case DUMMY: newGameState = performMoveDummyKi(state);
                break;
            case NEGAMAXV1: newGameState = performMoveNegamaxAIV1(state);
                break;
            case NEGAMAXV2: newGameState = performMoveNegamaxAIV2(state);
                break;
            case NEGAMAXV3: newGameState = performMoveNegamaxAIV3(state, timeRemaining);
                break;
            case NEGAMAXV4: newGameState = performMoveNegamaxAIV4(state, timeRemaining);
                break;
            default: newGameState = state;
        }
        timeUsedForLastMove = (System.currentTimeMillis() - currentTime);
        switch (player) {
            case 2: timeRemainingWhite -= timeUsedForLastMove; break;
            case 3: timeRemainingBlack -= timeUsedForLastMove; break;
        }
        if (timeRemainingWhite < 0) {
            newGameState.gameWinner = 3;
        } else if (timeRemainingBlack < 0) {
            newGameState.gameWinner = 2;
        }
        return newGameState;
    }


    // ########################################################## AIs ##############################################################



    /**
     * Dummy AI for test purposes. Executes a random legal move.
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
    private GameState performMoveNegamaxAIV1(GameState state){
        return negamaxAIV1.performMove(state);
    }

    /**
     * AI based on a negamax algorithm with alpha beta pruning
     *
     * @param state current state
     * @return the new state after the move
     */
    private GameState performMoveNegamaxAIV2(GameState state){
        return negamaxAIV2.performMove(state);
    }

    /**
     * AI based on a negamax algorithm with alpha beta pruning, time management and dynamic depth for the negamax algorithm.
     *
     * @param state current state
     * @param timeRemaining time remaining
     * @return the new state after the move
     */
    private GameState performMoveNegamaxAIV3(GameState state, long timeRemaining){
        return negamaxAIV3.performMove(state,timeRemaining);
    }

    /**
     * AI based on a negamax algorithm with alpha beta pruning, time management, dynamic depth for the negamax algorithm and improved rating function.
     *
     * @param state current state
     * @param timeRemaining time remaining
     * @return the new state after the move
     */
    private GameState performMoveNegamaxAIV4(GameState state, long timeRemaining){
        return negamaxAIV4.performMove(state,timeRemaining);
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
        gameLog += "1;" + player + ";" + position + ";;0;0;" + timeRemainingWhite + ";" + timeRemainingBlack + ";\n";
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

    private void setNumberOfVisitedNodes() {
        switch (AIWhite) {
            case NEGAMAXV1: numberOfVisitedNodesWhite = negamaxAIV1.numberOfVisitedNodes;
                break;
            case NEGAMAXV2: numberOfVisitedNodesWhite = negamaxAIV2.numberOfVisitedNodes;
                break;
            case NEGAMAXV3: numberOfVisitedNodesWhite = negamaxAIV3.numberOfVisitedNodes;
                break;
            case NEGAMAXV4: numberOfVisitedNodesWhite = negamaxAIV4.numberOfVisitedNodes;
                break;
        }
        switch (AIBlack) {
            case NEGAMAXV1: numberOfVisitedNodesBlack = negamaxAIV1.numberOfVisitedNodes;
                break;
            case NEGAMAXV2: numberOfVisitedNodesBlack = negamaxAIV2.numberOfVisitedNodes;
                break;
            case NEGAMAXV3: numberOfVisitedNodesBlack = negamaxAIV3.numberOfVisitedNodes;
                break;
            case NEGAMAXV4: numberOfVisitedNodesBlack = negamaxAIV4.numberOfVisitedNodes;
                break;
        }
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
