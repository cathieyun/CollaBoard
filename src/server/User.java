package server;

import java.awt.Graphics2D;

import canvas.ToolbarModel;

public class User {
    private final int userID;
    private String username;
    private int whiteboardID;
    private ToolbarModel toolbar;
    public User(int userID){
        this.userID = userID;
        toolbar = new ToolbarModel();
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
    public ToolbarModel getToolbar() {
        return toolbar;
    }
    
}
