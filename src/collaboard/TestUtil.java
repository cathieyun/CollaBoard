package collaboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import server.CollaboardServer;

public class TestUtil {
    
    private static final int port = 4444; // default port
    private static final String portStr = Integer.toString(port);

    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...

    public static void startServer() {
      
      startServer(true ? "--debug" : "--no-debug", "--port", portStr, "--file");
    }

    // ...
    // ...
    // ...

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

    private static void startServer(final String... args) {
        final String myArgs[] = args;
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

    public static boolean eqNoSpace(String s1, String s2) {
        return s1.replaceAll("\\s+", "").equals(s2.replaceAll("\\s+", ""));
    }

    public static String nextNonEmptyLine(BufferedReader in) throws IOException {
        while (true) {
            String ret = in.readLine();
            if (ret == null || !ret.equals(""))
                return ret;
        }
    }
}
