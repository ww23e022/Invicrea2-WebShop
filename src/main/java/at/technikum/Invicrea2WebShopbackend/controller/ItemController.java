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


    // Konstruktor für das Injizieren von ItemService and ItemMapper
    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    // Diese Methode verarbeitet POST-Anfragen auf dem Endpunkt "/items/addItems".
    // Sie fügt Items hinzu.
    @PostMapping("/addItems")
    public String addItems(@RequestBody List<Item> items) {
        try {
            for (Item item : items) {
                // Der Item wird in ein DTO (Data Transfer Object) konvertiert.
                ItemDto newItemDto = itemMapper.toItemDto(item);
                // Es wird überprüft, ob der Preis des Items negativ ist.
                if (newItemDto.getPrice() < 0) {
                    return "Price cannot be negative.";
                }

                // Die Gültigkeit des Artikels wird überprüft.
                itemService.validateItem(newItemDto);

                // Das DTO wird zurück in ein Item-Objekt umgewandelt.
                Item newItem = itemMapper.toItem(newItemDto);

                // Der Item wird dem Service hinzugefügt und gespeichert.
                itemService.addItem(newItem, newItem.getCategory());
                itemService.saveItem(newItem);
            }
            //Wenn alle Items erfolgreich hinzugefügt wurden,
            // wird "Items added successfully." zurückgegeben.
            return "Items added successfully.";
        } catch (IllegalArgumentException e) {
            // Andernfalls wird eine Fehlermeldung zurückgegeben.
            return e.getMessage();
        } catch (Exception e) {
            return "Error adding items: " + e.getMessage();
        }
    }

    // Diese Methode verarbeitet GET-Anfragen auf dem Endpunkt "/items/items".
    // Sie ruft alle Items aus dem ItemService ab und gibt sie zurück.
    @GetMapping("/items")
    public List<Item> findAllItems() {
        return itemService.getItems();
    }

    // Diese Methode verarbeitet GET-Anfragen auf dem Endpunkt "/items/item/{name}".
    // Sie ruft den Item mit dem angegebenen Namen aus dem ItemService ab
    // und gibt ihn zurück.
    @GetMapping("/item/{name}")
    public Item findItemByName(@PathVariable String name) {
        return itemService.getItemByName(name);
    }

    // Diese Methode verarbeitet DELETE-Anfragen auf dem Endpunkt "/items/delete/{id}".
    // Sie löscht den Item mit der angegebenen ID mithilfe des ItemService.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }

    // Diese Methode verarbeitet PUT-Anfragen auf dem Endpunkt "/items/update".
    // Sie aktualisiert den übergebenen Item mithilfe des ItemService und gibt ihn zurück.
    @PutMapping("/update")
    public Item updateItem(@RequestBody Item item) {
        return itemService.updateItem(item);
    }

    // Diese Methode verarbeitet GET-Anfragen auf dem Endpunkt "/items/itemsByCategory".
    // Sie gibt entweder alle Items zurück, wenn keine Kategorie angegeben ist,
    // oder gibt alle Items einer bestimmten Kategorie zurück.
    @GetMapping("/itemsByCategory")
    public List<Item> getItemsByCategory(@RequestParam(required = false) ItemCategory category) {
        if (category == null) {
            return itemService.getAllItems();
        } else {
            return itemService.getItemsByCategory(category);
        }
    }
}