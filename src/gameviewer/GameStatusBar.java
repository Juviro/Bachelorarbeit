package gameviewer;



import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Shows the current game status
 */
public class GameStatusBar extends JPanel {
	private static final long serialVersionUID = -7318955478474170079L;
	
	private final JLabel lblStatusText = new JLabel(" ");
	private final JButton btnShowRecommendation = new JButton("Zeige Vorschlag");
	private final JProgressBar pgbShowRecommendation = new JProgressBar();

	
	/**
	 * Creates a new GameStatusBar
	 */
	public GameStatusBar() {
		createGUI();
	}

	private void createGUI() {
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		setLayout(new BorderLayout());
		
		
		lblStatusText.setHorizontalAlignment(JLabel.CENTER);
		lblStatusText.setText("Ready to start.");
		
		btnShowRecommendation.setPreferredSize(new Dimension(140,15));
		btnShowRecommendation.setEnabled(false);
		
		pgbShowRecommendation.setStringPainted(false);
		pgbShowRecommendation.setValue(0);
		pgbShowRecommendation.setMinimum(0);
		pgbShowRecommendation.setMaximum(100);
		pgbShowRecommendation.setPreferredSize(new Dimension(140,5));
		
		add(lblStatusText, BorderLayout.CENTER);
		JPanel aiPanel = new JPanel();
		aiPanel.setLayout(new BorderLayout());
		aiPanel.add(pgbShowRecommendation, BorderLayout.NORTH);
		aiPanel.add(btnShowRecommendation, BorderLayout.CENTER);
		add(aiPanel, BorderLayout.EAST);
		
		setPreferredSize(new Dimension(0, 30));
	}
	
	/**
	 * Shows the specified message
	 * @param text message to show
	 */
	public void setMessage(String text) {
		lblStatusText.setText(text);
	}

	
	
	
	
	public void setCountDownValue(int number) {
		if (number > 0) {
			btnShowRecommendation.setEnabled(false);
			btnShowRecommendation.setText("Vorschlag in "+ number + "s.");
		}

		if (number <= 0) {
			btnShowRecommendation.setText("Zeige Vorschlag");
			btnShowRecommendation.setEnabled(true);
		}
	}
	
	public void resetRecommendation() {
		btnShowRecommendation.setEnabled(false);
		btnShowRecommendation.setText("Zeige Vorschlag");
	}

	
	public void addRecommendationButtonListener(ActionListener listener) {
		btnShowRecommendation.addActionListener(listener);
	}

	public void removeRecommendationButtonListener(ActionListener listener) {
		btnShowRecommendation.removeActionListener(listener);
	}

	
}
