package canvas;

import java.util.ArrayList;


/**
 * Freehand aggregates all the line segments that a user draws between the time
 * when the mouse is pressed and when the mouse is released. 
 */
public class Freehand implements DrawingObject{
	private ArrayList<Line> lineList; 
	public Freehand(ArrayList<Line> lineList) {
		this.lineList = lineList;
	}
	/**
	 * Initialize a Freehand by an array of integers representing points.
	 * Requires: length of pointsList%2 == 0, length of pointsList >=4
	 * @param thickness - String representation corresponding to one of the supported thicknesses. (s|m|l)
	 * @param color - String representation corresponding to one of the supported colors. (bl|y|r|g|o|m|blk|w)
	 * @param pointsList - array of integers representing x and y coordinates pairs. 
	 * even indexed values correspond to x coordinates and the value at the index immediately following is the corresponding y coordinate.
	 */
	public Freehand(int[] pointsList, String color, String thickness){
	    lineList = new ArrayList<Line>();
	    for (int i = 0; i < pointsList.length-2; i=i+2){
	       Line line = new Line(pointsList[i], pointsList[i+1], pointsList[i+2], pointsList[i+3], color, thickness);
	       lineList.add(line);
	    }
	}
	
	/**
	 * Returns a list of the Line objects that comprise the DrawingObject.
	 */
	public ArrayList<Line> getLineList() {
		return lineList;
	}


	/**
	 * Returns a String representation of a Freehand as a list of pairs of x and y values in order
	 * followed by a color and a thickness.
	 */
	@Override
	public String toString() {
	    StringBuilder string = new StringBuilder("freehand ");
	    for (int i=0; i < lineList.size() - 1; i++){
	        string.append(lineList.get(i).firstPointToString() + " ");
	    }
	    if (lineList.size() > 0){
	        string.append(lineList.get(lineList.size() - 1).toString());
	    }
		return string.toString();
	}

}
