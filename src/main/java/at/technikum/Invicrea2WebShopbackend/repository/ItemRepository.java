package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing database operations related to items.
 */
@Transactional
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Finds an item by its name.
     *
     * @param name The name of the item to find.
     * @return The found item.
     */
    Item findByName(String name);

    /**
     * Finds items based on their category.
     *
     * @param category The category to search for.
     * @return A list of items with the specified category.
     */
    List<Item> findByCategory(ItemCategory category);
}
