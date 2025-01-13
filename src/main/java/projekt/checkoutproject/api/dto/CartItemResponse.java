package projekt.checkoutproject.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import projekt.checkoutproject.infrastructure.database.entity.ProductEntity;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CartItemResponse {
    private String productName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal specialPrice;
    private Integer specialPriceQuantity;
    private Boolean specialPriceApplied;
    private BigDecimal totalPriceForItem;

    public CartItemResponse(ProductEntity product, Integer quantity, ItemPriceDetails priceDetails) {
        this.productName = product.getName();
        this.quantity = quantity;
        this.price = product.getNormalPrice();
        this.specialPrice = product.getSpecialPrice();
        this.specialPriceQuantity = product.getSpecialPriceQuantity();
        this.specialPriceApplied = priceDetails.getSpecialPriceApplied();
        this.totalPriceForItem = priceDetails.getTotalPrice();
    }
}
