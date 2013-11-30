package client;

import java.util.ArrayList;
import java.util.Observable;

import canvas.CanvasModel;
import canvas.DrawingObject;

/**
 * Class that stores the state of each individual client's canvas.
 * (not sure if we need it just yet)
 *
 */
public class ClientCanvasModel extends CanvasModel{
    private ArrayList<DrawingObject> drawingObjectList;
    private int drawingObjectListUndoIndex;
    public ClientCanvasModel(){
        super();
    }
}
