package user;

import exceptions.UserNotFoundException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private static final String VALID_USERID = "d2fadc3b-b791-4054-b51e-49be4beb24c7";
    private static final String INVALID_USERID = "not a user";
    private UserDAO userDAO;
    private UserService service;

    @Before
    public void setUp() {
        userDAO = mock(UserDAO.class);

        service = new UserService(userDAO);
    }

    @Test
    public void fetchUser() {
        when(userDAO.fetch(VALID_USERID)).thenReturn(Optional.of(new User()));

        final Optional<User> result = service.fetch(VALID_USERID);

        assertTrue(result.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetchUserEmptyUserId() {
        service.fetch("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetchUserNullUserId() {
        service.fetch(null);
    }

    @Test
    public void throwIfUserNotExistsCachesNewUser() throws Exception {
        final User user = new User();
        user.setLogin(VALID_USERID);
        when(userDAO.fetch(VALID_USERID)).thenReturn(Optional.of(user));

        final User result = service.getUser(VALID_USERID);

        verify(userDAO).fetch(VALID_USERID);
        assertEquals(user, result);
    }

    @Test
    public void throwIfUserNotExistsUsesCache() throws Exception {
        final User user = new User();
        user.setLogin(VALID_USERID);
        when(userDAO.fetch(VALID_USERID)).thenReturn(Optional.of(user));
        service.getUser(VALID_USERID);


        final User result = service.getUser(VALID_USERID);

        verify(userDAO, times(1)).fetch(VALID_USERID);
        assertEquals(user, result);
    }

    @Test(expected = UserNotFoundException.class)
    public void throwIfUserNotExistsUserNotExists() throws Exception {
        when(userDAO.fetch(INVALID_USERID)).thenReturn(Optional.empty());

        service.getUser(INVALID_USERID);
    }
}
