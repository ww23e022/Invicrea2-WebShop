package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.mapper.ItemMapper;
import at.technikum.Invicrea2WebShopbackend.model.File;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ItemCategory;
import at.technikum.Invicrea2WebShopbackend.repository.FileRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileService fileService;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setUp () {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    void testAddItems_withUploadedFile () {
        // Arrange
        File file = new File( );
        file.setId( UUID.randomUUID( ) );
        when( fileService.upload( multipartFile ) ).thenReturn( file );
        when( itemMapper.toItem( any( ItemDto.class ), any( File.class ) ) ).thenReturn( new Item( ) );
        when( itemRepository.save( any( Item.class ) ) ).thenReturn( new Item( ) );

        ItemDto itemDto = new ItemDto( );
        itemDto.setName( "Test Item" );
        itemDto.setDescription( "Description" );
        itemDto.setCategory( ItemCategory.VIEW_ALL );

        // Act
        itemService.addItems( List.of( itemDto ), multipartFile );

        // Assert
        verify( fileService ).upload( multipartFile );
        verify( itemRepository ).save( any( Item.class ) );
    }

    @Test
    void testUpdateItem_withExistingFile () {
        // Arrange
        UUID fileId = UUID.randomUUID( );
        File file = new File( );
        file.setId( fileId );

        ItemDto itemDto = new ItemDto( );
        itemDto.setFileId( fileId );
        itemDto.setName( "Updated Name" );

        Item existingItem = new Item( ); // Simuliere ein existierendes Item
        existingItem.setId( 1L );
        existingItem.setName( "Old Name" );

        when( fileRepository.findById( fileId ) ).thenReturn( Optional.of( file ) );
        when( itemMapper.toItem( any( ItemDto.class ), any( File.class ) ) ).thenReturn( existingItem );
        when( itemRepository.findById( 1L ) ).thenReturn( Optional.of( existingItem ) ); // Korrektur hier
        when( itemRepository.save( any( Item.class ) ) ).thenReturn( existingItem );

        // Act
        Item updatedItem = itemService.updateItem( itemDto, null );

        // Assert
        assertNotNull( updatedItem );
        verify( itemRepository ).save( any( Item.class ) );
    }

    @Test
    void testDeleteItem () {
        // Arrange
        Long itemId = 1L;
        when( shoppingCartItemRepository.findAllByItemId( itemId ) ).thenReturn( List.of( ) );

        // Act
        ResponseEntity<String> response = itemService.deleteItem( itemId );

        // Assert
        assertEquals( "Item removed !! 1", response.getBody( ) );
        verify( itemRepository ).deleteById( itemId );
    }

    @Test
    void testGetItemsByCategory_viewAll () {
        // Arrange
        when( itemRepository.findAll( ) ).thenReturn( List.of( new Item( ), new Item( ) ) );

        // Act
        List<Item> items = itemService.getItemsByCategory( ItemCategory.VIEW_ALL );

        // Assert
        assertEquals( 2, items.size( ) );
        verify( itemRepository ).findAll( );
    }

    @Test
    void testSearchItemsByNameAndCategory_specificCategory () {
        // Arrange
        String searchName = "Blaustahlpanzer";
        ItemCategory category = ItemCategory.RÜSTUNG;
        Item item = new Item( );
        item.setName( "Item 1" );

        when( itemRepository.findByNameContainingIgnoreCaseAndCategory( anyString( ), any( ItemCategory.class ) ) )
                .thenReturn( List.of( item ) );

        // Act
        List<Item> items = itemService.searchItemsByNameAndCategory( searchName, category );

        // Assert
        assertEquals( 1, items.size( ) );
        verify( itemRepository ).findByNameContainingIgnoreCaseAndCategory( anyString( ), any( ItemCategory.class ) );
    }

    @Test
    void testAddItem () {
        // Arrange
        Item item = new Item( );
        item.setCategory( ItemCategory.VIEW_ALL );

        // Act
        itemService.addItem( item, ItemCategory.VIEW_ALL );

        // Assert
        assertEquals( ItemCategory.VIEW_ALL, item.getCategory( ) );
        verify( itemRepository ).save( item );
    }
}