package gameviewer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Class for rendering the colors in the dropdown menus and tables.
 * See Interfaces ListCellRenderer and TableCellRenderer
 * for further information.
 */
public class ColorCellRenderer extends JLabel implements ListCellRenderer<Color>, TableCellRenderer {
	private static final long serialVersionUID = -6250974769304600554L;

	public ColorCellRenderer() {
		super();
		setOpaque(true);
		setPreferredSize(new Dimension(30, 20));
		setHorizontalAlignment(CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Color> list, Color value,
			int index, boolean isSelected, boolean hasFocus) {
		
		setIcon(list.getBackground(), list.getSelectionBackground(), value, isSelected, hasFocus);
		
		return this;
	}


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		setIcon(table.getBackground(), table.getSelectionBackground(), (Color)value, isSelected, hasFocus);
		
		return this;
		
	}
	
	/**
	 * Sets the icon (concrete: ColorIcon) which then will be rendered.
	 * 
	 * @param background background color
	 * @param selection selection color
	 * @param value actual color to display
	 * @param isSelected indicates whether the user selected the current entry
	 * @param hasFocus indicates whether the user
	 * @return a self-reference to this ColorCellRenderer object
	 */
	private Component setIcon(Color background, Color selection, Color value,
			boolean isSelected, boolean hasFocus) {
		
		// set icon and background
		setIcon(new ColorIcon(25, 13, value));
		if (isSelected || hasFocus) {
			setBackground(selection);
		} else {
			setBackground(background);
		}

		return this;
	}
	
	/**
	 * Represents the icon which will be painted when
	 * a component is rendered with ColorCellRenderer.
	 * The paintIcon method draws the actual symbol.
	 */
	static class ColorIcon implements Icon {
		private final int FRAMESIZE = 1;
		private int width;
		private int height;
		private Color color;

		public ColorIcon(int width, int height, Color color) {
			this.width = width;
			this.height = height;
			this.color = color;
		}

		@Override
		public int getIconHeight() {
			return this.height;
		}

		@Override
		public int getIconWidth() {
			return this.width;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(Color.BLACK);
			g.fillRect(x, y, width, height);
			g.setColor(color);
			g.fillRect(x + FRAMESIZE, y + FRAMESIZE, width - 2 * FRAMESIZE,
					height - 2 * FRAMESIZE);
		}
	}


}
