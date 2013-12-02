package canvas;

import java.util.ArrayList;


/**
 * Freehand aggregates all the line segments that a user draws between the time
 * when the mouse is pressed and when the mouse is released. The Freehand class
 * allows for meaningful undos/redos.
 * 
 */
public class Freehand implements DrawingObject{
	private ArrayList<Line> lineList;
	private ArrayList<Integer> pointsList;
	public Freehand(ArrayList<Line> lineList) {
		this.lineList = lineList;
	}
	/**
	 * initialize a Freehand by an array of integers representing points.
	 * Requires: length of pointsList%2 == 0, length of pointsList >=4
	 * @param pointsList
	 */
	public Freehand(int[] pointsList, String color, String thickness){
	    lineList = new ArrayList<Line>();
	    for (int i = 0; i < pointsList.length-2; i=i+2){
	       Line line = new Line(pointsList[i], pointsList[i+1], pointsList[i+2], pointsList[i+3], color, thickness);
	       lineList.add(line);
	    }
	}
	
	/**
	 * returns a list of the line objects that comprise the DrawingObject.
	 */
	public ArrayList<Line> getLineList() {
		return lineList;
	}

	@Override
	/**
	 * returns a String representation of a Freehand as a list of alternating x and y values
	 * followed by a color and a thickness.
	 */
	public String toString() {
	    StringBuilder string = new StringBuilder("freehand ");
	    for (int i=0; i < lineList.size() - 1; i++){
	        string.append(lineList.get(i).firstPointToString() + " ");
	    }
	    string.append(lineList.get(lineList.size() - 1).toString());
		return string.toString();
	}

}
