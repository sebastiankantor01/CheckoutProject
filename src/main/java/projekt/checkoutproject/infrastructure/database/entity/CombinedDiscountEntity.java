package projekt.checkoutproject.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "combined_discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CombinedDiscountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "combined_discount_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_x_id", nullable = false)
    private ProductEntity productX;

    @ManyToOne
    @JoinColumn(name = "product_y_id", nullable = false)
    private ProductEntity productY;

    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;
}
