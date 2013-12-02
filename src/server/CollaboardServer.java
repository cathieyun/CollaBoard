package server;



import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import whiteboard.Whiteboard;

import canvas.CanvasModel;
import canvas.DrawingObject;
import client.User;

import collaboard.Collaboard;
import collaboard.CollaboardGUI;

//TODO: Add a requests queue for the server to handle (I think it would be easier to
//implement than a BlockingQueue for each whiteboard.
//Make a new thread that is dedicated to handling requests.
public class CollaboardServer {

    private final ServerSocket serverSocket;
    private Collaboard collaboard;
    private AtomicInteger numClients;
    private HashMap<Integer, Thread> threads;
    private BlockingQueue<String> requests;
    public CollaboardServer(int port, Collaboard collaboard) throws IOException{
        this.serverSocket = new ServerSocket(port);
        this.collaboard = collaboard;
        this.numClients = new AtomicInteger(0);
        this.requests = new LinkedBlockingQueue<String>();
    }
    
    private void serve() throws IOException{
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            //allow the server to handle multiple connections
            UserThread thread = new UserThread(socket);
            // handle the current client
            thread.start();
        }
    }
    public class UserThread extends Thread{
        private Socket socket;
        private User user;
        private int userID;
        private InputStream inputStream;
        private OutputStream outputStream;
        public UserThread(Socket socket){
            this.socket= socket;
            userID = 0; //not sure if i need this, but let's just keep it for now
            //ensure that no two threads can access and increment numClients at the same time
            synchronized(numClients){
                //assign integer ID based on how many clients there are. doesn't really have to
                //reflect the actual # of clients, everyone just has to have a unique ID
                userID = numClients.intValue();
                numClients.getAndIncrement();
            }
            this.user = new User(userID);
            try{
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
            }catch(IOException e){
                e.printStackTrace();
            }
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
        private void handleConnection(Socket socket, int userID) throws IOException{
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter out = new PrintWriter(outputStream, true);
            try {
                out.println("userID " + userID); //send the userID
                StringBuilder message = new StringBuilder("list");
                for (int whiteboardID: collaboard.getWhiteboards().keySet()){
                    message.append(" " + whiteboardID);
                }
                out.println(message.toString()); //send the list of whiteboards
                for (String line = in.readLine(); line != null; line = in.readLine()) {
                    //System.out.println("Processing: " + line);
                    String output = handleRequest(line);
                    if (output != null) {
                        System.out.println("server response to " + line + ": " + output);
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
        
        public String handleRequest(String input) throws IOException{
            String regex = "(makeuser [A-Za-z0-9]+ -?\\d+)|(makeboard -?\\d+)|(undo -?\\d+ -?\\d+ -?\\d+)|"
                    + "(redo -?\\d+ -?\\d+ -?\\d+)|"
                    +"(draw -?\\d+ -?\\d+ -?\\d+ -?\\d+ (bl|y|r|g|o|m|blk|w) (s|m|l) -?\\d+ -?\\d+)|"
                    +"(enter [A-Za-z0-9]+ -?\\d+)| (exit [A-Za-z0-9]+ -?\\d+)|(bye)";
            if ( ! input.matches(regex)) {
                // invalid input
                System.out.println("client msg: " + input + " didn't match"); 
            }
            String[] tokens = input.split(" ");
            if (tokens[0].equals("makeuser")){
                System.out.println(tokens[1]);
                System.out.println(tokens[2]);
                System.out.println("Trying to make user:"+ tokens[1]);
                return(collaboard.addUser(Integer.parseInt(tokens[2]), tokens[1]));
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
            if (tokens[0].equals("enter")){ //TODO: Notify all threads in the same whiteboard that a new user has entered.
                System.out.println("received enter message");
                //add user to the whiteboard's list of users.
                Whiteboard whiteboard = collaboard.getWhiteboards().get(Integer.parseInt(tokens[2]));
                whiteboard.addUser(tokens[1]);
                StringBuilder message = new StringBuilder("users");
                ArrayList<String> users = whiteboard.getUsers();
                for (int i=0; i < users.size(); i++){
                    message.append(" " + users.get(i));
                }
                message.append("\n");
                CanvasModel canvasModel = whiteboard.getCanvasModel();
                for (int i = 0; i < canvasModel.getListSize(); i++){
                    DrawingObject o = canvasModel.getIthDrawingObject(i);
                    message.append("draw " + o.toString() + "\n");
                }
                message.append("ready");
                //this only works for freehands right now.
                //send the user a list of users and a list of objects already drawn.
                System.out.println("Sending this message: " + message);
                return message.toString();
            }
            if (tokens[0].equals("undo")){
                //undo, TODO: add to the server's event queue. (I think it would be easier to just implement a single queue for all whiteboards)
            }
            if (tokens[0].equals("redo")){
                //redo
            }
            if (tokens[0].equals("draw")){
                //draw
            }
            if (tokens[0].equals("bye")){
                //TODO: remove the user from the whiteboard's list of users, and from the list of taken usernames.
            }
            return "";
            
        }
    }

    public static void main(String[] args) {
        // Command-line argument parsing is provided. Do not change this method.
        
        int port = 4444; // default port

        try {
            runCollaboardServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runCollaboardServer(int port) throws IOException {
        CollaboardServer server = new CollaboardServer(port, new Collaboard());
        server.serve();
    }
}
