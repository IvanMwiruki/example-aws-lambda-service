package exceptions;

/**
 * Thrown to indicate an Amazon Videos user could not be found.
 */
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
