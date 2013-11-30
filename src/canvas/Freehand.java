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
	public String toString() {
		return "Freehand [lineList=" + lineList + "]";
	}

}
