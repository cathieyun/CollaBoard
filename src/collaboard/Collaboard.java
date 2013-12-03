package collaboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import whiteboard.Whiteboard;
/**
 * Class that stores all the active Whiteboards on the server, and the current list
 * of taken usernames.
 *
 */
public class Collaboard {

    private Map<Integer, Whiteboard>whiteboards;
    private Set<String> usernames;
    
    public Collaboard(){
        this.whiteboards = new HashMap<Integer, Whiteboard>();
        this.usernames = new HashSet<String>();
        usernames.add("bob");
    }
    /**
     * Adds a new whiteboard to the list of whiteboards.
     * @param ID - of the Whiteboard to be added
     * @return the newly created Whiteboard
     */
    public Whiteboard createNewWhiteboard(int ID){
        Whiteboard w = new Whiteboard(ID);
        whiteboards.put(ID, w);
        return w;
    }
    /**
     * Checks whether the desired whiteboardID is already taken
     * @param ID - to be checked.
     * @return true if already taken, false if not
     */
    public boolean existingWhiteboard(int ID){
        return (whiteboards.keySet().contains(ID));
    }
    /**
     * Checks whether the desired username is taken, and returns the message the server
     * should return to the client. If it's not taken, add it to the list.
     * @param username - desired username to check
     * @return - "usertaken" if the username is taken, "validuser" if not.
     */
    public String addUser(String username){
        if (usernames.contains(username)){
            return "usertaken";
        } 
        usernames.add(username);
        return "validuser";
    }
    
    public void removeUsername(String username){
        usernames.remove(username);
    }
    
    public Map<Integer, Whiteboard> getWhiteboards(){
        return whiteboards;
    }

}
