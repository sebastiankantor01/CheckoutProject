package projekt.checkoutproject.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemPriceDetails {
    private BigDecimal totalPrice;
    private Boolean specialPriceApplied;
}