package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing database operations related to shopping cart items.
 */
@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

    List<ShoppingCartItem> findAllByShoppingCartAccountId(Long accountId);

    /**
     * Finds a shopping cart item by its shopping cart ID and item ID.
     *
     * @param shoppingCartId The ID of the shopping cart.
     * @param itemId         The ID of the item.
     * @return The shopping cart item, if found.
     */
    Optional<ShoppingCartItem> findByShoppingCartIdAndItemId( Long shoppingCartId, Long itemId);


    /**
     * Deletes all shopping cart items by shopping cart ID.
     *
     * @param shoppingCartId The ID of the shopping cart.
     */
    void deleteAllByShoppingCartId(Long shoppingCartId);

    /**
     * Deletes all shopping cart items by a list of IDs.
     *
     * @param ids The list of IDs of shopping cart items.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM ShoppingCartItem sci WHERE sci.id IN :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);
}