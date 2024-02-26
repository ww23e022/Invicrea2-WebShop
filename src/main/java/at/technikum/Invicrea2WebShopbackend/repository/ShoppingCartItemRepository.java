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
 * Repository-Schnittstelle für den Zugriff auf Warenkorbposition-Datenbankoperationen.
 */
@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

    /**
     * Sucht eine Warenkorbposition anhand der Warenkorb-ID und der Artikel-ID.
     *
     * @param shoppingCartId Die ID des Warenkorbs.
     * @param itemId         Die ID des Artikels.
     * @return Die Warenkorbposition, falls vorhanden.
     */
    Optional<ShoppingCartItem> findByShoppingCartIdAndItemId( Long shoppingCartId, Long itemId);

    /**
     * Sucht alle Warenkorbpositionen anhand der Warenkorb-ID.
     *
     * @param shoppingCartId Die ID des Warenkorbs.
     * @return Eine Liste von Warenkorbpositionen.
     */
    List<ShoppingCartItem> findAllByShoppingCartId(Long shoppingCartId);

    /**
     * Löscht alle Warenkorbpositionen anhand der Warenkorb-ID.
     *
     * @param shoppingCartId Die ID des Warenkorbs.
     */
    void deleteAllByShoppingCartId(Long shoppingCartId);

    /**
     * Löscht alle Warenkorbpositionen anhand einer Liste von IDs.
     *
     * @param ids Die Liste der IDs der Warenkorbpositionen.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM ShoppingCartItem sci WHERE sci.id IN :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);
}