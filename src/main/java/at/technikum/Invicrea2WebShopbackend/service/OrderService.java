package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.exception.EntityNotFoundException;
import at.technikum.Invicrea2WebShopbackend.exception.InsufficientCoinsException;
import at.technikum.Invicrea2WebShopbackend.model.*;
import at.technikum.Invicrea2WebShopbackend.repository.AccountRepository;
import at.technikum.Invicrea2WebShopbackend.repository.OrderHistoryRepository;
import at.technikum.Invicrea2WebShopbackend.repository.OrderRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        AccountRepository accountRepository,
                        ShoppingCartItemRepository shoppingCartItemRepository,
                        OrderHistoryRepository orderHistoryRepository) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order find(String id) {
        return orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void delete(String id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrderHistoryByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Das Benutzerkonto mit der ID " +
                                accountId
                                + " wurde nicht gefunden."));
        return account.getOrder();
    }

    public Order createOrder(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Das Benutzerkonto mit der ID " +
                                accountId +
                                " wurde nicht gefunden."));

        ShoppingCart shoppingCart = account.getShoppingCart();
        List<ShoppingCartItem> cartItems = shoppingCart.getCartItems();

        int totalCoinsNeeded = calculateTotalCoins(cartItems);
        if (account.getCoins() < totalCoinsNeeded) {
            throw new InsufficientCoinsException("Nicht genügend Münzen auf dem Konto");
        }

        account.setCoins(account.getCoins() - totalCoinsNeeded);
        accountRepository.save(account);

        Order order = buildOrder(account);

        saveOrderAndClearShoppingCart(order, shoppingCart);

        saveOrderItemsInOrderHistory(cartItems, order);

        return order;
    }

    private int calculateTotalCoins(List<ShoppingCartItem> cartItems) {
        int totalCoins = 0;
        for (ShoppingCartItem cartItem : cartItems) {
            totalCoins += cartItem.getItem().getPrice() * cartItem.getQuantity();
        }
        return totalCoins;
    }

    private Order buildOrder(Account account) {
        Order order = new Order();
        order.setAccount(account);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private void saveOrderAndClearShoppingCart(Order order, ShoppingCart shoppingCart) {
        orderRepository.save(order);
        List<Long> cartItemIds = new ArrayList<>();
        for (ShoppingCartItem cartItem : shoppingCart.getCartItems()) {
            cartItemIds.add(cartItem.getId());
        }
        shoppingCartItemRepository.deleteAllByIdIn(cartItemIds);
    }

    private void saveOrderItemsInOrderHistory(
            List<ShoppingCartItem> cartItems,
            Order order) {
        for (ShoppingCartItem cartItem : cartItems) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setItem(cartItem.getItem());
            orderHistory.setItemName(cartItem.getItem().getName());
            int totalPrice = cartItem.getItem().getPrice() * cartItem.getQuantity();
            orderHistory.setItemPrice(totalPrice);
            orderHistory.setQuantity(cartItem.getQuantity());
            orderHistoryRepository.save(orderHistory);
        }
    }
}