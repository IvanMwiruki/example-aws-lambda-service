package exceptions;

/**
 * Thrown to indicate that a given rating is invalid, e.g. not a number, is
 * a number less than 0, is a number greater than 5, etc.
 */
public class InvalidRatingException extends Exception {

    public InvalidRatingException(String message) {
        super(message);
    }
}
