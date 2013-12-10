package collaboard;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import server.CollaboardServer;

/**
 * This class is used in CollaboardServerTest in order to initialize the server
 * and to handle connections from clients to the server.
 * 
 * Adapted from the published test of PS3.
 * 
 * @author Eric
 * 
 */
public class TestUtil {
    
    private static final int port = 4444; // default port

    public static void startServer() {
        new Thread(new Runnable() {
            public void run() {
                try {
//             	     System.out.println("Hey I got here in server test ColaboardServeryolo");
             	     CollaboardServer.runCollaboardServer(port);
               } catch (IOException e) {
                   e.printStackTrace();
               }
            }
        }).start();
    }

    public static Socket connect() throws IOException {
      Socket ret = null;
      final int MAX_ATTEMPTS = 50;
      int attempts = 0;
      do {
        try {
          ret = new Socket("127.0.0.1", port);
        } catch (ConnectException ce) {
          try {
            if (++attempts > MAX_ATTEMPTS)
              throw new IOException("Exceeded max connection attempts", ce);
            Thread.sleep(300);
          } catch (InterruptedException ie) {
            throw new IOException("Unexpected InterruptedException", ie);
          }
        }
      } while (ret == null);
      ret.setSoTimeout(3000);
      return ret;
    }
}
