package core.ais;

import core.board.GameState;
import core.board.Move;

import java.util.LinkedList;

import static java.lang.Double.max;
import static java.lang.Double.min;

/**
 * Same as V3, but with improved rating functions
 *
 */
public class NegamaxAI {
    private static final int AVERAGE_TURNS_PER_GAME = 60;

    private int current_max_depth = 7;
    public int numberOfVisitedNodes = 0;
    private LinkedList<Move> bestMoves = new LinkedList<>();

    private AISettings settings;

    public long timeTotal;
    private long timeStarted;
    private long timeRemaining;
    private int activePlayer;
    private Ratings ratings;

    public NegamaxAI (AISettings settings) {
        this.settings = settings;
        this.ratings = new Ratings(settings.weights);
    }


    public GameState performMove(GameState state, long timeRemaining) {
        activePlayer = state.activePlayer;
        this.timeRemaining = timeRemaining;

        // if you can't make a move, you lost the game
        if (state.getAllMoves(activePlayer).isEmpty()){
            state.gameWinner = (activePlayer == 2 ? 3 : 2);
        }

        if (settings.timeManagementEnabled) {
            // Keep track of the time we spent already for this move to not use too much.
            timeStarted = System.currentTimeMillis();
            // Adjust the current current_max_depth based on the time used already and the time remaining.
            setMaxDepth(timeRemaining, state.turn);
        } else {
            current_max_depth = 5;
        }

        // Perform the search for the best move with the negamax algorithm.
        negamax(state, current_max_depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        // Take a random move from all of the equal best moves.
        Move bestMove = bestMoves.get((int) (Math.random() * bestMoves.size()));
        return state.executeMove(bestMove);
    }

    /**
     * Negamax algorithm
     *
     * @param state current state
     * @param depth goes from current_max_depth to 0
     * @param alpha alpha, only if alpha beta pruning is enabled
     * @param beta beta, only if alpha beta pruning is enabled
     * @return current heuristic rating
     */
    private double negamax(GameState state, int depth, double alpha, double beta) {
        numberOfVisitedNodes++;
        int color = state.activePlayer;

        // If the game is over, return a high value. This value is increased based on how deep we're currently.
        if (state.gameWinner != -1) {
            if (state.gameWinner == color) {
                return 100000 + depth * 10000;
            } else {
                return -(100000 + depth * 10000);
            }
        }

        // Rate if we found our depth goal.
        // If the last move was a capture move and quiescence search is enabled, perform another iteration instead (maximum five times to not go too deep).
        if ((depth <= 0 && !(state.lastMove.isCaptureMove && settings.quiescenceEnabled)) || depth < -4) {
            return ratings.rateState(state, color);
        }

        double bestValue = Double.NEGATIVE_INFINITY;
        LinkedList<Move> moves = state.getAllMoves(state.activePlayer);
        if (settings.moveSortEnabled) {
            moves = sortMoves(moves);
        }
        for (Move move : moves) {
            double v;

            // Check if the move is a capture move. If it is, the player doesn't change.
            if (!move.isCaptureMove) {
                v = -negamax(state.executeMove(move), depth - 1, -beta, -alpha);
            } else {
                v = negamax(state.executeMove(move), depth - 1, alpha, beta);
            }

            // captureMoves are nearly always better, therefor they weight more
            if (move.isCaptureMove && depth == current_max_depth) {
                double multiplier = (activePlayer == color ? 1.5 : 0.5);
                v *= multiplier;
            }

            // save all equally rated moves to make the movement choice non-deterministic
            if (v == bestValue && depth == current_max_depth) {
                bestMoves.add(move);
            } else if (v > bestValue && depth == current_max_depth) {
                bestMoves =  new LinkedList<>();
                bestMoves.add(move);
                bestValue = v;
            }
            bestValue = max(bestValue, v);
            alpha = max(alpha, v);

            // If time management is enabled, stop the search if it's taking too much time.
            if (settings.timeManagementEnabled && ((System.currentTimeMillis() - timeStarted) > 0.2 * timeRemaining) ){
                if (depth == current_max_depth) {
                    current_max_depth--;
                }
                break;
            } else if (alpha > beta && settings.alphaBetaPruningEnabled) {
                break;
            }
        }
        return bestValue;
    }

    /**
     * Increase/decrease the current_max_depth dynamically based on how much time has been used already and how many further turns are expect to be played.
     * current_max_depth ranges from 7 to 9.
     * Fixed current_max_depth between 5 and 7 if 10% or less time remains.
     *
     * @param timeRemaining time remaining for the match in milliseconds
     * @param turn number of current turn
     */
    private void setMaxDepth(long timeRemaining, int turn) {
        double percentageTimeUsed = 1 - ((double) timeRemaining / (double) timeTotal);
        double estimatedTurnPercentage = min((double) turn / (double) AVERAGE_TURNS_PER_GAME, 1);

        if (percentageTimeUsed > 0.995) {
            current_max_depth = 5;
        } else if (percentageTimeUsed > 0.98) {
            current_max_depth = 6;
        } else if (percentageTimeUsed > 0.9) {
            current_max_depth = 7;
        } else if (percentageTimeUsed < estimatedTurnPercentage * 0.9 && current_max_depth < 9) {
            current_max_depth++;
        } else if (percentageTimeUsed > estimatedTurnPercentage && current_max_depth > 8) {
            current_max_depth--;
        }
    }

    /**
     * Takes a LinkedList of moves and returns a sorted list.
     * Only puts captureMoves to the beginning.
     *
     * @param moves LinkedList of moves to sort.
     * @return Sorted List
     */
    private LinkedList<Move> sortMoves(LinkedList<Move> moves) {
        LinkedList<Move> sortedList = new LinkedList<>();
        for (int i = 0; i < moves.size(); i++) {
            if(moves.get(i).isCaptureMove) {
                sortedList.add(moves.get(i));
                moves.remove(i);
                i--;
            }
        }
        sortedList.addAll(moves);
        return sortedList;
    }

}



