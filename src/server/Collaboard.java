package server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import canvas.CanvasModel;


/**
 * Class that stores all the active Whiteboards on the server, and the current list
 * of taken usernames.
 * Threadsafe through use of the monitor pattern, so that other threads won't be able to 
 * access Collaboard's fields while they are being mutated.
 */
public class Collaboard {

    private Map<Integer, Whiteboard>whiteboards;
    private Set<String> usernames;
    
    public Collaboard(){
        this.whiteboards = new HashMap<Integer, Whiteboard>();
        this.usernames = new HashSet<String>();
    }
    /**
     * Adds a new whiteboard to the list of whiteboards.
     * @param ID - of the Whiteboard to be added
     * @return the newly created Whiteboard
     * @throws RuntimeException if the ID is already taken
     * (shouldn't happen, since this method is only called after verifying
     * that the ID is available for creation).
     */
    public synchronized Whiteboard createNewWhiteboard(int ID){
        if (!existingWhiteboard(ID)){
            Whiteboard w = new Whiteboard(ID);
            whiteboards.put(ID, w);
            return w;
        }
        else{
            throw new RuntimeException("This whiteboard ID is already taken");
        }
    }
    /**
     * Checks whether the desired whiteboardID is already taken
     * @param ID - to be checked.
     * @return true if already taken, false if not
     */
    public synchronized boolean existingWhiteboard(int ID){
        return (whiteboards.keySet().contains(ID));
    }
    
    /**
     * Checks whether the desired username is taken, and returns the message the server
     * should return to the client. If it's not taken, add it to the list.
     * @param username - desired username to check
     * @return - "usertaken" if the username is taken, "validuser" if not.
     */
    public synchronized String addUser(String username){
        if (usernames.contains(username)){
            return "usertaken";
        } 
        usernames.add(username);
        return "validuser";
    }
    /**
     * Removes the specified username from the collaboard's list of usernames.
     * Doing so allows a new client to use that username.
     * Requires: username must be currently in the set of usernames.
     * @param username - to be removed
     */
    public synchronized void removeUsername(String username){
        usernames.remove(username);
    }
    
    /**
     * @return HashMap mapping whiteboardIDs to whiteboards.
     */
    public synchronized Map<Integer, Whiteboard> getWhiteboards(){
        return whiteboards;
    }
    
    /**
     * @param ID - of the target whiteboard
     * @return CanvasModel corresponding to the inputted Whiteboard ID
     */
    public synchronized CanvasModel getCanvasModelByID(int ID){
        return whiteboards.get(ID).getCanvasModel();
    }

}
