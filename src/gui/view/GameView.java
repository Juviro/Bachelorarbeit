package gui.view;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the part of the window the contains visualization of the game,
 * consisting of the game field in the center and the status at the right
 */
public class GameView extends JPanel {
   private static final long serialVersionUID = 746432283603174176L;


   final public GameField field = new GameField();
   final public GameStatus status = new GameStatus();
    private String[][] moves;


    public GameView() {
       createGui();
   }


   /**
    * Creates and adds necessary elements for the game view
    */
   private void createGui() {
       JPanel pnlGlueContainer = new JPanel();
       pnlGlueContainer.setLayout(new BoxLayout(pnlGlueContainer, BoxLayout.PAGE_AXIS));
       pnlGlueContainer.add(Box.createGlue());
       pnlGlueContainer.add(field);
       pnlGlueContainer.add(Box.createGlue());

       setLayout(new BorderLayout());
       add(pnlGlueContainer, BorderLayout.CENTER);
       add(status, BorderLayout.EAST);


       status.setBackground(Color.black);
       status.setPreferredSize(new Dimension(250,6000));
   }

    public void setMoves(String[][] moves) {
        this.moves = moves;
        status.setMoves(moves);
        System.out.println("moves = " + moves[0][2]);
        field.setBoard(getBoardValues(moves[0][2]));
        field.repaint();
    }

    public void changeRow(boolean up) {
        String boardRepresentation = status.changeRow(up);
        if (boardRepresentation != "") {
            field.setBoard(getBoardValues(boardRepresentation));

        }
    }

    int[][] getBoardValues(String representation) {
        return board.Parser.stringToArray(representation);
    }
}
