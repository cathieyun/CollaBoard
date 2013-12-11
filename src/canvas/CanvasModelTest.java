package canvas;
import static org.junit.Assert.*;
import org.junit.Test;
public class CanvasModelTest {
    //Check that preventRedoAfterThisEdit() removes DrawingObjects that come after the current undoIndex.
    @Test
    public void preventRedoAfterThisEditTest(){
        CanvasModel c = new CanvasModel();
        c.addDrawingObject(new Freehand(new int[]{1,2,3,4},"bl","s"));
        c.incrementIndex(); //undoIndex is now 1.
        assertTrue(c.checkRep());
        c.addDrawingObject(new Freehand(new int[]{3,4,5,6}, "w", "m"));
        assertTrue(c.checkRep());
        assertEquals(2, c.getListSize());
        c.preventRedoAfterThisEdit();
        assertEquals(1, c.getListSize()); //check that the list now has one element
        assertEquals(c.getIthDrawingObject(0).toString(), "freehand 1 2 3 4 bl s"); //check that the correct object was removed
        assertTrue(c.checkRep());
    }
    //check that undoIndex cannot be decremented to be less than 0
    @Test
    public void undoIndexLessThanZeroTest(){
        CanvasModel c = new CanvasModel();
        c.decrementIndex();
        assertEquals(c.getUndoIndex(), 0);
    }
    //check that the undoIndex cannot be incremented to become > the size of the drawingObjectsList
    @Test
    public void undoIndexGreaterThanZeroTest(){
        CanvasModel c = new CanvasModel();
        c.addDrawingObject(new Freehand(new int[]{3,4,5,6}, "w", "m"));
        c.incrementIndex();
        c.incrementIndex();
        assertEquals(c.getUndoIndex(),1);
    }
}
