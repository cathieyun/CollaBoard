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
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


import canvas.CanvasModel;
import canvas.DrawingObject;
import canvas.Freehand;
import canvas.Oval;


/**
 * Class representing the Collaboard server.
 * Stores the set of all Whiteboards and their current states, as well as current
 * active threads.
 */
public class CollaboardServer {

    private final ServerSocket serverSocket;
    private Collaboard collaboard;
    private AtomicInteger numClients; //keeps track of the number of clients in order to assign them each a unique user ID.
    private ArrayList<UserThread> threads;
    private BlockingQueue<String[]> requests;
    private HashMap<Integer, UserThread> threadsByID;
    
    public CollaboardServer(int port, Collaboard collaboard) throws IOException{
        this.serverSocket = new ServerSocket(port);
        this.threads = new ArrayList<UserThread>();
        this.threadsByID = new HashMap<Integer, UserThread>();
        this.collaboard = collaboard;
        this.numClients = new AtomicInteger(0);
        this.requests = new LinkedBlockingQueue<String[]>();
    }
    
    /**
     * Runs the server, listening for client connections and handling them.
     * Also starts a requestHandler thread dedicated to handling possible concurrent edits.
     * @throws IOException if the main server socket is broken.
     */
    private void serve() throws IOException{

        Thread requestHandler = new Thread(new Runnable(){
            /**
             * Thread that handles requests from the requests BlockingQueue, to ensure that edits are made
             * to each whiteboard in the order they are put on the queue, to ensure thread safety.
             */
            @Override
            public void run() {
                while(true){
                    String[] currentRequest = requests.poll();
                    if (currentRequest != null){
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
                    StringBuilder outputMsg = new StringBuilder();
                    if (request[0].equals("enter")|request[0].equals("exit")|request[0].equals("switchboard")){
                        int userID = Integer.parseInt(request[2]);
                        if (request[0].equals("enter")){ 
                            Whiteboard whiteboard = collaboard.getWhiteboards().get(whiteboardID);
                            whiteboard.addUser(request[1]);
                            StringBuilder message = new StringBuilder("users");
                            ArrayList<String> users = whiteboard.getUsers();
                            for (int i=0; i < users.size(); i++){ //send the list of users currently on the Whiteboard.
                                message.append(" " + users.get(i));
                            }
                            message.append("\nready"); //tell the client to initialize the Canvas.
                            CanvasModel canvasModel = whiteboard.getCanvasModel();
                            for (int i = 0; i < canvasModel.getListSize(); i++){ //send the list of drawing objects currently in the CanvasModel
                                DrawingObject o = canvasModel.getIthDrawingObject(i);
                                message.append("\ninitdraw " + o.toString());
                            }
                            message.append("\nundoindex " + canvasModel.getUndoIndex());
                            threadsByID.get(userID).getPrintWriter().println(message.toString());
                            outputMsg.append("enter ");
                        }
                        if (request[0].equals("exit")){
                            Whiteboard whiteboard = collaboard.getWhiteboards().get(whiteboardID);
                            whiteboard.removeUser(request[1]);
                            outputMsg.append("exit ");
                        }
                        if (request[0].equals("switchboard")){
                            if (!collaboard.existingWhiteboard(whiteboardID)){ 
                                //if the whiteboard doesn't already exist, create a new one
                                collaboard.createNewWhiteboard(whiteboardID);
                            }
                            Whiteboard whiteboard = collaboard.getWhiteboards().get(whiteboardID);
                            whiteboard.addUser(request[1]); //add the user to the list of active users
                            StringBuilder message = new StringBuilder();
                            ArrayList<String> users = whiteboard.getUsers();
                            for (int i=0; i < users.size(); i++){ //send the list of active users to populate the client's active users table
                                message.append("\nenter " + users.get(i));
                            } 
                            CanvasModel canvasModel = whiteboard.getCanvasModel();
                            for (int i = 0; i < canvasModel.getListSize(); i++){ //send the list of drawing objects in the CanvasModel
                                DrawingObject o = canvasModel.getIthDrawingObject(i);
                                message.append("\ninitdraw " + o.toString());
                            }
                            message.append("\nundoindex " + canvasModel.getUndoIndex());
                            threadsByID.get(userID).getPrintWriter().println(message.toString());
                            outputMsg.append("enter ");
                        }
                        for (UserThread t: threads){
                            //find the threads that are on the same whiteboard and send the enter request to them.
                            if ((whiteboardID == t.getCurrentWhiteboardID()) && (t.getUserID() != userID)){
                                PrintWriter output = t.getPrintWriter();
                                output.println(outputMsg.toString() + request[1]);
                            }
                        }
                    }
                    if (request[0].equals("undo")|request[0].equals("redo")){
                        outputMsg.append(request[0]);
                        if (request[0].equals("undo")){
                            collaboard.getCanvasModelByID(whiteboardID).decrementIndex();
                        }
                        else{
                            collaboard.getCanvasModelByID(whiteboardID).incrementIndex();
                        }
                    }
                    else if (request[0].equals("draw")){
                        String color = request[request.length-4];
                        String thickness = request[request.length-3];
                        collaboard.getCanvasModelByID(whiteboardID).preventRedoAfterThisEdit();
                        if(request[1].equals("freehand")){
                            int [] points = new int[request.length-6];
                            for (int i=0; i < points.length; i++){
                                points[i] = Integer.parseInt(request[i+2]);
                            }
                            Freehand freehand = new Freehand(points, color, thickness);
                            collaboard.getCanvasModelByID(whiteboardID).addDrawingObject(freehand);
                            
                        }
                        if(request[1].equals("oval")){
                            Oval oval = new Oval(Integer.parseInt(request[2]), Integer.parseInt(request[3]), Integer.parseInt(request[4]), Integer.parseInt(request[5]), color, thickness);
                            collaboard.getCanvasModelByID(whiteboardID).addDrawingObject(oval);
                        }
                        collaboard.getWhiteboards().get(whiteboardID).getCanvasModel().incrementIndex();
                        outputMsg.append("draw");
                        for (int i = 1; i < request.length-2; i++){
                            outputMsg.append(" " + request[i]);
                        }
                    }
                    //Send the message to all other threads on the same whiteboard to update their canvases.
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
            userID = numClients.getAndIncrement();
            threads.add(this);
            threadsByID.put(userID, this);
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
                    threads.remove(this);
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
         * @throws IOException if the connection has an error or terminates unexpectedly
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
                threads.remove(this);
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
		 * SWITCH: switchboard USERNAME USERID WHITEBOARDID<br>
		 * UNDO: undo USERID WHITEBOARDID<br>
 		 * REDO: redo USERID WHITEBOARDID<br>
 		 * COLOR: bl|y|r|g|o|m|blk|w<br>
 		 * THICKNESS: s|m|l<br>
		 * DRAWFREEHAND: draw freehand ([0-9]+ [0-9]+ )([0-9]+ [0-9]+ )+ COLOR THICKNESS USERID WHITEBOARDID<br>
		 * DRAWOVAL: draw oval [0-9]+ [0-9]+ [0-9]+ [0-9]+ COLOR THICKNESS USERID WHITEBOARDID<br>
		 * ENTER: enter USERNAME USERID WHITEBOARDID<br>
		 * EXIT: exit USERNAME USERID WHITEBOARDID<br>
		 * BYE: bye
         * 
         * @param input
         * @return server response to the Client's message.
         */
        public String handleRequest(String input){
            String regex = "(makeuser [A-Za-z0-9]+ \\d+)|(makeboard \\d+)|(undo \\d+ \\d+)|"
                    + "(redo \\d+ \\d+)|"+
                    "(draw freehand( -?\\d+ -?\\d+)( -?\\d+ -?\\d+)+ (bl|y|r|g|o|m|blk|w) (s|m|l) \\d+ \\d+)|" +
                    "(draw oval -?\\d+ -?\\d+ -?\\d+ -?\\d+ (bl|y|r|g|o|m|blk|w) (s|m|l) \\d+ \\d+)|" +
                    "(switchboard [A-Za-z0-9]+ \\d+ \\d+)|"
                    +"(enter [A-Za-z0-9]+ \\d+ \\d+)|(exit [A-Za-z0-9]+ \\d+ \\d+)|(bye)";
            if ( ! input.matches(regex)) {
                // invalid input
                System.out.println("client msg: " + input + " didn't match"); 
                return null;
            }
            String[] tokens = input.split(" ");
            if (tokens[0].equals("makeuser")){
                this.username = tokens[1];
                //returns "validuser" and adds the username to its list of users if not taken, else "usertaken"
                return(collaboard.addUser(tokens[1])); 
            }
            if (tokens[0].equals("makeboard")){
                int whiteboardID = Integer.parseInt(tokens[1]);
                //returns "validwhiteboard" and creates the associated whiteboard if the ID is not taken, else "whiteboardtaken"
                return(collaboard.addWhiteboard(whiteboardID));
            }
            if (tokens[0].equals("switchboard")){
                currentWhiteboardID = Integer.parseInt(tokens[2]);
            }
            if (tokens[0].equals("enter")){ 
                currentWhiteboardID = Integer.parseInt(tokens[2]);
            }
            if (tokens[0].equals("exit")){
                currentWhiteboardID = 0;
            }
            if (!tokens[0].equals("bye")){
                //add the message pertaining to whiteboard edits to the queue to prevent concurrency problems.
                requests.add(tokens);
            }
            if (tokens[0].equals("bye")){
            	collaboard.removeUsername(this.username);
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
