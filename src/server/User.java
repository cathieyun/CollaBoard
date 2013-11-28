package server;

public class User {
    private final int userID;
    private String username;
    private int whiteboardID;
    public User(int userID){
        this.userID = userID;
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
    
}
