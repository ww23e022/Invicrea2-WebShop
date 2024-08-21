package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findByCategory(ItemCategory category);

    List<Item> findByNameContainingIgnoreCase(String name);

    List<Item> findByNameContainingIgnoreCaseAndCategory(String name, ItemCategory category);

    Item findByName(String name);
}
