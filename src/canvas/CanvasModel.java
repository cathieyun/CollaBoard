package canvas;

import java.util.ArrayList;

/**
 * Model for the Canvas GUI.
 * Threadsafe through use of the monitor pattern.
 * No thread can gain direct access to drawingObjectList.
 * @author KateYu
 *
 */
public class CanvasModel{
    private ArrayList<DrawingObject> drawingObjectList;
    private int drawingObjectListUndoIndex;
    public CanvasModel(){
        drawingObjectList = new ArrayList<DrawingObject>();
        drawingObjectListUndoIndex = 0;
    }
    /**
     * 
     * @return the current size of drawingObjectList
     */
    public synchronized int getListSize(){
        return drawingObjectList.size();
    }
    /**
     * Requires: 0 <= index < drawingObjectList.size() 
     * @param i index of the DrawingObject to retrieve
     * @return DrawingObject at the ith index in drawingObjectList
     */
    public synchronized DrawingObject getIthDrawingObject(int i){
        return drawingObjectList.get(i);
    }
    
    /**
     * 
     * @return the current undo index
     */
    public synchronized int getDrawingObjectListUndoIndex(){
        return drawingObjectListUndoIndex;
    }
    /**
     * Decrements the undo index.
     * @return the current undo index
     */
    public synchronized int getAndDecrementIndex(){
        --drawingObjectListUndoIndex;
        return drawingObjectListUndoIndex;
    }
    /**
     * Increments the undo index.
     * @return the current undo index.
     */
    public synchronized int getAndIncrementIndex(){
        ++drawingObjectListUndoIndex;
        return drawingObjectListUndoIndex;
    }
    
    /**
     * Requires: 0 <= index < drawingObjectList.size()
     * Removes a freehand from drawingObjectList.
     * @param index of the freehand to be removed
     */
    public synchronized void removeDrawingObject(int index){
        drawingObjectList.remove(index);
    }
    
    /**
     * Adds a Freehand to drawingObjectList.
     * @param f Freehand object to be added.
     */
    public synchronized void addDrawingObject(DrawingObject d){
        drawingObjectList.add(d);
    }
}
