package canvas;

import java.util.ArrayList;


/**
 * Freehand aggregates all the line segments that a user draws between the time
 * when the mouse is pressed and when the mouse is released. The Freehand class
 * allows for meaningful undos/redos.
 * 
 * @author Eric Ruleman
 * 
 */
public class Freehand implements DrawingObject{
	private ArrayList<Line> lineList;

	public Freehand(ArrayList<Line> lineList) {
		this.lineList = lineList;
	}

	public ArrayList<Line> getLineList() {
		return lineList;
	}

	@Override
	/**
	 * returns a String representation of a Freehand as a list of alternating x and y values
	 * followed by a color and a thickness.
	 */
	public String toString() {
	    StringBuilder string = new StringBuilder();
	    for (int i=0; i < lineList.size() - 1; i++){
	        string.append(lineList.get(i).firstPointToString() + " ");
	    }
	    string.append(lineList.get(lineList.size() - 1).toString());
		return string.toString();
	}

}
