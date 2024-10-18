package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.exception.EntityNotFoundException;
import at.technikum.Invicrea2WebShopbackend.exception.InsufficientCoinsException;
import at.technikum.Invicrea2WebShopbackend.model.*;
import at.technikum.Invicrea2WebShopbackend.repository.OrderHistoryRepository;
import at.technikum.Invicrea2WebShopbackend.repository.OrderRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp () {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    void testGetTotalQuantitySold () {
        OrderHistory orderHistory1 = new OrderHistory( );
        orderHistory1.setQuantity( 5 );
        OrderHistory orderHistory2 = new OrderHistory( );
        orderHistory2.setQuantity( 10 );

        when( orderHistoryRepository.findAll( ) ).thenReturn( Arrays.asList( orderHistory1, orderHistory2 ) );

        long totalQuantitySold = orderService.getTotalQuantitySold( );

        assertEquals( 15, totalQuantitySold );
        verify( orderHistoryRepository, times( 1 ) ).findAll( );
    }

    @Test
    void testFindAll () {
        List<Order> orders = Arrays.asList( new Order( ), new Order( ) );
        when( orderRepository.findAll( ) ).thenReturn( orders );

        List<Order> result = orderService.findAll( );

        assertEquals( 2, result.size( ) );
        verify( orderRepository, times( 1 ) ).findAll( );
    }

    @Test
    void testFind_OrderFound () {
        Order order = new Order( );
        when( orderRepository.findById( "1" ) ).thenReturn( Optional.of( order ) );

        Order result = orderService.find( "1" );

        assertEquals( order, result );
        verify( orderRepository, times( 1 ) ).findById( "1" );
    }

    @Test
    void testFind_OrderNotFound () {
        when( orderRepository.findById( "1" ) ).thenReturn( Optional.empty( ) );

        assertThrows( EntityNotFoundException.class, () -> orderService.find( "1" ) );
        verify( orderRepository, times( 1 ) ).findById( "1" );
    }

    @Test
    void testSave () {
        Order order = new Order( );
        when( orderRepository.save( any( Order.class ) ) ).thenReturn( order );

        Order result = orderService.save( order );

        assertEquals( order, result );
        verify( orderRepository, times( 1 ) ).save( order );
    }

    @Test
    void testDelete () {
        orderService.delete( "1" );

        verify( orderRepository, times( 1 ) ).deleteById( "1" );
    }

    @Test
    void testGetOrderHistoryByUserId_UserFound () {
        User user = new User( );
        user.setOrder( Arrays.asList( new Order( ), new Order( ) ) );
        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );

        List<Order> orders = orderService.getOrderHistoryByUserId( 1L );

        assertEquals( 2, orders.size( ) );
        verify( userRepository, times( 1 ) ).findById( 1L );
    }

    @Test
    void testGetOrderHistoryByUserId_UserNotFound () {
        when( userRepository.findById( 1L ) ).thenReturn( Optional.empty( ) );

        assertThrows( EntityNotFoundException.class, () -> orderService.getOrderHistoryByUserId( 1L ) );
        verify( userRepository, times( 1 ) ).findById( 1L );
    }

    @Test
    void testCreateOrder_Success () {
        User user = new User( );
        user.setId( 1L );
        user.setCoins( 1000 );

        Item item = new Item( );
        item.setPrice( 100 );

        ShoppingCartItem cartItem = new ShoppingCartItem( );
        cartItem.setItem( item );
        cartItem.setQuantity( 2 );

        ShoppingCart shoppingCart = new ShoppingCart( );
        shoppingCart.setCartItems( Arrays.asList( cartItem ) );
        user.setShoppingCart( shoppingCart );

        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );
        when( orderRepository.save( any( Order.class ) ) ).thenAnswer( invocation -> invocation.getArgument( 0 ) );
        doNothing( ).when( shoppingCartItemRepository ).deleteAllByIdIn( anyList( ) );

        Order order = orderService.createOrder( 1L );

        assertNotNull( order );
        assertEquals( 800, user.getCoins( ) );
        verify( userRepository, times( 1 ) ).save( user );
        verify( orderRepository, times( 1 ) ).save( any( Order.class ) );
        verify( shoppingCartItemRepository, times( 1 ) ).deleteAllByIdIn( anyList( ) );
        verify( orderHistoryRepository, times( 1 ) ).save( any( OrderHistory.class ) );
    }

    @Test
    void testCreateOrder_UserNotFound () {
        when( userRepository.findById( 1L ) ).thenReturn( Optional.empty( ) );

        assertThrows( EntityNotFoundException.class, () -> orderService.createOrder( 1L ) );
        verify( userRepository, times( 1 ) ).findById( 1L );
    }

    @Test
    void testCreateOrder_InsufficientCoins () {
        User user = new User( );
        user.setId( 1L );
        user.setCoins( 50 );

        Item item = new Item( );
        item.setPrice( 100 );

        ShoppingCartItem cartItem = new ShoppingCartItem( );
        cartItem.setItem( item );
        cartItem.setQuantity( 1 );

        ShoppingCart shoppingCart = new ShoppingCart( );
        shoppingCart.setCartItems( Arrays.asList( cartItem ) );
        user.setShoppingCart( shoppingCart );

        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );

        assertThrows( InsufficientCoinsException.class, () -> orderService.createOrder( 1L ) );
        verify( userRepository, times( 1 ) ).findById( 1L );
        verify( userRepository, never( ) ).save( user );
        verify( orderRepository, never( ) ).save( any( Order.class ) );
    }

    @Test
    void testProcessOrderCancellation_Approve () {
        Order order = new Order( );
        order.setStatus( OrderStatus.CANCEL_REQUESTED );
        order.setTotalPrice( 200 );
        User user = new User( );
        user.setCoins( 800 );
        order.setUser( user );

        when( orderRepository.findById( "1" ) ).thenReturn( Optional.of( order ) );
        when( userRepository.save( user ) ).thenReturn( user );

        orderService.processOrderCancellation( "1", true );

        assertEquals( OrderStatus.CANCELLED, order.getStatus( ) );
        assertEquals( 1000, user.getCoins( ) );
        verify( orderRepository, times( 1 ) ).save( order );
        verify( userRepository, times( 1 ) ).save( user );
    }

    @Test
    void testProcessOrderCancellation_Reject () {
        Order order = new Order( );
        order.setStatus( OrderStatus.CANCEL_REQUESTED );
        order.setTotalPrice( 200 );
        User user = new User( );
        user.setCoins( 800 );
        order.setUser( user );

        when( orderRepository.findById( "1" ) ).thenReturn( Optional.of( order ) );

        orderService.processOrderCancellation( "1", false );

        assertEquals( OrderStatus.REJECTED, order.getStatus( ) );
        assertEquals( 800, user.getCoins( ) );
        verify( orderRepository, times( 1 ) ).save( order );
        verify( userRepository, never( ) ).save( user );
    }

    @Test
    void testProcessOrderCancellation_InvalidStatus () {
        Order order = new Order( );
        order.setStatus( OrderStatus.PROCESSED );

        when( orderRepository.findById( "1" ) ).thenReturn( Optional.of( order ) );

        assertThrows( IllegalArgumentException.class, () -> orderService.processOrderCancellation( "1", true ) );
        verify( orderRepository, never( ) ).save( order );
    }

    @Test
    void testRequestOrderCancellation_Success () {
        Order order = new Order( );
        order.setId( 1L );
        order.setOrderDate( LocalDateTime.now( ).minusDays( 5 ) );
        order.setStatus( OrderStatus.BOUGHT );
        User user = new User( );
        user.setId( 1L );
        order.setUser( user );

        when( orderRepository.findById( "1" ) ).thenReturn( Optional.of( order ) );

        orderService.requestOrderCancellation( "1", 1L );

        assertEquals( OrderStatus.CANCEL_REQUESTED, order.getStatus( ) );
        verify( orderRepository, times( 1 ) ).save( order );
    }

    @Test
    void testRequestOrderCancellation_OrderNotFound () {
        when( orderRepository.findById( "1" ) ).thenReturn( Optional.empty( ) );

        assertThrows( EntityNotFoundException.class, () -> orderService.requestOrderCancellation( "1", 1L ) );
        verify( orderRepository, times( 1 ) ).findById( "1" );
    }

    @Test
    void testRequestOrderCancellation_UnauthorizedUser () {
        Order order = new Order( );
        order.setUser( new User( ) );
        order.getUser( ).setId( 2L );

        when( orderRepository.findById( "1" ) ).thenReturn( Optional.of( order ) );

        assertThrows( IllegalArgumentException.class, () -> orderService.requestOrderCancellation( "1", 1L ) );
    }

    @Test
    void testRequestOrderCancellation_OrderTooOld () {
        Order order = new Order( );
        order.setOrderDate( LocalDateTime.now( ).minusDays( 15 ) );
        order.setUser( new User( ) );
        order.getUser( ).setId( 1L );
        order.setStatus( OrderStatus.BOUGHT );

        when( orderRepository.findById( "1" ) ).thenReturn( Optional.of( order ) );

        assertThrows( IllegalArgumentException.class, () -> orderService.requestOrderCancellation( "1", 1L ) );
    }

    @Test
    void testGetOrderDetailsByUserIdAndOrderId_Success () {
        User user = new User( );
        user.setId( 1L );

        Order order = new Order( );
        order.setId( 1L );
        order.setUser( user );

        OrderHistory orderHistory = new OrderHistory( );
        orderHistory.setOrder( order );

        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );
        when( orderRepository.findById( "1" ) ).thenReturn( Optional.of( order ) );
        when( orderHistoryRepository.findByOrder( order ) ).thenReturn( Arrays.asList( orderHistory ) );

        List<OrderHistory> result = orderService.getOrderDetailsByUserIdAndOrderId( 1L, "1" );

        assertEquals( 1, result.size( ) );
        verify( userRepository, times( 1 ) ).findById( 1L );
        verify( orderRepository, times( 1 ) ).findById( "1" );
        verify( orderHistoryRepository, times( 1 ) ).findByOrder( order );
    }

    @Test
    void testGetOrderDetailsByUserIdAndOrderId_UnauthorizedAccess () {
        User user = new User( );
        user.setId( 1L );

        User anotherUser = new User( );
        anotherUser.setId( 2L );

        Order order = new Order( );
        order.setId( 1L );
        order.setUser( anotherUser );

        when( userRepository.findById( 1L ) ).thenReturn( Optional.of( user ) );
        when( orderRepository.findById( "1" ) ).thenReturn( Optional.of( order ) );

        assertThrows( EntityNotFoundException.class, () -> orderService.getOrderDetailsByUserIdAndOrderId( 1L, "1" ) );
    }

    @Test
    void testGetAllOrderDetails () {
        List<OrderHistory> orderHistories = Arrays.asList( new OrderHistory( ), new OrderHistory( ) );
        when( orderHistoryRepository.findAll( ) ).thenReturn( orderHistories );

        List<OrderHistory> result = orderService.getAllOrderDetails( );

        assertEquals( 2, result.size( ) );
        verify( orderHistoryRepository, times( 1 ) ).findAll( );
    }
}