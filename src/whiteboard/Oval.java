package whiteboard;

public class Oval implements canvas.DrawingObject{
	int x;
	int y;
	int shapeStartX;
	int shapeStartY;

	public Oval (int shapeStartX, int shapeStartY, int x, int y) {
		this.shapeStartX = shapeStartX;
		this.shapeStartY = shapeStartY;
		this.x = x;
		this.y = y;
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
}
