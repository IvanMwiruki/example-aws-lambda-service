package user;

import exceptions.UserNotFoundException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * Manages users for Amazon Videos.
 */
public class UserService {

    private final UserDAO userDAO;

    /* Cached user to avoid multiple database calls to validate user */
    private User userCache;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Fetch an existing user by their userId.
     *
     * @param userId the userId of the user to retrieve
     * @return an Optional of the User with the specified userId
     */
    public Optional<User> fetch(String userId) {
        if (StringUtils.isBlank(userId)) {
            final String message = String.format("Cannot look up user by invalid userId value. {userId: %s}", userId);
            throw new IllegalArgumentException(message);
        }
        return userDAO.fetch(userId);
    }

    /**
     * Returns the user with the given userId.
     *
     * @param userId the userId of the user to verify
     * @return an object representing the user
     * @throws UserNotFoundException if the user could not be found
     */
    public User getUser(String userId) throws UserNotFoundException {
        if (userCache != null && userId.equals(userCache.getLogin())) {
            return userCache;
        }
        else {
            final Optional<User> user = fetch(userId);
            user.ifPresent(this::updateUserCache);
            return user.orElseThrow(() -> new UserNotFoundException(
                    String.format("The specified user: {%s} could not be found.", userId)));
        }
    }

    private void updateUserCache(User newUser) {
        userCache = newUser;
    }
}
