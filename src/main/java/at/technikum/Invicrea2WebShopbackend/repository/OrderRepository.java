package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository-Schnittstelle für den Zugriff auf
 * Datenbankoperationen im Zusammenhang mit Bestellungen
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
