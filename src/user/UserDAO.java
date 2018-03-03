package user;

import dynamodb.DynamoDBMapperWrapper;
import java.util.Optional;

/**
 * Retrieves users from DynamoDB.
 */
public class UserDAO {

    private final DynamoDBMapperWrapper mapper;

    public UserDAO(DynamoDBMapperWrapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Fetch an existing user from dynamo by their login.
     *
     * @param login the login of the user to retrieve
     * @return an Optional of the User with the specified login
     */
    public Optional<User> fetch(String login) {
        return mapper.load(User.class, login);
    }
}
