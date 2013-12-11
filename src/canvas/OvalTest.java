package canvas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
/**
 * Testing Strategy:
 * 1. Ensure that the toString() method outputs the correct String representation as to easily be integrated into our protocol.
 * 2. Ensure that the Oval class is a subtype of DrawingObject.
 */
public class OvalTest {

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
