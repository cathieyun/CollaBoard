package server;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.junit.Before;
import org.junit.Test;

import collaboard.TestUtil;

/**
 * Testing strategy:
 * 1. Check various protocol messages
 * 2. Check that everything is being mutated how we would expect it to.
 * 3. ?
 *
 */


public class CollaboardServerTest {
	PrintWriter out;
	BufferedReader in;
	
	private void setUp() {
		TestUtil.startServer();
	}

	/**
	 * Tests the "makeuser [A-Za-z0-9]+ -?\\d+" client-server protocol.
	 * 
	 * Client 1 connects, and attempts to create the username funkyPistol. Server should respond with "validuser".
	 * Client 2 connects, and attempts to create the username theStrokes. Server should respond with "validuser".
	 * Client 3 connects, and attempts to create the username theStrokes. Server should respond with "usertaken".
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(timeout = 10000)
	public void makeUsersTest() throws IOException, InterruptedException {
		setUp();
		// Avoid race where we try to connect to server too early
		Thread.sleep(100);

		try {
			// open a socket between client 1 and the server
			Socket sock = TestUtil.connect();
			in = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			assertEquals("userID 0", in.readLine()); // our client connection should be assigned userID 0
			assertEquals("list", in.readLine()); // server sends us the empty list of whiteboards

			// have client 1 attempt to create the username funkyPistol; server should respond with "validuser"
			out.println("makeuser funkyPistol 5");
			assertEquals("validuser", in.readLine());

			// open a socket between client 2 and the server
			Socket sock2 = TestUtil.connect();
			BufferedReader in2 = new BufferedReader(new InputStreamReader(
					sock2.getInputStream()));
			PrintWriter out2 = new PrintWriter(sock2.getOutputStream(), true);
			assertEquals("userID 1", in2.readLine()); // our client connection should be assigned userID 1
			assertEquals("list", in2.readLine()); // server sends us the empty list of whiteboards
			
			// have client 2 attempt to create the username theStrokes; server should respond with "validuser"
			out2.println("makeuser theStrokes 6");
			assertEquals("validuser", in2.readLine());
			
			// have client 1 make the whiteboard 13; server should reply "validwhiteboard"
			out.println("makeboard 13");
			assertEquals("validwhiteboard", in.readLine());
			
			// open a socket between client 3 and the server
			Socket sock3 = TestUtil.connect();
			BufferedReader in3 = new BufferedReader(new InputStreamReader(
					sock3.getInputStream()));
			PrintWriter out3 = new PrintWriter(sock3.getOutputStream(), true);
			assertEquals("userID 2", in3.readLine()); // our client connection should be assigned userID 2
			assertEquals("list 13", in3.readLine()); // server sends us the list of whiteboards (#13)
			
			// have client 3 attempt to create the username theStrokes; server should respond with "usertaken"
			out3.println("makeuser theStrokes 7");
			assertEquals("usertaken", in3.readLine());
			
			// have client 3 attempt to create the whiteboard 13, server should respond with "whiteboardtaken"
			out3.println("makeboard 13");
			assertEquals("whiteboardtaken", in3.readLine());
			
			// have client 1 switch to whiteboard 15, which will need to be created since it doesn't exist yet
			out.println("switchboard 15");
			assertEquals("enter 0", in.readLine());

//			sock.close();
//			sock2.close();
//			sock3.close();
		} catch (SocketTimeoutException e) {
			throw new RuntimeException(e);
		}
	}
}
