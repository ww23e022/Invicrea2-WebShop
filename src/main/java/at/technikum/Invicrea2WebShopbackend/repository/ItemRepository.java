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
    // Sie findet Item mit Namen.
    Item findByName(String name);

    // Sie findet Items durch Kategorie.
    List<Item> findByCategory(ItemCategory category);
}