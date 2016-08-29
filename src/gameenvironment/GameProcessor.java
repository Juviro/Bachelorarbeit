package gameenvironment;

import board.GameState;
import board.Move;
import board.Parser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

class GameProcessor {

    // log the game process
    private String gameLog = "MoveNr.;activePlayer;fieldString;lastMove\n";

    private int gameNumber;

    GameProcessor(String startingSetup, boolean whiteStarts, int gameNumber) {
        this.gameNumber = gameNumber;
        long[] bitmaps = createBitmaps(startingSetup);
        int startingPlayer = (whiteStarts ? 2 : 3);

        // initialize the game
        GameState gameState = new GameState(bitmaps, startingPlayer);

        int player = (whiteStarts ? 2 : 3);
        firstLog(startingSetup, player);

        // start the game and play it, until it's over
        while (gameState.gameWinner == -1) {
            switch (gameState.activePlayer) {
                case 2: gameState = performMoveWhite(gameState); break;
                case 3: gameState = performMoveBlack(gameState); break;
            }
//            gameState.printStats();
//            gameState.getAllMoves(gameState.activePlayer).forEach(System.out::println);
//            gameState.printField();
            logState(gameState);
        }
        saveLog();
        System.out.println("Player " + gameState.gameWinner + " won the Game!");
    }

    private GameState performMoveWhite(GameState state) {
        return performMoveDummyKi(state);
    }

    private GameState performMoveBlack(GameState state) {
        return performMoveDummyKi(state);
    }



    private GameState performMoveDummyKi(GameState state) {
        LinkedList<Move> moves = state.getAllMoves(state.activePlayer);
        if(moves.size() > 0) {
            Move move = moves.get((int) (Math.random() * moves.size()));
            return state.performMove(move);
        } else {
            state.gameWinner = 5;
            System.out.println("NO MORE MOVES POSSIBLE");
        }
        return state;
    }

    private void logState(GameState gameState) {
        gameLog += (gameState.turn + 1) + ";" + gameState.activePlayer + ";" + Parser.bitboardToString(gameState.bitmaps) + ";" + Parser.moveToString(gameState.lastMove) + "\n";
    }

    private void firstLog(String position, int player){
        gameLog += "1;" + player + ";" + position + ";\n";
    }

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

    private long[] createBitmaps(String startingSetup) {
        return Parser.stringToBitboard(startingSetup);
    }
}
