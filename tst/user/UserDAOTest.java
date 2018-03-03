package user;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import dynamodb.DynamoDBMapperWrapper;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDAOTest {

    private static final String USERID = "d2fadc3b-b791-4054-b51e-49be4beb24c7";

    private DynamoDBMapperWrapper mapper;
    private UserDAO dao;

    @Before
    public void setUp() {
        mapper = mock(DynamoDBMapperWrapper.class);

        dao = new UserDAO(mapper);
    }

    @Test
    public void fetchUser() {
        final User user = new User();
        user.setLogin(USERID);
        when(mapper.load(User.class, USERID)).thenReturn(Optional.of(user));

        final Optional<User> result = dao.fetch(USERID);

        assertEquals(user, result.get());
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void fetchUserDynamoException() {
        when(mapper.load(User.class, USERID)).thenThrow(new AmazonDynamoDBException("test"));

        dao.fetch(USERID);
    }
}
