package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import at.technikum.Invicrea2WebShopbackend.repository.AccountRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ItemService {

    private final ItemRepository itemRepository;

    private final AccountRepository accountRepository;

    public ItemService ( ItemRepository itemRepository, AccountRepository accountRepository ) {
        this.itemRepository = itemRepository;
        this.accountRepository = accountRepository;
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

        return null;
    }
    public Item updateItem(Item item) {

        return item;
    }
    public List<Item> getItemsByCategory(ItemCategory category) {

        return null;
    }

    public void addItem(Item item, ItemCategory category) {
        item.setCategory(category);

        items.add(item);
    }

    public List<Item> getAllItems() {
        return items;
    }

    public void validateItem(ItemDto item) {

    }

}
