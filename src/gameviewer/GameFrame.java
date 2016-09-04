package gameviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The main window for the game
 */
public class GameFrame extends JFrame implements KeyListener{
	private static final long serialVersionUID = -5915673935312770910L;
	
	/**
	 * Menu bar at the top
	 */
//	public MenuBar menuBar;
	/**
	 * Status bar at the bottom
	 */
	/**
	 * Central area for the game view
	 */
	public final GameView gameView = new GameView();

	public static void main (String args[]){
		new GameFrame();
	}

	/**
	 * Creates a new instance
	 */
	public GameFrame() {
		createFrame();
	}
	
	/**
	 * Creates frame and adds its content at the correct position
	 */
	void createFrame() {
		setTitle("Akiba");

		// MenuBar layout
		final MenuBar menuBar = new MenuBar(this);
		this.add(menuBar);
		this.setJMenuBar(menuBar);
		this.addKeyListener(this);
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(gameView, BorderLayout.CENTER);		

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setSize(new Dimension(700,500));
		this.setSize(new Dimension(700,500));
	}

	public void setReplay(String[][] replay) {
		gameView.setMoves(replay);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch( keyCode ) {
			case KeyEvent.VK_UP:
				gameView.changeRow(true);
				break;
			case KeyEvent.VK_DOWN:
				gameView.changeRow(false);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
