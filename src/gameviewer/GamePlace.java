package gameviewer;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a place on the {@link GameField}. A place is the smallest unit
 * of the field, where (at most) one bullet can be placed.
 */
public class GamePlace extends JPanel {
	private static final long serialVersionUID = -7657078126767919378L;
	private static final Color COLOR_SAND = Color.gray;

	/**
	 * coordinates of the place
	 */
	final public int posX, posY;
	private String fieldChar;

	PlaceType placeType = PlaceType.Sand;
	boolean showShadow = false;
	boolean showSelection = false;
	boolean showRecommendation = false;

	boolean isField;


	int bullet = 0;

	/**
	 * Create a new instance with provided X and Y coordinates.
	 */
	GamePlace(int posX, int posY, boolean isField, String fieldChar) {
		this.posX = posX;
		this.posY = posY;
		this.isField = isField;
		this.fieldChar = fieldChar;
	}
	
	/**
	 * Method to paint the place inside the window. Makes use of
	 * helper methods for painting.
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (isField) {
			paintSand(g);
		} else {
			paintChar(g, this.fieldChar);
		}
		addPaintFrame(g);

		if (bullet != 0) {
			Color color;
			switch(bullet) {
				case 1: color = Color.red;break;
				case 2: color = Color.white;break;
				case 3: color = Color.black;break;
				default: color = Color.blue;
			}
			paintKangarooShape(g, color);
		}
	}

	private void paintSand(Graphics g) {
		g.setColor(COLOR_SAND);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	private void addPaintRiver(Graphics g) {
		int w = getWidth() / 5;
		g.setColor(Color.BLUE.brighter());
		g.fillRect(0, 0, w, getHeight());		
	}
	
	private void addPaintFrame(Graphics g) {
		g.setColor(COLOR_SAND.darker());
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
	}
	
	private void paintKangarooShape(Graphics g, Color color) {
		int xm = getWidth()/2;
		
		g.setColor(color);
		g.fillOval((int) (xm/3.333), (int) (xm/3.333), (int) (xm*1.5), (int) (xm*1.5));
	}

	private void paintChar(Graphics g, String string) {
		int xm = getWidth()/2;

		g.setFont(new Font("Dialog", Font.PLAIN, 30));
		g.drawString(string, (int) (xm/1.5), (int) (1.5 *  xm));
	}
}
