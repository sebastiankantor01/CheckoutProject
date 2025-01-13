package projekt.checkoutproject.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projekt.checkoutproject.api.dto.CartResponse;
import projekt.checkoutproject.business.CheckoutService;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @GetMapping("/cart")
    public ResponseEntity<CartResponse> getCart(Principal principal) {
        CartResponse cartResponse = checkoutService.getCartDetails(principal.getName());
        return ResponseEntity.ok(cartResponse);
    }

    @PostMapping("/scan/{itemName}")
    public ResponseEntity<String> scanItem(
            Principal principal,
            @PathVariable String itemName) {
        checkoutService.scanItem(principal.getName(), itemName);

        return ResponseEntity.ok("Item scanned: " + itemName);
    }
}
