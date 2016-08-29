package gui.view;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.parser.Parser;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

/**
 * Always shows the status of the game (on the right side)
 */
public class GameStatus extends JPanel {
	private static final long serialVersionUID = -4168809353660543331L;

	/**
	 * labels containing the status data
	 */
	final private JLabel lblLapCount = new JLabel();
	final private JLabel lblKangarooCount = new JLabel();
	final private JLabel lblGameState = new JLabel();
	final private JLabel lblActivePlayer = new JLabel();
	final private JLabel lblStartPlayer = new JLabel();
	private int selectedRow = 0;
	private int numRows = 0;

	/**
	 * table data
	 */
    final String[] columnNames = {"Zug Nr.", "Farbe", "Zug"};
	private DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	private JTable table = new JTable(tableModel);
	private String[][] moves;


	public GameStatus() {
		createGui();
	}


	/**
	 * adds necessary GUI elements to the GameStatus
	 */
	private void createGui() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));    	
		this.add(getStatusPanel());
		this.add(getPlayerContainers());
		table.setFocusable(true);
		table.setRowSelectionAllowed(true);
	}

	private void setActionListener() {
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (table.getSelectedRow() > -1) {
					// print first column value from selected row
					System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
				}
			}
		});
	}

	/**
	 * Creates and returns a panel containing all
	 * status information elements
	 * @return panel with all alements
	 */
	private JPanel getStatusPanel() {
		JPanel pnlConfig = new JPanel();
	    pnlConfig.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Informationen"));
	    pnlConfig.setLayout(new GridLayout(0,2));
	    pnlConfig.add(new JLabel("Kugeln Weiß: "));
	    pnlConfig.add(lblLapCount);
	    pnlConfig.add(new JLabel("Kugeln Schwarz: "));
	    pnlConfig.add(lblStartPlayer);
	    pnlConfig.add(new JLabel("Rote Kugeln Weiß: "));
	    pnlConfig.add(lblGameState);
	    pnlConfig.add(new JLabel("Rote Kugeln Schwarz: "));
	    pnlConfig.add(lblActivePlayer);

	    return pnlConfig;
	}
	
	/**
	 * Returns the panel containing the
	 * table with all players
	 * @return panel with all players
	 */
	private JPanel getPlayerContainers() {
		table.setEnabled(false);
		table.getColumnModel().getColumn(1).setCellRenderer(new ColorCellRenderer());
        table.getColumnModel().getColumn(1).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setMaxWidth(75);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Züge"));
        container.setLayout(new BorderLayout());
		container.add(scrollPane, BorderLayout.CENTER);

		return container;
	}


	/**
	 * Sets the game phase
	 */
	public void setGamePhase(String text) {
		lblGameState.setText(text);
	}
	/**
	 * Sets the number of laps
	 */
	public void setLapCount(String text) {
		lblLapCount.setText(text);
	}
	/**
	 * Sets the number of kangaroos
	 */
	public void setKangarooCount(String text) {
		lblKangarooCount.setText(text);
	}
	/**
	 * Sets the active player
	 */
	public void setActivePlayer(String text) {
		lblActivePlayer.setText(text);
	}

	/**
	 * Sets the start player
	 */
	public void setStartPlayer(String text) {
		lblStartPlayer.setText(text);
	}
	
	/**
	 * Sets the progress for a certain player
	 * @param name name of the player whose progress is set
	 * @param progress progress to set
	 */
	public void setPlayerProgress(String name, String progress) {
		for (int i=0; i<tableModel.getRowCount(); i++) {
			String nameRow = (String)tableModel.getValueAt(i, 0);
			if (name.equals(nameRow)) {
				tableModel.setValueAt(progress, i, 2);
			}
		}
	}


	/**
	 * Sets the players
	 * @param moves players to be set
	 */
	void setMoves(String[][] moves) {
		this.moves = moves;
		for (int i = 0; i < moves.length; i++) {
			Color color;
			if (i > 0) {
				switch (Integer.valueOf(moves[i-1][1])) {
					case 2:
						color = Color.white;
						break;
					case 3:
						color = Color.black;
						break;
					default:
						color = Color.blue;
				}
			} else {
				color = Color.gray;
			}


			tableModel.addRow(new Object[]{moves[i][0], color, moves[i][3]});
		}
		numRows = moves.length;
		selectedRow = 0;
		table.setRowSelectionInterval(0, 0);
	}


	String changeRow(boolean up) {
		if(up && selectedRow > 0) {
			selectedRow--;
			table.setRowSelectionInterval(selectedRow, selectedRow);
			return moves[selectedRow][2];
		} else if (!up && selectedRow < numRows) {
			selectedRow++;
			table.setRowSelectionInterval(selectedRow, selectedRow);
			return moves[selectedRow][2];
		}
		return "";
	}
}
