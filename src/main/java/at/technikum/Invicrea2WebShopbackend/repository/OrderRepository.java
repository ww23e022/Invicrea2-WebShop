package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Order;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing database operations related to order history objects.
 */
@Repository
public interface OrderRepository extends ListCrudRepository<Order, String> {
}
