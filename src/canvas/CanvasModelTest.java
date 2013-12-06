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
        c.addDrawingObject(new Freehand(new int[]{3,4,5,6}, "w", "m"));
        assertEquals(2, c.getListSize());
        c.preventRedoAfterThisEdit();
        assertEquals(1, c.getListSize()); //check that the list now has one element
        assertEquals(c.getIthDrawingObject(0).toString(), "freehand 1 2 3 4 bl s"); //check that the correct object was removed
    }
}
