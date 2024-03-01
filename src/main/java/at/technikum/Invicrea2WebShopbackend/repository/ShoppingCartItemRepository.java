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

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

    /** Sucht alle Warenkorb Inhalt anhand der Konto-ID des Warenkorbs. */
    List<ShoppingCartItem> findAllByShoppingCartAccountId(Long accountId);

    /**
     * Sucht ein Warenkorb Inhalt anhand ihrer Warenkorb-ID und Artikel-ID.
     */
    Optional<ShoppingCartItem> findByShoppingCartIdAndItemId( Long shoppingCartId, Long itemId);


    /**
     * Löscht alle Warenkorb Inhalt anhand der Warenkorb-ID.
     */
    void deleteAllByShoppingCartId(Long shoppingCartId);

    /**
     * Löscht alle Warenkorb Inhalt anhand einer Liste von IDs.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM ShoppingCartItem sci WHERE sci.id IN :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);
}