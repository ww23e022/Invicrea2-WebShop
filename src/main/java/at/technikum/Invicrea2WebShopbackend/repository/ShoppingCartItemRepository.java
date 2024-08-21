package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

    /**
     * Sucht alle Warenkorb-Inhalte anhand der Warenkorb-ID.
     */
    List<ShoppingCartItem> findAllByShoppingCartId(Long shoppingCartId);

    /**
     * Sucht alle Warenkorb-Inhalte anhand ihrer Warenkorb-ID und Artikel-ID.
     */
    List<ShoppingCartItem> findAllByShoppingCartIdAndItemId(Long shoppingCartId, Long itemId);

    /**
     * Sucht alle Warenkorb-Inhalte anhand der Artikel-ID.
     */
    List<ShoppingCartItem> findAllByItemId(Long itemId);

    /**
     * Löscht alle Warenkorb-Inhalte anhand der Warenkorb-ID.
     */
    @Transactional
    void deleteAllByShoppingCartId(Long shoppingCartId);

    /**
     * Löscht alle Warenkorb-Inhalte anhand einer Liste von IDs.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM ShoppingCartItem sci WHERE sci.id IN :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);
}
