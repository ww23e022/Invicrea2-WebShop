package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing database operations related to order history objects.
 */
@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}

