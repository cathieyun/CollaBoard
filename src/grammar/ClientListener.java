package grammar;

import java.util.concurrent.BlockingQueue;

import whiteboard.Whiteboard;
import collaboard.Collaboard;

/**
 * Listener that takes in client inputs and makes the necessary changes to the model; 
 * Will add changes to specific whiteboards to their respective BlockingQueues.
 *
 */
public class ClientListener extends ProtocolBaseListener{
    private Collaboard collaboard;
    public ClientListener(Collaboard collaboard){
        this.collaboard = collaboard;
    }
    
    public void exitLine(ProtocolParser.LineContext ctx){
        System.out.println("got here");
    }
    public void exitMakeboard(ProtocolParser.MakeboardContext ctx) {
        int whiteboardID = Integer.parseInt(ctx.INT().getText());
        collaboard.createNewWhiteboard(whiteboardID, 400,400);
    }
    
    @Override public void exitDraw(ProtocolParser.DrawContext ctx) { 
        int whiteboardID = Integer.parseInt(ctx.INT().getText());
        Whiteboard whiteboard = collaboard.getWhiteboards().get(whiteboardID);
        BlockingQueue queue = whiteboard.getRequests();
        queue.add("draw" + ctx.stroke().getText());
    }


    @Override public void enterUndo(ProtocolParser.UndoContext ctx) { }
    @Override public void exitUndo(ProtocolParser.UndoContext ctx) { 
        int whiteboardID = Integer.parseInt(ctx.INT().getText());
        Whiteboard whiteboard = collaboard.getWhiteboards().get(whiteboardID);
        BlockingQueue queue = whiteboard.getRequests();
        queue.add("undo" + ctx.stroke().getText());
    }

    @Override public void enterMakeusername(ProtocolParser.MakeusernameContext ctx) { }
    @Override public void exitMakeusername(ProtocolParser.MakeusernameContext ctx) { 
        int userID = Integer.parseInt(ctx.INT().getText());
        System.out.println("making username");
        
    }

    @Override public void enterEnter(ProtocolParser.EnterContext ctx) { }
    @Override public void exitEnter(ProtocolParser.EnterContext ctx) { 
        int whiteboardID = Integer.parseInt(ctx.INT().getText());
        Whiteboard whiteboard = collaboard.getWhiteboards().get(whiteboardID);
        whiteboard.getUsers().add(ctx.USERNAME().getText());
    }

    @Override public void enterExit(ProtocolParser.ExitContext ctx) { }
    @Override public void exitExit(ProtocolParser.ExitContext ctx) { 
        int whiteboardID = Integer.parseInt(ctx.INT().getText());
        Whiteboard whiteboard = collaboard.getWhiteboards().get(whiteboardID);
        whiteboard.getUsers().remove(ctx.USERNAME().getText());
    }

}
