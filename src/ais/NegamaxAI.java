package ais;

import board.GameState;
import board.Move;
import gameenvironment.Ratings;

import java.util.LinkedList;

public class NegamaxAI {
    private static final int DEPTH = 7;
    private Move bestMove;


    public GameState performMove(GameState state) {
        LinkedList<Move> moves = state.getAllMoves(state.activePlayer);
        negamax(state, DEPTH, state.activePlayer);
        return state.performMove(moves.getFirst());
    }

    /**
     * Negamax algorithm
     *
     * @param state current state
     * @param depth
     * @param color 2 for white, 3 for black
     * @return current rating
     */
    private double negamax(GameState state, int depth, int color) {
        if (depth == 0) {
            return Ratings.rateState(state, color);
        }
        double bestValue = Double.NEGATIVE_INFINITY;
        LinkedList<Move> moves = state.getAllMoves(state.activePlayer);
//        int index = 0;
        for (Move move : moves) {
//            if (depth == DEPTH) {
//                index++;
//                System.out.println("Looking at move " + index + "/" + moves.size());
//            }
            int nextColor = (color == 2 ? 3 : 2);
            double v = -negamax(state.performMove(move), depth - 1, nextColor);
            bestValue = Double.max(bestValue, v);
            if (v == bestValue) {
                bestMove = move;
            }
        }
        return bestValue;
    }
}



