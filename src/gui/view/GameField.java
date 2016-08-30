package gui.view;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Displays the actual field of the game, that is,
 * the area where the kangaroos can move around.
 */
public class GameField extends JPanel {
	private static final long serialVersionUID = 2202084251467856749L;
	
	/**
	 * A field consists of a bunch of places (e.g. 7 x 7 places)
	 */
	final private GamePlace[][] field = new GamePlace[7][7];
	final private JPanel gameField = new JPanel();

	final private List<FieldListener> fieldListeners = new LinkedList<>();
	final private PlaceListener placeListener = new PlaceListener();

	/**
	 * Creates a new GameField, adds necessary GUI elements to it,
	 * and initializes it with blank {@link GamePlace}s.
	 */
	public GameField() {
		createGui();
		initBoard();
		this.addComponentListener(new SizeListener());
	}

	
	public JComponent getFieldView() {
		return gameField;
	}

	/**
	 * Creates a new GameField and adds necessary GUI elements to it.
	 */
	private void createGui() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setPreferredSize(new Dimension(300,300));
		this.add(gameField);
		
		gameField.setLayout(new GridLayout(8, 8));
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++) {
				if(i == 0 || j % 8 == 7) {
					String fieldChar = "";
					if(i == 0) {
						switch (j) {
							case 0: fieldChar = "a";break;
							case 1: fieldChar = "b";break;
							case 2: fieldChar = "c";break;
							case 3: fieldChar = "d";break;
							case 4: fieldChar = "e";break;
							case 5: fieldChar = "f";break;
							case 6: fieldChar = "g";break;
						}
					} else {
						fieldChar = String.valueOf(i);
					}
					GamePlace place = new GamePlace(j, i, false, fieldChar);
//					field[j][i] = place;
					gameField.add(place);
				} else {
					GamePlace place = new GamePlace(j, i, true, "");
					field[j][i - 1] = place;
					gameField.add(place);
				}
			}
	}

	public void setBullet(int x, int y, int color) {
		field[y][x].bullet = color;
	}

	/**
	 * Initializes field with blank {@link GamePlace}s.
	 */
	private void initBoard() {
		for (int x=0; x<7; x++)
			for (int y=0; y<7; y++) {
				field[x][y].placeType = PlaceType.Sand;
//				field[x][y].kangaroo = null;
				field[x][y].showShadow = false;
				field[x][y].showSelection = false;
				field[x][y].showRecommendation = false;
			}
		
//		for (Position w : Board.water)
//			field[w.x][w.y].placeType = PlaceType.Water;
//
//		for (Position w : Board.riverLeft)
//			field[w.x][w.y].placeType = PlaceType.RiverLeft;
	}
	
	/**
	 * Resizes the GameField such that the places stay (nearly) quadratic.
	 */
	private void makeQuadratic() {
		int h = Math.min(getWidth(), getHeight())-30;
		int w = (int) (h*1.1);
		Dimension dimension = new Dimension(w,h);
		gameField.setPreferredSize(dimension);
		gameField.revalidate();
	}

	public void setBoard(int[][] boardValues) {
		clearAllBullets();
		for (int i = 0; i < boardValues.length; i++) {
			for (int i1 = 0; i1 < boardValues[i].length; i1++) {
				setBullet(i, i1, boardValues[i][i1]);
				field[i][i1].repaint();
			}
		}
	}

	private void clearAllBullets() {
		for (int i = 0; i < 7; i++) {
			for (int i1 = 0; i1 < 7; i1++) {
				setBullet(i, i1, 0);
			}
		}
	}

	class SizeListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			makeQuadratic();
		}
	}

	/**
	 * Removes all kangaroos on the board.
	 */
	public void clearKangaroos() {
		initBoard();
	}
	

	/**
	 * Adds a FieldListener to the field.
	 * @param fieldListener FieldListener to be added
	 */
	public void addFieldListener(FieldListener fieldListener) {
		fieldListeners.add(fieldListener);
	}
	
	/**
	 * Removes a FieldListener from the field.
	 * @param fieldListener FieldListener to be removed
	 */
	public void removeFieldListener(FieldListener fieldListener) {
		fieldListeners.remove(fieldListener);
	}
	

	/**
	 * Informs all FieldListeners that a field was clicked
	 * @param x X coordinate of the clicked field
	 * @param y Y coordinate of the clicked field
	 */
	protected void fireFieldClicked(int x, int y) {
		for (FieldListener l : fieldListeners)
			l.fieldClicked(x, y);
	}
	
	/**
	 * Informs all FieldListeners that a field was entered
	 * @param x X coordinate of the entered field
	 * @param y Y coordinate of the entered field
	 */
	protected void fireFieldEntered(int x, int y) {
		for (FieldListener l : fieldListeners)
			l.fieldEntered(x, y);
	}
	
	/**
	 * Informs all FieldListeners that a field was exited
	 * @param x X coordinate of the exited field
	 * @param y Y coordinate of the exited field
	 */
	protected void fireFieldExited(int x, int y) {
		for (FieldListener l : fieldListeners)
			l.fieldExited(x, y);
	}
	
	
	/**
	 * Listens for mouse interaction with the Places
	 */
	class PlaceListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			GamePlace place = (GamePlace)e.getSource();
			fireFieldClicked(place.posX, place.posY);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			GamePlace place = (GamePlace)e.getSource();
			fireFieldEntered(place.posX, place.posY);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			GamePlace place = (GamePlace)e.getSource();
			fireFieldExited(place.posX, place.posY);
		}

		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}		
	}

	/**
	 * Shows a shadow at the position where the kangaroo started
	 * 
	 * @param kangaroo kangaroo that is moving
	 * @param lastPosition position where it started
	 */
//	public void setShadow(Kangaroo kangaroo, Position lastPosition) {
//		if (kangaroo == null) return;
//		int x = kangaroo.px;
//		int y = kangaroo.py;
//
//		if (lastPosition != null && !lastPosition.hasPosition(x, y)) {
//			field[x][y].kangaroo = kangaroo;
//			field[x][y].showShadow = true;
//
//			x = lastPosition.x;
//			y = lastPosition.y;
//		}
//
//		field[x][y].kangaroo = kangaroo;
//		field[x][y].showSelection = true;
//	}


	/**
	 * Displays the AI recommendation 
	 * 
	 * @param lastPosition position to display the recommendation
	 * @param benefit the benefit  for the field
	 */
//	public void indicateRecommendation(Position lastPosition, float benefit) {
//		field[lastPosition.x][lastPosition.y].showRecommendation = true;
//		field[lastPosition.x][lastPosition.y].benefit = benefit;
//	}
}
