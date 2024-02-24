package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);
    //List<Item> findByOwnerAccountId(Long id);

    List<Item> findByCategory(ItemCategory category);
}
