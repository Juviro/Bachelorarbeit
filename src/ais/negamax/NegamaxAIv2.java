package ais.negamax;

import board.GameState;
import board.Move;

import java.util.LinkedList;

/**
 * Negamax AI with alpha beta pruning
 *
 *
 */
public class NegamaxAIv2 {
    private static final int DEPTH = 6;
    private LinkedList<Move> bestMoves = new LinkedList<>();
    public int numberOfVisitedNodes = 0;


    public GameState performMove(GameState state) {
        // if you can't make a move, you lost the game
        if (state.getAllMoves(state.activePlayer).isEmpty()){
            state.gameWinner = (state.activePlayer == 2 ? 3 : 2);
        }
        negamax(state, DEPTH, state.activePlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Move bestMove = bestMoves.get((int) (Math.random() * bestMoves.size()));
        return state.executeMove(bestMove);
    }

    /**
     * Negamax algorithm
     *
     * @param state current state
     * @param depth goes from DEPTH to 0
     * @param color 2 for white, 3 for black
     * @return current rating
     */
    private double negamax(GameState state, int depth, int color, double alpha, double beta) {
        numberOfVisitedNodes++;
        // If the game is over, return a high value. This value is increased based on how deep we're currently.
        if (state.gameWinner != -1) {
            if (state.gameWinner == color) {
                return 1000 + depth * 100;
            } else {
                return -1000 + depth * 100;
            }
        }
        // rate if we found our depth goal
        if (depth == 0) {
            return Ratings.rateState(state, color);
        }

        double bestValue = Double.NEGATIVE_INFINITY;
        LinkedList<Move> moves = state.getAllMoves(state.activePlayer);
        moves = sortMoves(moves);
        for (Move move : moves) {
            double v;

            // Check if the move is a capture move. If it is, the player doesn't change.
            if (!move.isCaptureMove) {
                int nextColor = (color == 2 ? 3 : 2);
                v = -negamax(state.executeMove(move), depth - 1, nextColor, -beta, -alpha);
            } else {
                v = negamax(state.executeMove(move), depth - 1, color, alpha, beta);
            }
            // save all equally rated moves to make the movement choice non-deterministic
            if (v == bestValue && depth == DEPTH) {
                bestMoves.add(move);
            } else if (v > bestValue && depth == DEPTH) {
                bestMoves =  new LinkedList<>();
                bestMoves.add(move);
                bestValue = v;
            }
            bestValue = Double.max(bestValue, v);
            alpha = Double.max(alpha, v);
            if (alpha > beta) {
                break;
            }
        }
        return bestValue;
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



