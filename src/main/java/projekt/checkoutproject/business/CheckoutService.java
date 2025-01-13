package projekt.checkoutproject.business;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projekt.checkoutproject.api.dto.CartItemResponse;
import projekt.checkoutproject.api.dto.CartResponse;
import projekt.checkoutproject.api.dto.CombinedDiscountResponse;
import projekt.checkoutproject.api.dto.ItemPriceDetails;
import projekt.checkoutproject.infrastructure.database.entity.*;
import projekt.checkoutproject.infrastructure.database.repository.CartRepository;
import projekt.checkoutproject.infrastructure.database.repository.CombinedDiscountRepository;
import projekt.checkoutproject.infrastructure.database.repository.ProductRepository;
import projekt.checkoutproject.infrastructure.database.repository.UserRepository;
import projekt.checkoutproject.infrastructure.exceptions.ProductNotFoundException;
import projekt.checkoutproject.infrastructure.exceptions.UserNotFoundException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CombinedDiscountRepository combinedDiscountRepository;

    public void scanItem(String email, String itemName) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        ProductEntity product = productRepository.findByName(itemName)
                .orElseThrow(() -> new ProductNotFoundException(itemName));

        CartEntity cart = checkCartAvailability(user);

        Optional<CartItemEntity> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getName().equals(product.getName()))
                .findFirst();

        updateItemQuantity(existingCartItem, cart, product);

        cartRepository.save(cart);
    }

    private static void updateItemQuantity(Optional<CartItemEntity> existingCartItem, CartEntity cart, ProductEntity product) {
        if (existingCartItem.isPresent()) {
            CartItemEntity cartProduct = existingCartItem.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + 1);
        } else {
            CartItemEntity cartItem = new CartItemEntity(cart, product);
            cart.getItems().add(cartItem);
        }
    }

    private static CartEntity checkCartAvailability(UserEntity user) {
        CartEntity cart = user.getCart();

        if (Objects.isNull(cart)) {
            cart = new CartEntity(user);
        }

        return cart;
    }

    public CartResponse getCartDetails(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        CartEntity cart = user.getCart();

        if (Objects.isNull(cart)) {
            return new CartResponse();
        }

        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> new CartItemResponse(item.getProduct(), item.getQuantity(), calculateTotalItemPrice(item)))
                .collect(Collectors.toList());

        BigDecimal totalPrice = calculateTotalPrice(cart);
        List<CombinedDiscountResponse> appliedDiscounts = findAppliedDiscounts(cart);

        return new CartResponse(items, totalPrice, appliedDiscounts);
    }

    private ItemPriceDetails calculateTotalItemPrice(CartItemEntity item) {
        ProductEntity product = item.getProduct();
        int quantity = item.getQuantity();
        BigDecimal totalPriceForItem;
        boolean hasSpecialPrice = false;

        if (product.getSpecialPriceQuantity() != null && quantity >= product.getSpecialPriceQuantity()) {
            totalPriceForItem = product.getSpecialPrice().multiply(BigDecimal.valueOf(quantity));
            hasSpecialPrice = true;
        } else {
            totalPriceForItem = product.getNormalPrice().multiply(BigDecimal.valueOf(quantity));
        }

        return new ItemPriceDetails(totalPriceForItem, hasSpecialPrice);
    }

    public BigDecimal calculateTotalPrice(CartEntity cart) {
        BigDecimal totalValue = BigDecimal.ZERO;

        for (CartItemEntity item : cart.getItems()) {
            ProductEntity product = item.getProduct();
            int quantity = item.getQuantity();

            if (product.getSpecialPriceQuantity() != null && quantity >= product.getSpecialPriceQuantity()) {
                totalValue = totalValue.add(product.getSpecialPrice().multiply(BigDecimal.valueOf(quantity)));
            } else {
                totalValue = totalValue.add(product.getNormalPrice().multiply(BigDecimal.valueOf(quantity)));
            }
        }

        return totalValue;
    }

    private List<CombinedDiscountResponse> findAppliedDiscounts(CartEntity cart) {
        List<CombinedDiscountEntity> availableDiscounts = combinedDiscountRepository
                .findDiscountsForProducts(cart.getItems().stream()
                        .map(CartItemEntity::getProduct)
                        .collect(Collectors.toList()));

        Map<ProductEntity, Integer> remainingQuantities = cart.getItems().stream()
                .collect(Collectors.toMap(
                        CartItemEntity::getProduct,
                        CartItemEntity::getQuantity
                ));

        availableDiscounts.sort((d1, d2) -> d2.getDiscountAmount().compareTo(d1.getDiscountAmount()));
        List<CombinedDiscountResponse> appliedDiscounts = new ArrayList<>();

        for (CombinedDiscountEntity discount : availableDiscounts) {
            ProductEntity productX = discount.getProductX();
            ProductEntity productY = discount.getProductY();

            int quantityX = remainingQuantities.getOrDefault(productX, 0);
            int quantityY = remainingQuantities.getOrDefault(productY, 0);

            int pairs = Math.min(quantityX, quantityY);

            if (pairs > 0) {
                remainingQuantities.put(productX, quantityX - pairs);
                remainingQuantities.put(productY, quantityY - pairs);

                BigDecimal totalDiscount = discount.getDiscountAmount()
                        .multiply(BigDecimal.valueOf(pairs));

                appliedDiscounts.add(new CombinedDiscountResponse(
                        productX.getName(),
                        productY.getName(),
                        pairs,
                        discount.getDiscountAmount(),
                        totalDiscount
                ));
            }
        }

        return appliedDiscounts;
    }
}
