package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import at.technikum.Invicrea2WebShopbackend.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/items")
@CrossOrigin
public class ItemController {

    private final ItemService itemService;

    public ItemController ( ItemService itemService ) {
        this.itemService = itemService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addItems (
            @RequestPart("items") List<ItemDto> itemDtos,
            @RequestPart(value = "file", required = false) MultipartFile fileToUpload ) {
        try {
            itemService.addItems( itemDtos, fileToUpload );
            return ResponseEntity.ok( "Items added successfully." );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest( ).body( e.getMessage( ) );
        } catch (Exception e) {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                    .body( "Error adding items: " + e.getMessage( ) );
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Item> updateItem (
            @RequestPart("item") ItemDto itemDto,
            @RequestPart(value = "file", required = false) MultipartFile fileToUpload ) {
        try {
            Item updatedItem = itemService.updateItem( itemDto, fileToUpload );
            return ResponseEntity.ok( updatedItem );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest( ).body( null );
        } catch (Exception e) {
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( null );
        }
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public List<Item> findAllItems () {
        return itemService.getItems( );
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public List<Item> searchItemsByCategoryAndName ( @RequestParam String name,
                                                     @RequestParam ItemCategory category ) {
        return itemService.searchItemsByNameAndCategory( name, category );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteItem ( @PathVariable Long id ) {
        return itemService.deleteItem( id );
    }

    @GetMapping(params = "category")
    @PreAuthorize("isAuthenticated()")
    public List<Item> getItemsByCategory ( @RequestParam(required = false) ItemCategory category ) {
        if (category == null) {
            return itemService.getItems( );
        } else {
            return itemService.getItemsByCategory( category );
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getItemCount () {
        long itemCount = itemService.getItemCount( );
        return ResponseEntity.ok( itemCount );
    }
}
