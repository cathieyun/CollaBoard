package collaboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import whiteboard.Whiteboard;

public class Collaboard {
/**
 * Contains a collection of whiteboards.
 */
    private List<Whiteboard> whiteboards;
    private Set<Integer> whiteboardIDs;
    private Set<String> usernames;
    
    public Collaboard(){
        this.whiteboards = new ArrayList<Whiteboard>();
    }
    
    public void createNewWhiteboard(int ID, int x, int y){
        if (whiteboardIDs.contains(ID)){
            //do something
        }
        else{
            Whiteboard w = new Whiteboard(ID, x, y);
            whiteboards.add(w);
            whiteboardIDs.add(ID);
        }
    }
    
    private void checkUsernameValidity(String input){
        if (usernames.contains(input)){
            //return error message
        }
    }
    
    public List<Whiteboard> getWhiteboards(){
        return whiteboards;
    }
    
}
