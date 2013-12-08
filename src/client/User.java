package client;

import canvas.ToolbarModel;
/**
 * Class representing a user. Stores its userID, current username, and ID of the current whiteboard.
 *
 */
public class User {
    //for identification purposes, in case we want to add the functionality to change usernames
    private final int userID; 
    private String username;
    //current ID of the whiteboard the user is viewing
    private int whiteboardID;
    //current state of the user's toolbar settings
    private ToolbarModel toolbar;
    public User(int userID){
        this.userID = userID;
        toolbar = new ToolbarModel(userID);
    }
    public int getUserID() {
        return userID;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getWhiteboardID() {
        return whiteboardID;
    }
    public void setWhiteboardID(int whiteboardID) {
        this.whiteboardID = whiteboardID;
    }
    /**
     * @return the ToolbarModel associated with the user.
     */
    public ToolbarModel getToolbar() {
        return toolbar;
    }
}
