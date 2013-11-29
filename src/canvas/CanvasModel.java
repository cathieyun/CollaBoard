package canvas;

import java.util.ArrayList;
import java.util.List;

public class CanvasModel{
    private List<DrawingObject> drawingObjects;
    public CanvasModel(){
        drawingObjects = new ArrayList<DrawingObject>();
    }
    
    public List<DrawingObject> getDrawingObjects(){
        return drawingObjects;
    }
    
}
