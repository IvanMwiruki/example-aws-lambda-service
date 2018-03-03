package exceptions;

/**
 * Thrown to indicate a resource for Amazon Videos could not be found.
 */
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
