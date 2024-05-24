package at.technikum.Invicrea2WebShopbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Exception thrown when an entity is not found. */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs a new EntityNotFoundException with no detail message.
     */
    public EntityNotFoundException() {
    }

    /**
     * Constructs a new EntityNotFoundException with the specified detail message.
     *
     * @param message the detail message.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
