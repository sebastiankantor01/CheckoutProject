package projekt.checkoutproject.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekt.checkoutproject.infrastructure.database.entity.CombinedDiscountEntity;
import projekt.checkoutproject.infrastructure.database.entity.ProductEntity;

import java.util.List;

@Repository
public interface CombinedDiscountRepository extends JpaRepository<CombinedDiscountEntity, Long> {
    @Query("SELECT cd FROM CombinedDiscountEntity cd " +
            "WHERE cd.productX IN :products " +
            "OR cd.productY IN :products")
    List<CombinedDiscountEntity> findDiscountsForProducts(
            @Param("products") List<ProductEntity> products);
}
