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



    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping("/addItem")
    public String addItems(@RequestBody List<Item> items) {
        try {
            for (Item item : items) {
                ItemDto newItemDto = itemMapper.toItemDto(item);

                if (newItemDto.getPrice() < 0) {
                    return "Der Preis kann nicht negativ sein.";
                }

                itemService.validateItem(newItemDto);

                Item newItem = itemMapper.toItem(newItemDto);

                itemService.addItem(newItem, newItem.getCategory());
                itemService.saveItem(newItem);
            }
            return "Items wurden erfolgreich hinzugefügt.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Fehler beim Hinzufügen der Items: " + e.getMessage();
        }
    }

    /*@PostMapping("/addItem")
    public String addItem(@RequestBody Item item) {
        try {
            ItemDto newItemDto = itemMapper.toItemDto(item);

            if (newItemDto.getPrice() < 0) {
                return "Der Preis kann nicht negativ sein.";
            }

            itemService.validateItem(newItemDto);

            Item newItem = itemMapper.toItem(newItemDto);

            itemService.addItem(newItem, newItem.getCategory());
            itemService.saveItem(newItem);
            return "Item wurde erfolgreich hinzugefügt.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Fehler beim Hinzufügen des Items: " + e.getMessage();
        }
    }*/



    //@PostMapping("/addItems")
    //public List<Item> addItems(@RequestBody List<Item> products) {
    //    return itemService.saveItems(products);
   // }

    @GetMapping("/items")
    public List<Item> findAllItems() {
        return itemService.getItems();
    }

    @GetMapping("/itemById/{id}")
    public ItemDto findItemById(@PathVariable Long id) {
        Item item = itemService.getItemById(id);

        return itemMapper.toItemDto(item);
    }

    @GetMapping("/item/{name}")
    public Item findItemByName(@PathVariable String name) {
        return itemService.getItemByName(name);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }

    @PutMapping("/update")
    public Item updateItem(@RequestBody Item item) {
        return itemService.updateItem(item);
    }

    @GetMapping("/itemsByCategory")
    public List<Item> getItemsByCategory(@RequestParam(required = false) ItemCategory category) {
        if (category == null) {
            return itemService.getAllItems();
        } else {
            return itemService.getItemsByCategory(category);
        }
    }
    @PostMapping("/addItemToCategory")
    public ResponseEntity<String> addItemWithCategory(@RequestBody Item item,
                                                      @RequestParam ItemCategory category) {
        itemService.addItemWithCategory(item, category);
        return ResponseEntity.ok("Item wurde erfolgreich hinzugefügt.");
    }

    @GetMapping("/accounts/{accountId}/items")
    public List<Item> getItemsByAccountId(@PathVariable Long accountId) {
        return itemService.getItems(accountId);
    }
}
