package whiteboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import canvas.Canvas;
import canvas.ToolbarModel;

public class Whiteboard {
    private final Canvas canvas;
    private List<String> users;
    private final int whiteboardID;
    private BlockingQueue requests;
    
    public Whiteboard(int whiteboardID, int x, int y, String user){
        this.whiteboardID = whiteboardID;
        this.canvas = new Canvas(x,y);
        this.users = new ArrayList<String>();
        requests = new LinkedBlockingQueue<String>(); //stores the requests to be handled
        users.add(user);
    }
    
    public BlockingQueue getRequests(){
        return requests;
    }
    
    public List<String> getUsers(){
        return users;
    }
    
	/**
	 * addUser() is called when a user enters a Whiteboard. Adds the username to
	 * the userList.
	 * 
	 * @param user
	 *            the username of the user who entered the Whiteboard.
	 */
	public void addUser(String user) {
		users.add(user);
	}
    
	/**
	 * removeUser() is called when a user exits a Whiteboard. Removes the
	 * username from userList.
	 * 
	 * @param user
	 *            the username of the user who exited the Whiteboard
	 */
	public void removeUser(String user) {
		users.remove(user);
	}
}