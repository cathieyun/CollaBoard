package canvas;

/**
 * Line represents a line segment that a user draws by dragging the mouse. 
 * Freehand objects are composed of one or more Lines.
 * 
 */
public class Line {
	int x1;
	int y1;
	int x2;
	int y2;
	String color;
	String thickness;

	public Line(int x1, int y1, int x2, int y2, String color, String thickness) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
		this.thickness = thickness;
	}
	
	@Override
	public String toString() {
		String s = x1 + " " + y1 + " " + x2 + " "  + y2 + " " + color + " " + thickness;
		return s;
	}
	/**
	 * @return String representing the x and y coordinate of the first point of the line. 
	 */
    public String firstPointToString() {
        String s = x1 + " " + y1;
        return s;
    }	
	

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public String getColor() {
		return color;
	}

	public String getThickness() {
		return thickness;
	}
}