package projekt.checkoutproject.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.checkoutproject.infrastructure.database.entity.ProductEntity;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);
}
