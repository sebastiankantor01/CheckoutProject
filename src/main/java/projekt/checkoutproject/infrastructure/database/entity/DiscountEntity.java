package projekt.checkoutproject.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discounts")
public class DiscountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;

    @ManyToOne
    @JoinColumn(name = "product_x_id", nullable = false)
    private ProductEntity productX;

    @ManyToOne
    @JoinColumn(name = "product_y_id", nullable = false)
    private ProductEntity productY;

    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount;
}
