package canvas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OvalTest {
	//Test that instantiating two ovals the same way will produce the same string rep
	@Test
	public void constructorsTest(){
		Oval oval1 = new Oval(5, 5, 20, 26, "blk", "m");
		Oval oval2 = new Oval(5, 5, 20, 26, "blk", "m");
		assertEquals(oval1.toString(), oval2.toString());
	}
	//Test that an oval is an instance of a DrawingObject
	@Test
	public void instanceOfDrawingObjectTest(){
		Oval oval1 = new Oval(5, 5, 20, 26, "blk", "m");
		assertEquals(true, oval1 instanceof DrawingObject);
	}
	//Test that the toString method of Oval works properly, returns all info.
	@Test
	public void toStringTest(){
		Oval oval1 = new Oval(5, 5, 20, 26, "blk", "m");
		assertEquals(oval1.toString(),"oval 5 5 20 26 blk m");
	}
	
}
