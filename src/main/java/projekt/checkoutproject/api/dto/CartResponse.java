package projekt.checkoutproject.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartResponse {
    private List<CartItemResponse> items;
    private List<CombinedDiscountResponse> appliedDiscounts;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalPrice;

    public CartResponse(List<CartItemResponse> items,
                        BigDecimal totalValue,
                        List<CombinedDiscountResponse> appliedDiscounts) {
        this.items = items;
        this.appliedDiscounts = appliedDiscounts;

        this.totalDiscountAmount = appliedDiscounts.stream()
                .map(CombinedDiscountResponse::getTotalDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalPrice = totalValue.subtract(this.totalDiscountAmount);
    }
} 