package projekt.checkoutproject.infrastructure.exceptions;

public class UnknownUserException extends RuntimeException {
    public UnknownUserException(String userName) {
        super(String.format("Unknown user: %s", userName));
    }
}
