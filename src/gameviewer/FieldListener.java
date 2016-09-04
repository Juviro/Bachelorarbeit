package gameviewer;


/**
 * This interface can be used to notify objects when a
 * field is clicked or the mouse hovers over it. 
 *
 */
public interface FieldListener {
	
	/**
	 * Called when a field is clicked
	 * @param x x-coordinate of clicked field
	 * @param y y-coordinate of clicked field
	 */
	public void fieldClicked(int x, int y);
	/**
	 * Called when the mouse leaves a field
	 * @param x x-coordinate of left field
	 * @param y y-coordinate of left field
	 */
	public void fieldExited(int x, int y);
	/**
	 * Called when the mouse enters a field
	 * @param x x-coordinate of entered field
	 * @param y y-coordinate of entered field
	 */
	public void fieldEntered(int x, int y);
	

}
