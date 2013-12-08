package server;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import whiteboard.Whiteboard;

import canvas.CanvasModel;
import canvas.DrawingObject;
import canvas.Freehand;
import canvas.Oval;
import client.User;

import collaboard.Collaboard;

/**
 * Class representing the Collaboard server.
 * Stores the set of all Whiteboards and their current states, as well as current
 * active threads.
 */
public class CollaboardServer {

    private final ServerSocket serverSocket;
    private Collaboard collaboard;
    private AtomicInteger numClients;
    private ArrayList<UserThread> threads;
    private BlockingQueue<String[]> requests;
    
    public CollaboardServer(int port, Collaboard collaboard) throws IOException{
        this.serverSocket = new ServerSocket(port);
        this.threads = new ArrayList<UserThread>();
        this.collaboard = collaboard;
        this.numClients = new AtomicInteger(0);
        this.requests = new LinkedBlockingQueue<String[]>();
    }
    
    private void serve() throws IOException{
        /**
         * Thread that handles requests from the requests BlockingQueue, to ensure that edits are made
         * to each whiteboard in the order they are put on the queue, to ensure thread safety.
         */
        Thread requestHandler = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    String[] currentRequest = requests.poll();
                    if (currentRequest != null){
                        //System.out.println("got a request");
                        handleRequest(currentRequest);
                    }
                }      
            }
            /**
             * Method by which to handle requests put on the queue.
             * @param request - draw, undo, or redo request
             */
            private void handleRequest(String[] request){
                    int whiteboardID = Integer.parseInt(request[request.length-1]);
                    CanvasModel currentModel = collaboard.getWhiteboards().get(whiteboardID).getCanvasModel();
                    StringBuilder outputMsg = new StringBuilder();
                    if (request[0].equals("undo")|request[0].equals("redo")){
                        outputMsg.append(request[0]);
                        if (request[0].equals("undo")){
                            currentModel.decrementIndex();
                        }
                        else{
                            currentModel.incrementIndex();
                        }
                    }
                    else if (request[0].equals("draw")){
                        String color = request[request.length-4];
                        String thickness = request[request.length-3];
                        currentModel.preventRedoAfterThisEdit();
                        if(request[1].equals("freehand")){
                            int [] points = new int[request.length-6];
                            for (int i=0; i < points.length; i++){
                                points[i] = Integer.parseInt(request[i+2]);
                            }
                            Freehand freehand = new Freehand(points, color, thickness);
                            currentModel.addDrawingObject(freehand);
                            
                        }
                        if(request[1].equals("oval")){
                            Oval oval = new Oval(Integer.parseInt(request[2]), Integer.parseInt(request[3]), Integer.parseInt(request[4]), Integer.parseInt(request[5]), color, thickness);
                            currentModel.addDrawingObject(oval);
                        }
                        currentModel.incrementIndex();
                        outputMsg.append("draw");
                        for (int i = 1; i < request.length-2; i++){
                            outputMsg.append(" " + request[i]);
                        }
                    }
                    System.out.println("outputMsg: " + outputMsg.toString());
                    //Send the message to all other threads in the same whiteboard to update their canvases.
                    for (UserThread t: threads){
                        if ((whiteboardID == t.getCurrentWhiteboardID())){
                            PrintWriter output = t.getPrintWriter();
                            output.println(outputMsg.toString());
                        }
                    }
            }
            
        });
        requestHandler.start();
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            //allow the server to handle multiple connections
            UserThread thread = new UserThread(socket);
            // handle the current client
            thread.start();
        }
    }
    /**
     * Class representing a single user connection.
     *
     */
    public class UserThread extends Thread{
        private Socket socket;
        private String username;
        private int userID;
        private int currentWhiteboardID;
        private InputStream inputStream;
        private OutputStream outputStream;
        private PrintWriter out;
        public UserThread(Socket socket){
            this.socket= socket;
            currentWhiteboardID = 0; //initialize to 0
            //ensure that no two threads can access and increment numClients at the same time
            synchronized(numClients){
                //assign integer ID based on how many clients there are. doesn't really have to
                //reflect the actual # of clients, everyone just has to have a unique ID
                userID = numClients.intValue();
                numClients.getAndIncrement();
            }
            threads.add(this);
            try{
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        /**
         * @return the PrintWriter associated with this thread.
         */
        public PrintWriter getPrintWriter(){
            return out;
        }
        /**
         * @return the WhiteboardID of the whiteboard the thread is currently in.
         */
        public int getCurrentWhiteboardID(){
            return currentWhiteboardID;
        }
        /**
         * @return the unique userID of the thread
         */
        public int getUserID(){
            return userID;
        }
        @Override
        public void run() {
            try {
                handleConnection(socket, userID);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 
        }
        /**
         * Method used to handle a single client connection.
         * 
         * @param socket
         * @param userID - Unique userID assigned to each socket. Used for identification purposes.
         * @throws IOException
         */
        private void handleConnection(Socket socket, int userID) throws IOException{
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            out = new PrintWriter(outputStream, true);
            try {
                out.println("userID " + userID); //send the userID
                StringBuilder message = new StringBuilder("list");
                for (int whiteboardID: collaboard.getWhiteboards().keySet()){
                    message.append(" " + whiteboardID);
                }
                out.println(message.toString()); //send the list of whiteboards
                for (String line = in.readLine(); line != null; line = in.readLine()) {
                    System.out.println("Received client message: " + line);
                    String output = handleRequest(line);
                    if (output != null) {
                        System.out.println("Server response to " + line + ": " + output);
                        out.println(output);
                    }
                }
            } finally {
                if (socket != null && ! socket.isClosed()) {
                    try {
                        // make sure we close the socket (and associated I/O streams)
                        socket.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                out.close();
                in.close();
            }
        }
        
        /**
         * Handles messages from each client according to the following grammar: <br>
         * WHITEBOARDID: [0]|[1-9][0-9]* <br>
         * USERID: [1-9][0-9]*<br>
         * USERNAME: [0-9a-zA-Z]+<br>
		 * MAKEUSER: makeuser USERNAME WHITEBOARDID<br>
		 * MAKEBOARD: makeboard WHITEBOARDID<br>
		 * SWITCH: switchboard USERNAME WHITEBOARDID<br>
		 * UNDO: undo USERID WHITEBOARDID<br>
 		 * REDO: redo USERID WHITEBOARDID<br>
 		 * COLOR: bl|y|r|g|o|m|blk|w<br>
 		 * THICKNESS: s|m|l<br>
		 * DRAWFREEHAND: draw freehand ([0-9]+ [0-9]+ )([0-9]+ [0-9]+ )+ COLOR THICKNESS USERID WHITEBOARDID<br>
		 * DRAWOVAL: draw oval [0-9]+ [0-9]+ [0-9]+ [0-9]+ COLOR THICKNESS USERID WHITEBOARDID<br>
		 * ENTER: enter USERNAME WHITEBOARDID<br>
		 * EXIT: exit USERNAME<br>
		 * BYE: bye
         * 
         * @param input
         * @return
         * @throws IOException
         */
        public String handleRequest(String input) throws IOException{
            String regex = "(makeuser [A-Za-z0-9]+ -?\\d+)|(makeboard -?\\d+)|(undo -?\\d+ -?\\d+)|"
                    + "(redo -?\\d+ -?\\d+)|"+
                    "(draw freehand( -?\\d+ -?\\d+)( -?\\d+ -?\\d+)+ (bl|y|r|g|o|m|blk|w) (s|m|l) -?\\d+ -?\\d+)|" +
                    "(draw oval -?\\d+ -?\\d+ -?\\d+ -?\\d+ (bl|y|r|g|o|m|blk|w) (s|m|l) -?\\d+ -?\\d+)|" +
                    "(switchboard [A-Za-z0-9]+ -?\\d+)|"
                    +"(enter [A-Za-z0-9]+ -?\\d+)|(exit [A-Za-z0-9]+)|(bye)";
            if ( ! input.matches(regex)) {
                // invalid input
                System.out.println("client msg: " + input + " didn't match"); 
                return null;
            }
            String[] tokens = input.split(" ");
            if (tokens[0].equals("makeuser")){
                this.username = tokens[1];
                return(collaboard.addUser(tokens[1]));
            }
            if (tokens[0].equals("makeboard")){
                int whiteboardID = Integer.parseInt(tokens[1]);
                if (collaboard.existingWhiteboard(whiteboardID)){
                    return "whiteboardtaken";
                }
                collaboard.createNewWhiteboard(whiteboardID);
                return "validwhiteboard";
                //addboard
            }
            if (tokens[0].equals("switchboard")){
                currentWhiteboardID = Integer.parseInt(tokens[2]);
                if (!collaboard.existingWhiteboard(currentWhiteboardID)){ 
                    //if the whiteboard doesn't already exist, create a new one
                    collaboard.createNewWhiteboard(currentWhiteboardID);
                }
                Whiteboard whiteboard = collaboard.getWhiteboards().get(currentWhiteboardID);
                whiteboard.addUser(tokens[1]);
                StringBuilder message = new StringBuilder();
                ArrayList<String> users = whiteboard.getUsers();
                for (int i=0; i < users.size(); i++){
                    message.append("\nenter " + users.get(i));
                } 
                CanvasModel canvasModel = whiteboard.getCanvasModel();
                for (int i = 0; i < canvasModel.getListSize(); i++){
                    DrawingObject o = canvasModel.getIthDrawingObject(i);
                    message.append("\ninitdraw " + o.toString());
                }
                message.append("\nundoindex " + canvasModel.getUndoIndex());
                for (UserThread t: threads){
                    System.out.println("currentwhiteboardID: "+ currentWhiteboardID);
                    System.out.println("Thread whiteboardID: "+ t.getCurrentWhiteboardID());
                    System.out.println("UserID "+ userID);
                    System.out.println("Thread UserID "+ t.getUserID());
                    //find the threads that are on the same whiteboard and send the enter request to them.
                    if ((currentWhiteboardID == t.getCurrentWhiteboardID()) && (userID != t.getUserID())){
                        PrintWriter output = t.getPrintWriter();
                        output.println("enter " + tokens[1]);
                    }
                }
                return message.toString();
            }
            if (tokens[0].equals("enter")){ 
                currentWhiteboardID = Integer.parseInt(tokens[2]);
                Whiteboard whiteboard = collaboard.getWhiteboards().get(currentWhiteboardID);
                whiteboard.addUser(tokens[1]);
                StringBuilder message = new StringBuilder("users");
                ArrayList<String> users = whiteboard.getUsers();
                for (int i=0; i < users.size(); i++){
                    message.append(" " + users.get(i));
                }
                message.append("\nready");    
                CanvasModel canvasModel = whiteboard.getCanvasModel();
                for (int i = 0; i < canvasModel.getListSize(); i++){
                    DrawingObject o = canvasModel.getIthDrawingObject(i);
                    message.append("\ninitdraw " + o.toString());
                }

                message.append("\nundoindex " + canvasModel.getUndoIndex());
                //send the user a list of users and a list of objects already drawn.
                System.out.println("Sending this message: " + message);
                for (UserThread t: threads){
                    //find the threads that are on the same whiteboard and send the enter request to them.
                    if ((currentWhiteboardID == t.getCurrentWhiteboardID()) && (t.getUserID() != userID)){
                        PrintWriter output = t.getPrintWriter();
                        output.println("enter " + tokens[1]);
                    }
                }
                return message.toString();
            }
            if (tokens[0].equals("exit")){
                for (UserThread t: threads){
                    //find the threads that are on the same whiteboard and send the enter request to them.
                    if ((currentWhiteboardID == t.getCurrentWhiteboardID()) && (t.getUserID() != userID)){
                        PrintWriter output = t.getPrintWriter();
                        output.println("exit " + tokens[1]);
                    }
                }
                Whiteboard whiteboard = collaboard.getWhiteboards().get(currentWhiteboardID);
                whiteboard.removeUser(tokens[1]);
                currentWhiteboardID = 0;
            }
            if (tokens[0].equals("undo")|tokens[0].equals("redo")|tokens[0].equals("draw")){
                //add the message pertaining to whiteboard edits to the queue to prevent concurrency problems.
                requests.add(tokens);
            }
            if (tokens[0].equals("bye")){
            	collaboard.removeUsername(this.username);
            	//then end the socket connection.
            }
            return "";
            
        }
    }

    public static void main(String[] args) {
        int port = 4444; // default port
        try {
            runCollaboardServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Run the server.
     * @param port
     * @throws IOException
     */
    public static void runCollaboardServer(int port) throws IOException {
        CollaboardServer server = new CollaboardServer(port, new Collaboard());
        server.serve();
    }
}
