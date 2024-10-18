package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.mapper.ItemMapper;
import at.technikum.Invicrea2WebShopbackend.model.File;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.repository.FileRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final ItemMapper itemMapper;
    private final ShoppingCartItemRepository shoppingCartItemRepository;

    public ItemService ( ItemRepository itemRepository,
                         FileRepository fileRepository,
                         FileService fileService,
                         ItemMapper itemMapper,
                         ShoppingCartItemRepository shoppingCartItemRepository ) {
        this.itemRepository = itemRepository;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
        this.itemMapper = itemMapper;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
    }

    public void addItems ( List<ItemDto> itemDtos, MultipartFile fileToUpload ) {
        try {
            File file = null;
            if (fileToUpload != null) {
                file = fileService.upload( fileToUpload );
            }
            for (ItemDto itemDto : itemDtos) {
                if (file != null) {
                    UUID fileId = file.getId( );
                    itemDto.setFileId( fileId );
                } else {
                    if (itemDto.getFileId( ) != null) {
                        file = getFileById( itemDto.getFileId( ) );
                    } else {
                        throw new IllegalArgumentException(
                                "Either upload a file or provide an existing fileId."
                        );
                    }
                }
                validateItem( itemDto );
                Item item = itemMapper.toItem( itemDto, file );
                addItem( item, item.getCategory( ) );
            }
        } catch (Exception e) {
            throw new RuntimeException( "Error adding items: " + e.getMessage( ), e );
        }
    }

    public Item updateItem ( ItemDto itemDto, MultipartFile fileToUpload ) {
        try {
            File file = null;
            if (fileToUpload != null) {
                file = fileService.upload( fileToUpload );
                itemDto.setFileId( file.getId( ) );
            } else {
                if (itemDto.getFileId( ) != null) {
                    file = getFileById( itemDto.getFileId( ) );
                } else {
                    throw new IllegalArgumentException(
                            "Either upload a new file or provide an existing fileId."
                    );
                }
            }

            Item item = itemMapper.toItem( itemDto, file );
            Item updatedItem = updateItemInRepository( item );

            return updatedItem;
        } catch (Exception e) {
            throw new RuntimeException( "Error updating item: " + e.getMessage( ), e );
        }
    }

    private Item updateItemInRepository ( Item item ) {
        Item existingItem = itemRepository.findById( item.getId( ) )
                .orElseThrow( () -> new RuntimeException( "Item not found" ) );
        existingItem.setName( item.getName( ) );
        existingItem.setPrice( item.getPrice( ) );
        existingItem.setDescription( item.getDescription( ) );
        existingItem.setFile( item.getFile( ) );
        existingItem.setCategory( item.getCategory( ) );

        Item updatedItem = itemRepository.save( existingItem );

        // Update shopping cart items containing this item
        updateShoppingCartItems( updatedItem );

        return updatedItem;
    }

    public Item saveItem ( Item item ) {
        return itemRepository.save( item );
    }

    public List<Item> getItems () {
        return StreamSupport.stream( itemRepository.findAll( ).spliterator( ), false )
                .collect( Collectors.toList( ) );
    }

    public List<Item> searchItemsByNameAndCategory ( String name, ItemCategory category ) {
        if (category == ItemCategory.VIEW_ALL) {
            return itemRepository.findByNameContainingIgnoreCase( name );
        } else {
            return itemRepository.findByNameContainingIgnoreCaseAndCategory( name, category );
        }
    }

    public ResponseEntity<String> deleteItem ( Long id ) {
        // Entferne alle Warenkorb-Items, die den zu löschenden Artikel enthalten
        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findAllByItemId( id );
        if (!cartItems.isEmpty( )) {
            shoppingCartItemRepository.deleteAllByIdIn( cartItems.stream( )
                    .map( ShoppingCartItem::getId )
                    .collect( Collectors.toList( ) ) );
        }

        // Lösche das Item aus der Datenbank
        itemRepository.deleteById( id );
        return ResponseEntity.ok( ).body( "Item removed !! " + id );
    }

    private void updateShoppingCartItems ( Item updatedItem ) {
        List<ShoppingCartItem> cartItems = shoppingCartItemRepository
                .findAllByItemId( updatedItem.getId( ) );
        for (ShoppingCartItem cartItem : cartItems) {
            cartItem.setItemName( updatedItem.getName( ) );
            cartItem.setItemPrice( updatedItem.getPrice( ) * cartItem.getQuantity( ) );
            shoppingCartItemRepository.save( cartItem );
        }
    }

    public List<Item> getItemsByCategory ( ItemCategory category ) {
        if (category == ItemCategory.VIEW_ALL) {
            return StreamSupport.stream( itemRepository.findAll( ).spliterator( ), false )
                    .collect( Collectors.toList( ) );
        } else {
            return itemRepository.findByCategory( category );
        }
    }

    public void addItem ( Item item, ItemCategory category ) {
        item.setCategory( category );
        itemRepository.save( item );
    }

    public void validateItem ( ItemDto itemDto ) {
        if (itemDto.getName( ) == null || itemDto.getName( ).isEmpty( ) ||
                itemDto.getDescription( ) == null || itemDto.getDescription( ).isEmpty( ) ||
                itemDto.getFileId( ) == null ||
                itemDto.getCategory( ) == null) {
            throw new IllegalArgumentException(
                    "Ein oder mehrere Attribute wurden leer gelassen. " +
                            "Bitte füllen Sie alle Felder aus." );
        }
    }

    public File getFileById ( UUID fileId ) {
        return fileRepository.findById( fileId )
                .orElseThrow( () -> new RuntimeException( "File not found" ) );
    }

    public long getItemCount () {
        return itemRepository.count( );
    }
}
