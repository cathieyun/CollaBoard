package client;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import canvas.ToolbarModel;

public class User {
    //for identification purposes, in case we want to add the functionality to change usernames
    private final int userID; 
    //current username
    private String username;
    //current ID of the whiteboard the user is viewing
    private int whiteboardID;
    //current state of the user's toolbar settings
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
