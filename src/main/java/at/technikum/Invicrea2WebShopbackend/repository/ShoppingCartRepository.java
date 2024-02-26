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

    /**
     * Finds a shopping cart by account ID.
     *
     * @param accountId The ID of the account.
     * @return The shopping cart of the account, if found.
     */
    Optional<ShoppingCart> findByAccountId( Long accountId);
}