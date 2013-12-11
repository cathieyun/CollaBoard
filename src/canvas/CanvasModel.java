package canvas;

import java.util.ArrayList;

/**
 * Model by which the server stores the information for each individual Whiteboard.
 * Threadsafe because only one thread (the request handler thread) can gain access to an instance
 * of CanvasModel at a time.
 * No thread can gain direct access to drawingObjectList.
 * Rep Invariant: undoIndex >= 0.
 */
public class CanvasModel{
    private ArrayList<DrawingObject> drawingObjectList;
    private int undoIndex;
    public CanvasModel(){
        drawingObjectList = new ArrayList<DrawingObject>();
        undoIndex = 0;
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
	 * 
	 * @param i - index of the DrawingObject to retrieve
	 * @return DrawingObject at the ith index in drawingObjectList
	 */
    public synchronized DrawingObject getIthDrawingObject(int i){
        return drawingObjectList.get(i);
    }
    
	/**
	 * 
	 * @return the current undo index
	 */
    public synchronized int getUndoIndex(){
        return undoIndex;
    }

    /**
     * Set the undo index to the input index.
     */
    public synchronized void setUndoIndex(int index){
        undoIndex = index;
    }
	/**
	 * Decrements the undo index.
	 */
    public synchronized void decrementIndex(){
    	if (undoIndex > 0) {
            undoIndex--;
    	}
    }

	/**
	 * Increments the undo index.
	 */
    public synchronized void incrementIndex(){
    	if (undoIndex < drawingObjectList.size()) {
            undoIndex++;
    	}
    }
    
	/**
	 * Requires: 0 <= index < drawingObjectList.size(). Removes a DrawingObject
	 * from drawingObjectList.
	 * 
	 * @param index of the DrawingObject to be removed
	 */
    public synchronized void removeDrawingObject(int index){
        drawingObjectList.remove(index);
    }
    
	/**
	 * Adds a DrawingObject to drawingObjectList.
	 * 
	 * @param d - DrawingObject object to be added.
	 */
    public synchronized void addDrawingObject(DrawingObject d){
        drawingObjectList.add(d);
    }
    
	/**
	 *  After a series of undo operations, if a user begins to draw again,
	 *  all edits after the current edit are discarded.
	 */
	public synchronized void preventRedoAfterThisEdit() {
		for (int i = getListSize() - 1; getUndoIndex() < getListSize(); i--) {
			removeDrawingObject(i);
		}
	}
	
	public boolean checkRep(){
	    return undoIndex <= drawingObjectList.size() && undoIndex >=0;
	}
}
