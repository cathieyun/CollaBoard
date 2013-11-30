package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import collaboard.CollaboardGUI;

public class Client {
    private int userID;
    private int port;
    private Socket socket;
    private CollaboardGUI gui;
    private User user;
    private PrintWriter out;
    private BufferedReader in;
    public Client(int port){
        this.port = port;
    }
    
    public void run(){
        try {
            socket = new Socket("127.0.0.1", port);
            handleServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Server not running");
            }
    }
    
    private void handleServer() throws IOException{
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter out = new PrintWriter(outputStream, true);
        String fromServer = in.readLine(); //get the userID and initialize userObject first.
        handleRequest(fromServer);
        System.out.println(fromServer);
        gui = new CollaboardGUI(user, outputStream, inputStream);
        gui.setVisible(true);
        while ((fromServer = in.readLine()) != null){
            handleRequest(fromServer);
       }
        
    }
    
    private void handleRequest(String input){
        String regex = "(userID [0-9]+)|(update)|"
                + "(usertaken)|(whiteboardtaken)|(list( -?\\d+)*)|"
                +"(enter [A-Za-z0-9]+)| (exit [A-Za-z0-9]+)| (resend ([A-Za-z0-9]( )*)+)";
        if ( ! input.matches(regex)) {
            // invalid input
            System.out.println("server msg: "+ input + " didn't match");
        }
        String[] tokens = input.split(" ");
        if (tokens[0].equals("userID")){
            this.userID = Integer.parseInt(tokens[1]);
            user = new User(userID);
        }
        if (tokens[0].equals("usertaken")){
            //display error message on gui
        }
        if (tokens[0].equals("whiteboardtaken")){
            //display error message on gui
        }
        if (tokens[0].equals("list")){
            for (int i = 1; i < tokens.length; i++){
               gui.getWhiteboards().add(new Integer(tokens[i]));
            }
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
    }
    public static void main(String[]args){
        Client client = new Client(4444);
        client.run();
    }
}
