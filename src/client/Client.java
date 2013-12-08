package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import canvas.Canvas;
import canvas.DrawingObject;
import canvas.Freehand;
import canvas.Oval;

import collaboard.CollaboardGUI;
/**
 * Client class for Collaboard. Represents a single client connection.
 * Handles inputs/outputs to the server and acts as a controller for the GUI.
 *
 */
public class Client {
    private String host;
    private int port;
    private Socket socket;
    private CollaboardGUI gui;
    private User user;
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    public void run(){
        try {
            socket = new Socket(host, port);
            handleServer(); //TODO: add swingworker so that all edits happen on event handling thread
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Server not running");
            }
    }
    /**
     * Handles the connection to the server.
     * @throws IOException
     */
    private void handleServer() throws IOException{
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String fromServer = in.readLine(); //get the userID and initialize userObject first.
        handleMessage(fromServer);
        gui = new CollaboardGUI(user, outputStream, inputStream);
        gui.setVisible(true);
        while ((fromServer = in.readLine()) != null){
            if (!fromServer.equals("")){ //ignore newlines
                System.out.println("From server: "+ fromServer);
                handleMessage(fromServer);
            }
       }
        
    }
    /**
     * Handles messages from the server according to the following grammar:<br>
     * USERID: [0-9]+<br>
     * VALIDUSER: validuser<br>
     * VALIDWHITEBOARD: validwhiteboard<br>
     * READY: ready<br>
     * COLOR: bl|y|r|g|o|m|blk|w<br>
     * THICKNESS: s|m|l<br>
     * INITDRAW: initdraw ((freehand ([0-9]+ [0-9]+ )([0-9]+ [0-9]+ )+)|<br>
     *      (oval [0-9]+ [0-9]+ [0-9]+ [0-9]+ ) COLOR THICKNESS<br>
     * UNDOINDEX: undoindex [0-9]+<br>
     * USERTAKEN: usertaken<br>
     * WHITEBOARDTAKEN: whiteboardtaken<br>
     * ENTER: enter USERNAME<br>
     * EXIT: exit USERNAME<br>
     * UNDO: undo<br>
     * REDO: redo<br>
     * 
     * All edits to the GUI are done on the event handling thread through the use of
     * SwingUtilities.invokeLater().
     * @param input - message from the server
     */
    private void handleMessage(String input){
        String regex = "(userID [0-9]+)|(validuser)|(validwhiteboard)|(ready)|" +
        		"((init)*draw freehand( -?\\d+ -?\\d+)( -?\\d+ -?\\d+)+ (bl|y|r|g|o|m|blk|w) (s|m|l))|" +
        		"((init)*draw oval -?\\d+ -?\\d+ -?\\d+ -?\\d+ (bl|y|r|g|o|m|blk|w) (s|m|l))|" +
        		"(initdraw)|(initdone)|(undoindex [0-9]+)|"
                + "(usertaken)|(whiteboardtaken)|(list( -?\\d+)*)|(users ([A-Za-z0-9]( )*)+)|"
                +"(enter [A-Za-z0-9]+)|(exit [A-Za-z0-9]+)|(undo)|(redo)";
        if ( ! input.matches(regex)) {
            System.out.println("server msg: "+ input + " didn't match");
        }
        String[] tokens = input.split(" ");
        if (tokens[0].equals("userID")){
            int userID = Integer.parseInt(tokens[1]);
            System.out.println("I am thread: "+userID);
            user = new User(userID);
        }
        if (tokens[0].equals("validuser")){
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.goToWhiteboardSelect();            
                }                
            });
        }
        if (tokens[0].equals("validwhiteboard")){
            System.out.println("Entering canvas");
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.enterCanvas();            
                }                
            });
        }
        if (tokens[0].equals("usertaken")){
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.displayUserTakenError();            
                }                
            });
        }
        if (tokens[0].equals("whiteboardtaken")){
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.displayWhiteboardTakenError();            
                }                
            });
        }
        if (tokens[0].equals("list")){
            for (int i = 1; i < tokens.length; i++){
               gui.getWhiteboards().add(new Integer(tokens[i]));
            }
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.initializeWhiteboardPane();              
                }                
            });
        }
        if (tokens[0].equals("users")){
            for (int i = 1; i < tokens.length; i++){
                gui.getUsers().add(tokens[i]);
            }
        }
        if(tokens[0].equals("enter")){
            final String username = tokens[1];
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.addUser(username);             
                }                
            });
        }
        if(tokens[0].equals("exit")){
            final String username = tokens[1];
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.removeUser(username);             
                }                
            });
        }
        if (tokens[0].equals("ready")){
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.initializeCanvas();             
                }                
            });
        }
        if (tokens[0].equals("undoindex")){
            gui.getCanvasModel().setUndoIndex(Integer.parseInt(tokens[1]));//set the undo index.
            System.out.println("Set the undo index to: "+ Integer.parseInt(tokens[1]));
            //only draw the objects up until the undoindex.
            for (int i = 0; i < gui.getCanvasModel().getUndoIndex(); i++){
                final int index = i;
                SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                    @Override
                    public void run() {
                       gui.drawObject(gui.getCanvasModel().getIthDrawingObject(index));               
                    }                
                });
            }
        }
        if (tokens[0].equals("undo")){
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.getCanvas().undo();               
                }                
            });
        }
        if (tokens[0].equals("redo")){
            SwingUtilities.invokeLater(new Runnable(){ //avoid race conditions on the GUI by adding this to event handling thread's queue
                @Override
                public void run() {
                    gui.getCanvas().redo();               
                }                
            });
        }
        if (tokens[0].equals("initdraw")){
            System.out.println("received initdraw message");
            String color = tokens[tokens.length-2];
            String thickness = tokens[tokens.length-1];
            ClientCanvasModel currentModel = gui.getCanvasModel();
            //add the objects to the model.
            if(tokens[1].equals("freehand")){
                int [] points = new int[tokens.length-4];
                for (int i=0; i < points.length; i++){
                    points[i] = Integer.parseInt(tokens[i+2]);
                }
                Freehand freehand = new Freehand(points, color, thickness);
                currentModel.addDrawingObject(freehand);
            }
            if(tokens[1].equals("oval")){
                Oval oval = new Oval(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), color, thickness);
                currentModel.addDrawingObject(oval);
            }
        }
        if (tokens[0].equals("draw")){
            String color = tokens[tokens.length-2];
            String thickness = tokens[tokens.length-1];
            ClientCanvasModel currentModel = gui.getCanvasModel();
            gui.getCanvasModel().preventRedoAfterThisEdit(); 
            if(tokens[1].equals("freehand")){
                int [] points = new int[tokens.length-4];
                for (int i=0; i < points.length; i++){
                    points[i] = Integer.parseInt(tokens[i+2]);
                }
                final Freehand freehand = new Freehand(points, color, thickness);
                currentModel.addDrawingObject(freehand);
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        gui.drawObject(freehand);    
                    }                
                });
                
            }
            if(tokens[1].equals("oval")){
                final Oval oval = new Oval(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), color, thickness);
                currentModel.addDrawingObject(oval);
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        gui.drawObject(oval);    
                    }                
                });
            }
            gui.getCanvasModel().incrementIndex();//increment the index.
       }
    }
    /**
     * Creates a connection to the server.
     */
    public static void main(String[]args){
        Client client = new Client("18.111.7.141", 4444);
        client.run();
    }
}
