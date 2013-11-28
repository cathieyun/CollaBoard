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
    
    public Whiteboard(int whiteboardID, int x, int y){
        this.whiteboardID = whiteboardID;
        this.canvas = new Canvas(x,y);
        this.users = new ArrayList<String>();
        requests = new LinkedBlockingQueue<String>(); //stores the requests to be handled
    }
    
    public BlockingQueue getRequests(){
        return requests;
    }
    
    public List<String> getUsers(){
        return users;
    }
    
}
