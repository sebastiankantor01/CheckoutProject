package projekt.checkoutproject.infrastructure.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userName) {
        super(String.format("User not found: %s", userName));
    }
}
