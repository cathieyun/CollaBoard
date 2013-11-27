package server;

import java.io.IOException;
import java.net.ServerSocket;

import collaboard.Collaboard;


public class WhiteboardServer {

    private final ServerSocket serverSocket;
    private Collaboard collaboard;
    public WhiteboardServer(int port) throws IOException{
        serverSocket = new ServerSocket(port);
    }
    
    
    private void handleConnection(Collaboard collaboard){
        this.collaboard = collaboard;
        //TODO: a lot of stuff
    }
    
    public class UserConnection implements Runnable{
        @Override
        public void run() {
            // TODO Auto-generated method stub
            
        }
    }
}
