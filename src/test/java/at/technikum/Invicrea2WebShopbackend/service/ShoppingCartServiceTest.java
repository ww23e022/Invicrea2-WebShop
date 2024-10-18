package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartRepository;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp () {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    public void testGetItemsInCartByShoppingCartId () {
        // Arrange
        Long userId = 1L;
        ShoppingCart cart = new ShoppingCart( );
        cart.setId( 1L );
        when( shoppingCartRepository.findByUserId( userId ) ).thenReturn( Optional.of( cart ) );

        Item item = new Item( );
        item.setName( "Test Item" );
        item.setPrice( 10 );

        ShoppingCartItem cartItem = new ShoppingCartItem( );
        cartItem.setItem( item );
        cartItem.setQuantity( 2 );
        cartItem.setItemName( item.getName( ) );
        cartItem.setItemPrice( item.getPrice( ) * cartItem.getQuantity( ) );

        List<ShoppingCartItem> cartItems = new ArrayList<>( );
        cartItems.add( cartItem );
        when( shoppingCartItemRepository.findAllByShoppingCartId( cart.getId( ) ) ).thenReturn( cartItems );

        // Act
        List<ShoppingCartItem> result = shoppingCartService.getItemsInCartByShoppingCartId( userId );

        // Assert
        assertEquals( 2, result.get( 0 ).getQuantity( ) );
        assertEquals( "Test Item", result.get( 0 ).getItemName( ) );
        assertEquals( 20, result.get( 0 ).getItemPrice( ) );
    }

    @Test
    public void testAddItemToCart_NewItem () {
        // Arrange
        Long userId = 1L;
        Long itemId = 1L;
        int quantity = 2;

        ShoppingCart cart = new ShoppingCart( );
        cart.setId( 1L );
        when( shoppingCartRepository.findByUserId( userId ) ).thenReturn( Optional.of( cart ) );

        Item item = new Item( );
        item.setId( itemId );
        item.setName( "Test Item" );
        item.setPrice( 10 );
        when( itemRepository.findById( itemId ) ).thenReturn( Optional.of( item ) );

        // Act
        shoppingCartService.addItemToCart( userId, itemId, quantity );

        // Assert
        verify( shoppingCartItemRepository, times( 1 ) ).save( any( ShoppingCartItem.class ) );
    }

    @Test
    public void testUpdateCartItem () {
        // Arrange
        Long userId = 1L;
        Long itemId = 1L;
        int newQuantity = 3;

        ShoppingCart cart = new ShoppingCart( );
        cart.setId( 1L );
        when( shoppingCartRepository.findByUserId( userId ) ).thenReturn( Optional.of( cart ) );

        ShoppingCartItem cartItem = new ShoppingCartItem( );
        cartItem.setQuantity( 1 );
        Item item = new Item( );
        item.setPrice( 10 );
        cartItem.setItem( item );

        List<ShoppingCartItem> existingItems = new ArrayList<>( );
        existingItems.add( cartItem );
        when( shoppingCartItemRepository.findAllByShoppingCartIdAndItemId( cart.getId( ), itemId ) ).thenReturn( existingItems );

        // Act
        shoppingCartService.updateCartItem( userId, itemId, newQuantity );

        // Assert
        assertEquals( 3, cartItem.getQuantity( ) );
        assertEquals( 30, cartItem.getItemPrice( ) );
        verify( shoppingCartItemRepository, times( 1 ) ).save( cartItem );
    }

    @Test
    public void testRemoveItemFromCart () {
        // Arrange
        Long userId = 1L;
        Long itemId = 1L;

        ShoppingCart cart = new ShoppingCart( );
        cart.setId( 1L );
        when( shoppingCartRepository.findByUserId( userId ) ).thenReturn( Optional.of( cart ) );

        ShoppingCartItem cartItem = new ShoppingCartItem( );
        List<ShoppingCartItem> existingItems = new ArrayList<>( );
        existingItems.add( cartItem );
        when( shoppingCartItemRepository.findAllByShoppingCartIdAndItemId( cart.getId( ), itemId ) ).thenReturn( existingItems );

        // Act
        shoppingCartService.removeItemFromCart( userId, itemId );

        // Assert
        verify( shoppingCartItemRepository, times( 1 ) ).delete( cartItem );
    }

    @Test
    public void testClearCart () {
        // Arrange
        Long userId = 1L;
        ShoppingCart cart = new ShoppingCart( );
        cart.setId( 1L );
        when( shoppingCartRepository.findByUserId( userId ) ).thenReturn( Optional.of( cart ) );

        // Act
        shoppingCartService.clearCart( userId );

        // Assert
        verify( shoppingCartItemRepository, times( 1 ) ).deleteAllByShoppingCartId( cart.getId( ) );
    }

    @Test
    public void testCreateShoppingCart () {
        // Arrange
        Long userId = 1L;
        User user = new User( );
        user.setId( userId );
        when( userRepository.findById( userId ) ).thenReturn( Optional.of( user ) );

        ShoppingCart cart = new ShoppingCart( );
        cart.setUser( user );
        when( shoppingCartRepository.save( any( ShoppingCart.class ) ) ).thenReturn( cart );

        // Act
        ShoppingCart result = shoppingCartService.createShoppingCart( userId );

        // Assert
        assertEquals( user, result.getUser( ) );
        verify( shoppingCartRepository, times( 1 ) ).save( any( ShoppingCart.class ) );
    }
}