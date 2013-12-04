package canvas;

/**
 * Interface representing various drawing objects that represent marks on the canvas.
 *
 */
public interface DrawingObject {
	
	/**
	 * returns a String representation of a DrawingObject as a list of alternating x and y values
	 * followed by a color and a thickness.
	 */
	public String toString();
	
}