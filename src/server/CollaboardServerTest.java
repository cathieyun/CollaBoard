
package server;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.junit.Test;

import collaboard.TestUtil;

/**
 * Testing strategy:
 * 1. Check various protocol messages (Eric: This is done.)
 * 2. Check that everything is being mutated how we would expect it to. (Eric: Not sure how to do this.)
 * 3. ?
 *
 */


public class CollaboardServerTest {
	PrintWriter out;
	BufferedReader in;

	/**
	 * This testing suite covers client-server protocol messages, including clients connecting to the server and 
	 * users entering, exiting, and switching whiteboards.
	 * 
	 * 
	 * Testing Strategy:
	 * Client 1 connects, and attempts to create the username "funkyPistol". Server should respond with "validuser".
	 * Client 2 connects, and attempts to create the username "theStrokes". Server should respond with "validuser".
	 * Client 1 makes whiteboard 13. Server should reply "newboard 13" then "validwhiteboard".
	 * Client 2 should be alerted of whiteboard 13's existence.

	 * 
	 * Client 3 connects, and attempts to create the username "theStrokes". Server should respond with "usertaken".
	 * Client 3 creates the username rollingStones; server should respond with "validuser".
	 * Client 3 attempts to create the whiteboard 13, server should respond with "whiteboardtaken".
	 * Client 3 creates the whiteboard 15, server should respond with "validwhiteboard".
	 * Clients 1 and 2 should be alerted that Client 3 created whiteboard 15.
	 * 
	 * Client 1 switches from whiteboard 13. Server responds with an empty string.
	 * Client 1 (username = funkyPistol, userID = 0) enters whiteboard 15.
	 * Client 1 disconnects from the Collaboard program
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(timeout = 10000)
	public void clientServerTest() throws IOException, InterruptedException {
		// initialize the server
		final int port = 4444;
		TestUtil.startServer(port); // initialize the server on port 4444
		
		
		// Avoid race where we try to connect to server too early
		Thread.sleep(100);

		try {
			// open a socket between client 1 and the server
			Socket sock = TestUtil.connect(port);
			in = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			assertEquals("userID 0", in.readLine()); // our client connection should be assigned userID 0
			assertEquals("list", in.readLine()); // server sends us the empty list of whiteboards

			// have client 1 attempt to create the username funkyPistol; server should respond with "validuser"
			out.println("makeuser funkyPistol 5");
			assertEquals("validuser", in.readLine());

			// open a socket between client 2 and the server
			Socket sock2 = TestUtil.connect(port);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(
					sock2.getInputStream()));
			PrintWriter out2 = new PrintWriter(sock2.getOutputStream(), true);
			assertEquals("userID 1", in2.readLine()); // our client connection should be assigned userID 1
			assertEquals("list", in2.readLine()); // server sends us the empty list of whiteboards
			
			// have client 2 attempt to create the username theStrokes; server should respond with "validuser"
			out2.println("makeuser theStrokes 6");
			assertEquals("validuser", in2.readLine());
			
			// have client 1 make the whiteboard 13; server should reply "newboard 13" then "validwhiteboard"
			out.println("makeboard 13");
			assertEquals("newboard 13", in.readLine());
			assertEquals("validwhiteboard", in.readLine());
			// client 2 should be alerted of whiteboard 13's existence
			assertEquals("newboard 13", in2.readLine());
			
			// open a socket between client 3 and the server
			Socket sock3 = TestUtil.connect(port);
			BufferedReader in3 = new BufferedReader(new InputStreamReader(
					sock3.getInputStream()));
			PrintWriter out3 = new PrintWriter(sock3.getOutputStream(), true);
			assertEquals("userID 2", in3.readLine()); // our client connection should be assigned userID 2
			assertEquals("list 13", in3.readLine()); // server sends us the list of whiteboards (#13)
			
			// have client 3 attempt to create the username theStrokes; server should respond with "usertaken"
			out3.println("makeuser theStrokes 7");
			assertEquals("usertaken", in3.readLine());
			
			// have client 3 attempt to create the username rollingStones; server should respond with "validuser"
			out3.println("makeuser rollingStones 7");
			assertEquals("validuser", in3.readLine());
			
			// have client 3 attempt to create the whiteboard 13, server should respond with "whiteboardtaken"
			out3.println("makeboard 13");
			assertEquals("whiteboardtaken", in3.readLine());
			
			// have client 3 attempt to create the whiteboard 15, server should respond with "validwhiteboard"
			out3.println("makeboard 15");
			assertEquals("newboard 15", in3.readLine());
			assertEquals("validwhiteboard", in3.readLine());
			
			// client 1 and 2 should be alerted that client 3 created whiteboard 15
			assertEquals("newboard 15", in.readLine());
			assertEquals("newboard 15", in2.readLine());
			
			// have client 1 switch from whiteboard 13
			out.println("switchboard funkyPistol 0 13");
						
			// have funkyPistol (userID 0) enter whiteboard 15
			out.println("enter funkyPistol 0 15");
			assertEquals("enter funkyPistol", in.readLine());
			assertEquals("undoindex 0", in.readLine());
            assertEquals("ready", in.readLine());
			assertEquals("enter funkyPistol", in.readLine());
			assertEquals("undoindex 0", in.readLine());
						
			// have Client 1 disconnect from the Collaboard program
			out.println("bye");
						
			sock.close();
			sock2.close();
			sock3.close();
		} catch (SocketTimeoutException e) {
			throw new RuntimeException(e);
		}
	}
}
