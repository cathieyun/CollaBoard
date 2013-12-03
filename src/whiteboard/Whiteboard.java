package whiteboard;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import canvas.Canvas;
import canvas.CanvasModel;
import canvas.ToolbarModel;

/**
 * Class representing the state of one collaborative Whiteboard.
 * Contains a CanvasModel and a list of users.
 */
public class Whiteboard {
    private CanvasModel canvas;
    private ArrayList<String> users;
    private final int whiteboardID; //the unique ID associated with the whiteboard.
    
    /**
     * Constructor
     * Requires: whiteboardID > 0, not already associated with another Whiteboard.
     */
    public Whiteboard(int whiteboardID){
        this.whiteboardID = whiteboardID;
        canvas = new CanvasModel();
        this.users = new ArrayList<String>();
    }
    
    public ArrayList<String> getUsers(){
        return users;
    }
    /**
     * @return - the canvas model associated with the whiteboard.
     */
    public CanvasModel getCanvasModel() {
        return canvas;
    }
    
	/**
	 * Called when a new user enters the Whiteboard. Adds her username to the users list.
	 * @param user - username of the user who entered the Whiteboard.
	 */
	public void addUser(String user) {
		users.add(user);
	}
    
	/**
	 * Called when a user exits the whiteboard. Removes his/her username from the users list.
	 * 
	 * @param user - username of the user who exited the Whiteboard
	 */
	public void removeUser(String user) {
		users.remove(user);
	}
}