package server;
import static org.junit.Assert.*;
import org.junit.Test;
/**
 * Testing Strategy:
 * Ensure that non getter/setter methods behave as expected.
 * 1. Adding users - when someone tries to add an untaken username, it should be successful return "validuser"
 *                   when someone tries to add a taken username, it should not work and return "usertaken"
 * 2. Adding whiteboards - if someone tries to create a whiteboard with an existing ID it should throw
 *                   a RuntimeException. We will not run into this while running our system because 
 *                   this is already checked before calling createNewWhiteboard().
 *
 */
public class CollaboardTest {
    
    //check that "validuser" is returned when trying to add a username that has not been taken
    @Test
    public void addUntakenUsernameTest(){
        Collaboard c = new Collaboard();
        assertEquals("validuser", c.addUser("joe"));
    }
    //check that "usertaken" is returned when trying to add an existing username
    @Test
    public void takenUsernameTest(){
        Collaboard c = new Collaboard();
        c.addUser("bob");
        assertEquals("usertaken", c.addUser("bob"));
        
    }
    
    //check that existingWhiteboard() returns true if a whiteboardID is attached to an active whiteboard, else false
    @Test
    public void existingWhiteboardTest(){
        Collaboard c = new Collaboard();
        c.createNewWhiteboard(1);
        assertEquals(true, c.existingWhiteboard(1));
        assertEquals(false, c.existingWhiteboard(2));
    }
    //Check that createNewWhiteboard() throws an exception if someone tries to create a whiteboard with an ID that already exists
    @Test(expected=RuntimeException.class)
    public void takenWhiteboardIDTest(){
        Collaboard c = new Collaboard();
        c.createNewWhiteboard(1);
        c.createNewWhiteboard(1);
    }
}
