package collaboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import server.User;

import whiteboard.Whiteboard;

public class Collaboard {
/**
 * Contains a collection of whiteboards.
 */
    private Map<Integer, Whiteboard>whiteboards;
    private HashMap<Integer, User> users;
    private Set<String> usernames;
    
    public Collaboard(){
        this.whiteboards = new HashMap<Integer, Whiteboard>();
        this.users = new HashMap<Integer, User>();
    }
    
    public Whiteboard createNewWhiteboard(int ID, int x, int y){
        Whiteboard w = new Whiteboard(ID);
        whiteboards.put(ID, w);
        return w;
    }
    
    public boolean exisitingWhiteboard(int ID){
        return (whiteboards.keySet().contains(ID));
    }
    
    public String addUser(int userID, String username){

//        if (usernames.contains(username)){
//            return "E*"; //username already taken
//        } //TODO: implement unique usernames later
        usernames.add(username);
        users.get(userID).setUsername(username);
        return "C:" + username + userID;
    }
    
    public Map<Integer, Whiteboard> getWhiteboards(){
        return whiteboards;
    }   
}
