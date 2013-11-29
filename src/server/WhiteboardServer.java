package server;

import grammar.ClientListener;
import grammar.ProtocolLexer;
import grammar.ProtocolParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import collaboard.Collaboard;
import collaboard.CollaboardGUI;


public class WhiteboardServer {

    private final ServerSocket serverSocket;
    private Collaboard collaboard;
    private AtomicInteger numClients;
    public WhiteboardServer(int port, Collaboard collaboard) throws IOException{
        this.serverSocket = new ServerSocket(port);
        this.collaboard = collaboard;
        this.numClients = new AtomicInteger(0);
    }
    
    private void serve() throws IOException{
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            //allow the server to handle multiple connections
            Thread thread = new Thread(new UserConnection(socket));
            // handle the current client
            thread.start();
        }
    }
    private void handleConnection(Socket socket, User user) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        try {
            CollaboardGUI gui = new CollaboardGUI(collaboard, user);
            gui.setVisible(true);
            String line = in.readLine();
//            String output = collaboard.checkUsernameValidity(line);
//            if (output != null){
//                out.println(output);
//            }
            for (line = in.readLine(); line != null; line = in.readLine()) {
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
            out.close();
            in.close();
        }
    }
    
    public String handleRequest(String input){
        //run parser
        return "";
        
    }
    public class UserConnection implements Runnable{
        
        private Socket socket;
        private User user;
        public UserConnection(Socket socket){
            this.socket= socket;
            int userID = 0;
            //ensure that no two threads can access and increment numClients at the same time
            synchronized(numClients){
                //assign integer ID based on how many clients there are. doesn't really have to
                //reflect the actual # of clients, everyone just has to have a unique ID
                userID = numClients.intValue();
                numClients.getAndIncrement();
            }
            this.user = new User(userID);
        }

        @Override
        public void run() {
            try {
                handleConnection(socket, user);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 
        }
    }
    /**
     * Runs Listener on the contents of the input String
     * @param input - String to be parsed
     * @throws IOException
     */
    public void runClientListener(String input) throws IOException {
        // Create a stream of tokens using the lexer.
        CharStream x = new ANTLRInputStream(input);
        CharStream stream = (CharStream)x;
        ProtocolLexer lexer = new ProtocolLexer(stream);
        lexer.reportErrorsAsExceptions();
        TokenStream tokens = new CommonTokenStream(lexer);
            
        // Feed the tokens into the parser.
        ProtocolParser parser = new ProtocolParser(tokens);
        parser.reportErrorsAsExceptions();
            
        // Generate the parse tree using the starter rule.
        ParseTree tree;
        tree = parser.line(); // "line" is the starter rule.
            
        // Walk the tree with the listener.
        ParseTreeWalker walker = new ParseTreeWalker();
        ClientListener listener = new ClientListener(collaboard);
        //walker.walk(listener, tree);
        //((ABCListener) listener).getMusic().toPlayableFormat();
        //return ((ABCListener) listener).getMusic();
        
    }
}
