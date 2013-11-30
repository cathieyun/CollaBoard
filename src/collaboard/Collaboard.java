package collaboard;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import client.User;


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
        this.usernames = new HashSet<String>();
    }
    
    public Whiteboard createNewWhiteboard(int ID){
        Whiteboard w = new Whiteboard(ID);
        whiteboards.put(ID, w);
        return w;
    }
 
    public boolean existingWhiteboard(int ID){
        return (whiteboards.keySet().contains(ID));
    }
    
    public void addUser(int userID, String username){
        System.out.println("Creating username: "+username);
//        if (usernames.contains(username)){
//            return "E*"; //username already taken
//        } //TODO: implement unique usernames later
        usernames.add(username);
    }
    
    public Map<Integer, Whiteboard> getWhiteboards(){
        return whiteboards;
    }

}
