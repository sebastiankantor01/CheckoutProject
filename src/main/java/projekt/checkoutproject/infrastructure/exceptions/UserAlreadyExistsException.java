package projekt.checkoutproject.infrastructure.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String userName) {
        super("User already exists: " + userName);
    }
}
