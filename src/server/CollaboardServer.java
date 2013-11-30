package server;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import client.User;

import collaboard.Collaboard;
import collaboard.CollaboardGUI;


public class CollaboardServer {

    private final ServerSocket serverSocket;
    private Collaboard collaboard;
    private AtomicInteger numClients;
    private HashMap<Integer, Thread> threads;
    public CollaboardServer(int port, Collaboard collaboard) throws IOException{
        this.serverSocket = new ServerSocket(port);
        this.collaboard = collaboard;
        this.numClients = new AtomicInteger(0);
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
                handleConnection(socket, user, userID);
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
        private void handleConnection(Socket socket, User user, int userID) throws IOException{
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter out = new PrintWriter(outputStream, true);
            try {
                out.println("userID " + userID); //send the userID
                for (String line = in.readLine(); line != null; line = in.readLine()) {
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
                    + "(redo -?\\d+ -?\\d+ -?\\d+)|(whiteboards)|"
                    +"(draw -?\\d+ -?\\d+ -?\\d+ -?\\d+ (blue|yellow|red|green|orange|magenta|black|white) (small|med|large) -?\\d+ -?\\d+)|"
                    +"(enter [A-Za-z0-9]+ -?\\d+)| (exit [A-Za-z0-9]+ -?\\d+)|(bye)";
            if ( ! input.matches(regex)) {
                // invalid input
                System.out.println("client msg: " + input + " didn't match"); 
            }
            String[] tokens = input.split(" ");
            if (tokens[0].equals("makeuser")){
                System.out.println(tokens[1]);
                System.out.println(tokens[2]);
                collaboard.addUser(Integer.parseInt(tokens[2]), tokens[1]);
                System.out.println("Made user:"+ tokens[1]);
                return null;
            }
            if (tokens[0].equals("makeboard")){
                int whiteboardID = Integer.parseInt(tokens[1]);
                if (collaboard.existingWhiteboard(whiteboardID)){
                    return "error 1";
                }
                collaboard.createNewWhiteboard(whiteboardID);
                return null;
                //addboard
            }
            if (tokens[0].equals("whiteboards")){
                StringBuilder message = new StringBuilder("list");
                for (int whiteboardID: collaboard.getWhiteboards().keySet()){
                    message.append(" " + whiteboardID);
                }
                return message.toString();
            }
            if (tokens[0].equals("undo")){
                //undo
            }
            if (tokens[0].equals("redo")){
                //redo
            }
            if (tokens[0].equals("draw")){
                //draw
            }
            if (tokens[0].equals("bye")){
                
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
