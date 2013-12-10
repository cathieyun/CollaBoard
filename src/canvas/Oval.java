package canvas;
/**
 * Abstract representation of an Oval drawn on the canvas.
 */
public class Oval implements DrawingObject{
	int x, y, shapeStartX, shapeStartY;
	String color, thickness;
	/**
	 * Instantiates an Oval object.
     * @param thickness - String representation corresponding to one of the supported thicknesses. (s|m|l)
     * @param color - String representation corresponding to one of the supported colors. (bl|y|r|g|o|m|blk|w)
	 */
	public Oval (int shapeStartX, int shapeStartY, int x, int y, String color, String thickness) {
		this.shapeStartX = shapeStartX;
		this.shapeStartY = shapeStartY;
		this.x = x;
		this.y = y;
		this.color = color;
		this.thickness = thickness;
	}
	
	public String getColor() {
		return color;
	}

	public String getThickness() {
		return thickness;
	}

	public int getWidth() {
		return Math.abs(shapeStartX - x);
	}
	
	public int getHeight() {
		return Math.abs(shapeStartY - y);
	}
	
	public int getTopLeftX() {
		return shapeStartX < x ? shapeStartX : x;
	}
	
	public int getTopLeftY() {
		return shapeStartY < y ? shapeStartY : y;
	}
    /**
     * Returns a String representation of an Oval that is consistent with the client-server protocol.
     */
	@Override
	public String toString() {
		return "oval " + shapeStartX + " " + shapeStartY + " " + x + " " + y + " " + color + " " + thickness;
	}
}
