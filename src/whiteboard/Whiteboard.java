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
 * Whiteboard 
 * 
 * @author Eric Ruleman
 *
 */
public class Whiteboard {
    private CanvasModel canvas;
    private ArrayList<String> users;
    private final int whiteboardID;
    private BlockingQueue requests;
    
	/**
	 * The constructor method for Whiteboard.
	 * 
	 * @param whiteboardID
	 *            the unique, nonnegative numerical ID for the whiteboard
	 * @param user
	 *            the user who created the whiteboard
	 */
    public Whiteboard(int whiteboardID, String user){
        this.whiteboardID = whiteboardID;
        canvas = new CanvasModel();
        this.users = new ArrayList<String>();
        requests = new LinkedBlockingQueue<String>(); //stores the requests to be handled
        users.add(user); 
    }
    
    public BlockingQueue getRequests(){
        return requests;
    }
    
    public ArrayList<String> getUsers(){
        return users;
    }

    public CanvasModel getCanvasModel() {
        return canvas;
    }
    
	/**
	 * addUser() is called when a user enters a Whiteboard. Adds the user to
	 * users.
	 * 
	 * @param user
	 *            the user who entered the Whiteboard.
	 */
	public void addUser(String user) {
		users.add(user);
	}
    
	/**
	 * removeUser() is called when a user exits a Whiteboard. Removes the
	 * user from users.
	 * 
	 * @param user
	 *            the user who exited the Whiteboard
	 */
	public void removeUser(String user) {
		users.remove(user);
	}
}