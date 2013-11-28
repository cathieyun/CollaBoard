package whiteboard;

import java.util.ArrayList;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

import canvas.Canvas;

/**
 * Whiteboard: Canvas + MessageQueue + WhiteboardID + UndoManager + UsersList
 * 
 * 
 * 
 * @author Eric Ruleman
 * 
 */
public class Whiteboard {
	private Canvas canvas;
	private ArrayList<Username> userList = new ArrayList<Username>();
	private ArrayList<Line> lineList = new ArrayList<Line>();

	/**
	 * The constructor for the Whiteboard class that takes one parameter, the
	 * username who created the whiteboard. The Canvas for this Whiteboard will
	 * default to 800 by 600.
	 * 
	 * @param username
	 *            the username who created the whiteboard
	 */
	public Whiteboard(Username username) {
		new Whiteboard(800, 600, username);
	}

	/**
	 * The constructor for the Whiteboard class that takes three parameters, the
	 * width and height of the canvas to be constructed, as well as the username
	 * who created the whiteboard.
	 * 
	 * 
	 * @param width
	 *            the width of the canvas to construct
	 * @param height
	 *            the height of the canvas to construct
	 * @param username
	 *            the username who created the Whiteboard
	 */
	public Whiteboard(int width, int height, Username username) {
		this.canvas = new Canvas(width, height);
		userList.add(username);
	}

	/**
	 * addUser() is called when a user enters a Whiteboard. Adds the username to
	 * the userList.
	 * 
	 * @param username
	 *            the username of the user who entered the Whiteboard.
	 */
	public void addUser(Username username) {
		userList.add(username);
	}

	/**
	 * removeUser() is called when a user exits a Whiteboard. Removes the
	 * username from userList.
	 * 
	 * @param username
	 *            the username of the user who exited the Whiteboard
	 */
	public void removeUser(Username username) {
		userList.remove(username);
	}
}
