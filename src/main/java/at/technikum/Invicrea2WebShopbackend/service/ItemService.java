package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    public ItemService ( ItemRepository itemRepository, UserRepository userRepository ) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Item saveItem( Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }
    private List<Item> items = new ArrayList<>();

    public Item getItemByName(String name) {
        return itemRepository.findByName(name);
    }

    public ResponseEntity<String> deleteItem(Long id) {
        itemRepository.deleteById(id);
        return ResponseEntity.ok().body("Item removed !! " + id);
    }
    public Item updateItem(Item item) {
        Item existingProduct = itemRepository.findById((item.getId())).orElse(null);
        existingProduct.setName(item.getName());
        existingProduct.setPrice(item.getPrice());
        existingProduct.setDescription(item.getDescription());
        existingProduct.setImageUrl(item.getImageUrl());
        return itemRepository.save(existingProduct);
    }
    public List<Item> getItemsByCategory(ItemCategory category) {
        if (category == ItemCategory.VIEW_ALL) {
            return itemRepository.findAll();
        } else {
            return itemRepository.findByCategory(category);
        }
    }

    public void addItem(Item item, ItemCategory category) {
        item.setCategory(category);

        items.add(item);
    }

    public List<Item> getAllItems() {
        return items;
    }

    public void validateItem(ItemDto item) {
        if (item.getName() == null || item.getName().isEmpty() ||
                item.getDescription() == null || item.getDescription().isEmpty() ||
                item.getImageUrl() == null || item.getImageUrl().isEmpty() ||
                item.getCategory() == null) {
            throw new IllegalArgumentException("Ein oder mehrere Attribute wurden leer gelassen. " +
                    "Bitte füllen Sie alle Felder aus.");
        }
    }

}
