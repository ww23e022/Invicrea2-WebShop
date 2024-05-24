package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing database operations related to shopping carts.
 */
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    /** Sucht einen Warenkorb anhand der Kontonummer. */
    Optional<ShoppingCart> findByAccountId( Long accountId);
}