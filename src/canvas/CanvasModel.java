package canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the Canvas GUI.
 * Threadsafe through use of the monitor pattern.
 * No thread can gain direct access to freehandList.
 * @author KateYu
 *
 */
public class CanvasModel{
    private ArrayList<Freehand> freehandList;
    private int freehandListUndoIndex;
    public CanvasModel(){
        freehandList = new ArrayList<Freehand>();
        freehandListUndoIndex = 0;
    }
    /**
     * 
     * @return the current size of freehandList
     */
    public synchronized int getListSize(){
        return freehandList.size();
    }
    /**
     * Requires: 0 <= index < freehandList.size() 
     * @param i index of the Freehand to retrieve
     * @return Freehand at the ith index in freehandList
     */
    public synchronized Freehand getIthFreehand(int i){
        return freehandList.get(i);
    }
    
    /**
     * 
     * @return the current undo index
     */
    public synchronized int getFreehandListUndoIndex(){
        return freehandListUndoIndex;
    }
    /**
     * Decrements the undo index.
     * @return the current undo index
     */
    public synchronized int getAndDecrementIndex(){
        --freehandListUndoIndex;
        return freehandListUndoIndex;
    }
    /**
     * Increments the undo index.
     * @return the current undo index.
     */
    public synchronized int getAndIncrementIndex(){
        ++freehandListUndoIndex;
        return freehandListUndoIndex;
    }
    
    /**
     * Requires: 0 <= index < freehandList.size()
     * Removes a freehand from freehandList.
     * @param index of the freehand to be removed
     */
    public synchronized void removeFreehand(int index){
        freehandList.remove(index);
    }
    
    /**
     * Adds a Freehand to freehandList.
     * @param f Freehand object to be added.
     */
    public synchronized void addFreehand(Freehand f){
        freehandList.add(f);
    }
}
