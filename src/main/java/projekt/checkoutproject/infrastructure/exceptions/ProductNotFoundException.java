package projekt.checkoutproject.infrastructure.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String userName) {
        super(String.format("Product not found: %s", userName));
    }
}
