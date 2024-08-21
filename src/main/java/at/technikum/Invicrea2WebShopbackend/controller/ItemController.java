package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.mapper.ItemMapper;
import at.technikum.Invicrea2WebShopbackend.model.File;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import at.technikum.Invicrea2WebShopbackend.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@CrossOrigin
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addItems(@RequestBody List<ItemDto> itemDtos) {
        try {
            for (ItemDto itemDto : itemDtos) {
                itemService.validateItem(itemDto);
                File file = itemService.getFileById(itemDto.getFileId());
                Item item = itemMapper.toItem(itemDto, file);
                itemService.addItem(item, item.getCategory());
            }
            return "Items added successfully.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Error adding items: " + e.getMessage();
        }
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public List<Item> findAllItems() {
        return itemService.getItems();
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public List<Item> searchItemsByCategoryAndName(@RequestParam String name,
                                                   @RequestParam ItemCategory category) {
        return itemService.searchItemsByNameAndCategory(name, category);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item updateItem(@RequestBody ItemDto itemDto) {
        File file = itemService.getFileById(itemDto.getFileId());
        Item item = itemMapper.toItem(itemDto, file);
        return itemService.updateItem(item);
    }

    @GetMapping(params = "category")
    @PreAuthorize("isAuthenticated()")
    public List<Item> getItemsByCategory(@RequestParam(required = false) ItemCategory category) {
        if (category == null) {
            return itemService.getItems();
        } else {
            return itemService.getItemsByCategory(category);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getItemCount() {
        long itemCount = itemService.getItemCount();
        return ResponseEntity.ok(itemCount);
    }
}
