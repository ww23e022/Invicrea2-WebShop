package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository-Schnittstelle für den Zugriff auf Warenkorb-Datenbankoperationen.
 */
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    /**
     * Sucht einen Warenkorb anhand der Kontonummer.
     *
     * @param accountId Die ID des Kontos.
     * @return Der Warenkorb des Kontos, falls vorhanden.
     */
    Optional<ShoppingCart> findByAccountId( Long accountId);
}