package whiteboard;
import static org.junit.Assert.*;

import org.junit.Test;

public class WhiteboardTest {
    //Check that adding users works.
    @Test
    public void addUserTest(){
        Whiteboard w = new Whiteboard(1);
        w.addUser("bob");
        w.addUser("mary");
        assertEquals(2, w.getUsers().size());
        assertEquals(w.getUsers().toString(), "[bob, mary]");
    }
    //Check that removing users works.
    @Test
    public void removeUserTest(){
        Whiteboard w = new Whiteboard(1);
        w.addUser("a");
        w.addUser("b");
        w.addUser("c");
        w.removeUser("b");
        assertEquals(2, w.getUsers().size());
        assertEquals(w.getUsers().toString(), "[a, c]");
    }
}
