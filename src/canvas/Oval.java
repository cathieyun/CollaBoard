package canvas;

public class Oval implements DrawingObject{
	int x, y, shapeStartX, shapeStartY;
	String color, thickness;

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

	@Override
	public String toString() {
		return "Oval [x=" + x + ", y=" + y + ", shapeStartX=" + shapeStartX
				+ ", shapeStartY=" + shapeStartY + "]";
	}
}
