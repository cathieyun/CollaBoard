package whiteboard;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import canvas.Canvas;
import canvas.CanvasModel;
import canvas.ToolbarModel;

public class Whiteboard {
    private CanvasModel canvas;
    private List<String> users;
    private final int whiteboardID;
    private BlockingQueue requests;
    
    public Whiteboard(int whiteboardID){
        this.whiteboardID = whiteboardID;
        canvas = new CanvasModel();
        this.users = new ArrayList<String>();
        requests = new LinkedBlockingQueue<String>(); //stores the requests to be handled
    }
    
    public BlockingQueue getRequests(){
        return requests;
    }
    
    public List<String> getUsers(){
        return users;
    }

    public CanvasModel getCanvasModel() {
        return canvas;
    }
    
}
