package at.technikum.Invicrea2WebShopbackend.exception;

/** Exception thrown when there are insufficient coins for a transaction. */
public class InsufficientCoinsException extends RuntimeException {

    /**
     * Constructs a new InsufficientCoinsException with the specified detail message.
     @param message the detail message.
     */
    public InsufficientCoinsException(String message) {
        super(message);
    }
}

