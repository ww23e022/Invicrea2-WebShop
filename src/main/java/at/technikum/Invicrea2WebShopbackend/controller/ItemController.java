package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.mapper.ItemMapper;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.CoinsService;
import at.technikum.Invicrea2WebShopbackend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/items")
@CrossOrigin
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Autowired
    private AccountService accountService;
    @Autowired
    private CoinsService coinsService;

    // Constructor for injecting ItemService and ItemMapper
    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    // Handler for POST requests on "/items/addItem" adds items
    @PostMapping("/addItem")
    public String addItems(@RequestBody List<Item> items) {
        try {
            // Loop through all the passed items
            for (Item item : items) {
                // Convert the item to a DTO
                ItemDto newItemDto = itemMapper.toItemDto(item);

                // Check if the price is negative
                if (newItemDto.getPrice() < 0) {
                    return "Price cannot be negative.";
                }

                // Validate the item
                itemService.validateItem(newItemDto);

                // Convert the DTO back to an item
                Item newItem = itemMapper.toItem(newItemDto);

                // Add and save the item
                itemService.addItem(newItem, newItem.getCategory());
                itemService.saveItem(newItem);
            }
            return "Items added successfully.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Error adding items: " + e.getMessage();
        }
    }

    // Handler for GET requests on "/items/items" returns all items
    @GetMapping("/items")
    public List<Item> findAllItems() {
        return itemService.getItems();
    }

    // Handler for GET requests on "/items/itemById/{id}" returns an item by ID
    @GetMapping("/itemById/{id}")
    public ItemDto findItemById(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        return itemMapper.toItemDto(item);
    }

    // Handler for GET requests on "/items/item/{name}" returns an item by name
    @GetMapping("/item/{name}")
    public Item findItemByName(@PathVariable String name) {
        return itemService.getItemByName(name);
    }

    // Handler for DELETE requests on "/items/delete/{id}" deletes an item
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }

    // Handler for PUT requests on "/items/update" updates an item
    @PutMapping("/update")
    public Item updateItem(@RequestBody Item item) {
        return itemService.updateItem(item);
    }

    // Handler for GET requests on "/items/itemsByCategory" returns items by category
    @GetMapping("/itemsByCategory")
    public List<Item> getItemsByCategory(@RequestParam(required = false) ItemCategory category) {
        if (category == null) {
            return itemService.getAllItems();
        } else {
            return itemService.getItemsByCategory(category);
        }
    }

    // Handler for POST requests on "/items/addItemToCategory" adds an item with category
    @PostMapping("/addItemToCategory")
    public ResponseEntity<String> addItemWithCategory(@RequestBody Item item,
                                                      @RequestParam ItemCategory category) {
        itemService.addItemWithCategory(item, category);
        return ResponseEntity.ok("Item added successfully.");
    }

    // Handler for GET requests on "/items/accounts/{accountId}/items"
    // returns items for an account
    @GetMapping("/accounts/{accountId}/items")
    public List<Item> getItemsByAccountId(@PathVariable Long accountId) {
        return itemService.getItems(accountId);
    }
}