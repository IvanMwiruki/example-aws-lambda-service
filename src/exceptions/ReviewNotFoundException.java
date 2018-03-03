package exceptions;

/**
 * Thrown to indicate an Amazon Videos review could not be found.
 */
public class ReviewNotFoundException extends ResourceNotFoundException {

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
