package projekt.checkoutproject.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CombinedDiscountResponse {
    private String productX;
    private String productY;
    private int numberOfPairs;
    private BigDecimal discountPerPair;
    private BigDecimal totalDiscount;
}
