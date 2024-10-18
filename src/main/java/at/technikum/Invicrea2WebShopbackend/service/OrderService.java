package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.exception.EntityNotFoundException;
import at.technikum.Invicrea2WebShopbackend.exception.InsufficientCoinsException;
import at.technikum.Invicrea2WebShopbackend.model.*;
import at.technikum.Invicrea2WebShopbackend.repository.OrderHistoryRepository;
import at.technikum.Invicrea2WebShopbackend.repository.OrderRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    @Autowired
    public OrderService ( OrderRepository orderRepository,
                          UserRepository userRepository,
                          ShoppingCartItemRepository shoppingCartItemRepository,
                          OrderHistoryRepository orderHistoryRepository ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public long getTotalQuantitySold () {
        List<OrderHistory> orderHistories = orderHistoryRepository.findAll( );
        long totalQuantitySold = 0;

        for (OrderHistory orderHistory : orderHistories) {
            totalQuantitySold += orderHistory.getQuantity( );
        }

        return totalQuantitySold;
    }

    public List<Order> findAll () {
        return orderRepository.findAll( );
    }

    public Order find ( String id ) {
        return orderRepository.findById( id )
                .orElseThrow( EntityNotFoundException::new );
    }

    public Order save ( Order order ) {
        return orderRepository.save( order );
    }

    public void delete ( String id ) {
        orderRepository.deleteById( id );
    }

    public List<Order> getOrderHistoryByUserId ( Long userId ) {
        User user = userRepository.findById( userId )
                .orElseThrow( () -> new EntityNotFoundException(
                        "Der Benutzer mit der ID " +
                                userId +
                                " wurde nicht gefunden." ) );
        return user.getOrder( );
    }

    public Order createOrder ( Long userId ) {
        User user = userRepository.findById( userId )
                .orElseThrow( () -> new EntityNotFoundException(
                        "Der Benutzer mit der ID " +
                                userId +
                                " wurde nicht gefunden." ) );

        ShoppingCart shoppingCart = user.getShoppingCart( );
        List<ShoppingCartItem> cartItems = shoppingCart.getCartItems( );

        int totalCoinsNeeded = calculateTotalCoins( cartItems );
        if (user.getCoins( ) < totalCoinsNeeded) {
            throw new InsufficientCoinsException( "Nicht genügend Münzen auf dem Konto" );
        }
        user.setCoins( user.getCoins( ) - totalCoinsNeeded );
        userRepository.save( user );

        int totalCartPrice = 0; // Variable für den Gesamtpreis des Warenkorbs
        Order order = buildOrder( user );
        // Berechne den Gesamtpreis des Warenkorbs und speichere ihn
        for (ShoppingCartItem cartItem : cartItems) {
            int totalPrice = cartItem.getItem( ).getPrice( ) * cartItem.getQuantity( );
            totalCartPrice += totalPrice;
        }
        order.setTotalPrice( totalCartPrice );
        saveOrderAndClearShoppingCart( order, shoppingCart );
        saveOrderItemsInOrderHistory( cartItems, order );
        return order;
    }

    private int calculateTotalCoins ( List<ShoppingCartItem> cartItems ) {
        int totalCoins = 0;
        for (ShoppingCartItem cartItem : cartItems) {
            totalCoins += cartItem.getItem( ).getPrice( ) * cartItem.getQuantity( );
        }
        return totalCoins;
    }

    private Order buildOrder ( User user ) {
        Order order = new Order( );
        order.setUser( user );
        order.setOrderDate( LocalDateTime.now( ) );
        return order;
    }

    private void saveOrderAndClearShoppingCart ( Order order, ShoppingCart shoppingCart ) {
        orderRepository.save( order );
        List<Long> cartItemIds = new ArrayList<>( );
        for (ShoppingCartItem cartItem : shoppingCart.getCartItems( )) {
            cartItemIds.add( cartItem.getId( ) );
        }
        shoppingCartItemRepository.deleteAllByIdIn( cartItemIds );
    }

    private void saveOrderItemsInOrderHistory (
            List<ShoppingCartItem> cartItems,
            Order order ) {
        for (ShoppingCartItem cartItem : cartItems) {
            OrderHistory orderHistory = new OrderHistory( );
            orderHistory.setOrder( order );
            orderHistory.setItem( cartItem.getItem( ) );
            orderHistory.setItemName( cartItem.getItem( ).getName( ) );
            int totalPrice = cartItem.getItem( ).getPrice( ) * cartItem.getQuantity( );
            orderHistory.setItemPrice( totalPrice );
            orderHistory.setQuantity( cartItem.getQuantity( ) );
            orderHistoryRepository.save( orderHistory );
        }
    }

    public List<OrderHistory> getOrderDetailsByUserIdAndOrderId ( Long userId, String orderId ) {
        User user = userRepository.findById( userId )
                .orElseThrow( () -> new EntityNotFoundException(
                        "Der Benutzer mit der ID " +
                                userId +
                                " wurde nicht gefunden." ) );

        Order order = orderRepository.findById( orderId )
                .orElseThrow( () -> new EntityNotFoundException(
                        "Die Bestellung mit der ID " +
                                orderId +
                                " wurde nicht gefunden." ) );

        // Überprüfen, ob die Bestellung zum angegebenen Benutzer gehört
        if (!order.getUser( ).equals( user )) {
            throw new EntityNotFoundException( "Die Bestellung mit der ID " +
                    orderId +
                    " gehört nicht zum Benutzer mit der ID " +
                    userId );
        }

        return orderHistoryRepository.findByOrder( order );
    }

    public List<OrderHistory> getAllOrderDetails () {
        return orderHistoryRepository.findAll( );
    }

    public void requestOrderCancellation ( String orderId, Long userId ) {
        Order order = findOrderById( orderId );

        // Check if order belongs to the user
        if (!order.getUser( ).getId( ).equals( userId )) {
            throw new IllegalArgumentException( "You are not authorized to cancel this order." );
        }

        // Check if order is older than 10 days
        LocalDateTime tenDaysAgo = LocalDateTime.now( ).minusDays( 10 );
        if (order.getOrderDate( ).isBefore( tenDaysAgo )) {
            throw new IllegalArgumentException(
                    "Order cannot be cancelled as it is older than 10 days." );
        }

        // Check if cancellation is already requested or processed
        if (order.getStatus( ) == OrderStatus.CANCEL_REQUESTED || order.getStatus( ) ==
                OrderStatus.CANCELLED) {
            throw new IllegalArgumentException( "Cancellation already requested or processed." );
        }

        // Update status to CANCEL_REQUESTED
        order.setStatus( OrderStatus.CANCEL_REQUESTED );
        orderRepository.save( order );
    }

    public void processOrderCancellation ( String orderId, boolean approve ) {
        Order order = findOrderById( orderId );

        if (order.getStatus( ) != OrderStatus.CANCEL_REQUESTED) {
            throw new IllegalArgumentException( "Invalid cancellation request." );
        }

        if (approve) {
            // Refund coins to the user
            User user = order.getUser( );
            user.setCoins( user.getCoins( ) + order.getTotalPrice( ) );
            userRepository.save( user );
            order.setStatus( OrderStatus.CANCELLED );
        } else {
            order.setStatus( OrderStatus.REJECTED );
        }

        orderRepository.save( order );
    }

    private Order findOrderById ( String orderId ) {
        return orderRepository.findById( orderId )
                .orElseThrow( () -> new EntityNotFoundException( "Order not found" ) );
    }
}
