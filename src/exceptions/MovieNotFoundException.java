package exceptions;

/**
 * Thrown to indicate a movie could not be found.
 */
public class MovieNotFoundException extends ResourceNotFoundException {

    public MovieNotFoundException(String message) {
        super(message);
    }
}
