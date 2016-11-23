package ais;

import board.GameState;
import board.Move;

import java.util.LinkedList;

/**
 * Dummy AI that performs a random move
 */
public class DummyAI {
    public static GameState performMove(GameState state) {
        LinkedList<Move> moves = state.getAllMoves(state.activePlayer);
        if(moves.size() > 0) {
            Move move = moves.get((int) (Math.random() * moves.size()));
            return state.executeMove(move);
        } else {
            // if you can't make a move, you lost the game
            state.gameWinner = (state.activePlayer == 2 ? 3 : 2);
        }
        return state;
    }
}
