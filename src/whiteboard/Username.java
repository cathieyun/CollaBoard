package whiteboard;

import java.util.ArrayList;

/**
 * The Username class represents a username in the system. Duplicate usernames
 * are not allowed, so the static variable usernamesTaken is used to prevent
 * duplicate usernames. When a user leaves the Collaboard program, his/her
 * username is removed from the list usernamesTaken by calling delete() on the
 * username object.
 * 
 * @author Eric Ruleman
 * 
 */
public class Username {
	private static ArrayList<Username> usernamesTaken;
	private String name;

	/**
	 * The constructor method for the username class. Throws a RuntimeException
	 * if the requestedUsername already exists.
	 * 
	 * Note: Usernames must alpha-numeric and up to 20 characters long. A
	 * username that does not meet these qualifications will be handled by the
	 * actionListeners on the GUI.
	 * 
	 * @param requestedUsername
	 *            an alphanumeric username up to 20 characters long
	 */
	public Username(String requestedUsername) {
		// check for duplicate usernames
		for (Username n : usernamesTaken) {
			if (requestedUsername.equals(n.toString())) {
				throw new RuntimeException(
						"Duplicate username. Please choose another");
			}
		}

		name = requestedUsername;
	}

	public String toString() {
		return this.name;
	}

	/**
	 * delete() is called when a user exits the Collaboard program. The username
	 * is removed from the list usernamesTake, which allows the username to be
	 * used again.
	 */
	public void delete() {
		usernamesTaken.remove(this.name);
	}

}
