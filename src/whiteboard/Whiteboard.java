package whiteboard;

import java.util.ArrayList;
import java.util.List;

import canvas.Canvas;
import canvas.Toolbar;

public class Whiteboard {
    private final Canvas canvas;
    private List<String> users;
    private final int whiteboardID;
    public Whiteboard(int whiteboardID, int x, int y){
        this.whiteboardID = whiteboardID;
        this.canvas = new Canvas(x,y);
        this.users = new ArrayList<String>();
    }
}
