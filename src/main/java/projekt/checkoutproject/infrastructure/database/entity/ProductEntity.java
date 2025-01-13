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
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_hash")
    private String productHash;

    @Column(name = "name")
    private String name;

    @Column(name = "normal_price", nullable = false)
    private BigDecimal normalPrice;

    @Column(name = "special_price_quantity")
    private Integer specialPriceQuantity;

    @Column(name = "special_price")
    private BigDecimal specialPrice;

    public ProductEntity(String name, BigDecimal normalPrice, int specialPriceQuantity, BigDecimal specialPrice) {

    }
}
