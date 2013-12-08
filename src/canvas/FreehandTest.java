package canvas;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class FreehandTest {
    //Test that instantiating two identical freehands with the two different constructors produces the same string rep
    @Test
    public void constructorsTest(){
        Line line1 = new Line(1,1,2,2,"bl","s");
        Line line2 = new Line(2,2,3,3,"bl","s");
        ArrayList<Line> lines = new ArrayList<Line>();
        lines.add(line1);
        lines.add(line2);
        Freehand f1 = new Freehand(lines);
        int [] points = {1,1,2,2,3,3};
        Freehand f2 = new Freehand(points, "bl", "s");
        assertEquals(f1.toString(),f2.toString());
    }
	//Test that an FreeHand is an instance of a DrawingObject
	public void instanceOfDrawingObjectTest(){
		int [] points = {1,1,2,2,3,3};
		Freehand f1 = new Freehand(points, "bl", "s");
        assertEquals(true, f1 instanceof DrawingObject);
	}
    //Test that the freehand's toString() method produces the expected string representation.
    @Test
    public void toStringTest(){
        int [] points = {1,1,2,2,3,3};
        Freehand f = new Freehand(points, "bl", "s");
        assertEquals(f.toString(),"freehand 1 1 2 2 3 3 bl s");
    }

}
