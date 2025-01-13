package projekt.checkoutproject.infrastructure.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Provided password is invalid. Please try again.");
    }
}
