package projekt.checkoutproject.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.checkoutproject.infrastructure.database.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}