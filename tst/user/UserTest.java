package user;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class UserTest {

    private static final String USERID = "d2fadc3b-b791-4054-b51e-49be4beb24c7";

    @Test
    public void equal() {
        final User user1 = new User();
        user1.setLogin(USERID);
        final User user2 = new User();
        user2.setLogin(USERID);

        assertTrue(user1.equals(user2));
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void notEqual() {
        final User user1 = new User();
        user1.setLogin(USERID);
        final User user2 = new User();

        assertFalse(user1.equals(user2));
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }
}
