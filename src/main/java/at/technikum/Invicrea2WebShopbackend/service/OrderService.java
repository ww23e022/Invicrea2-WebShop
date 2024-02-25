package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.exception.EntityNotFoundException;
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
                                accountId
                                + " wurde nicht gefunden."));

        ShoppingCart shoppingCart = account.getShoppingCart();
        List<ShoppingCartItem> cartItems = shoppingCart.getCartItems();

        Order order = buildOrder(account);
        List<CoinTransaction> coinTransactions = createCoinTransactions(cartItems, order);

        saveOrderAndClearShoppingCart(order, shoppingCart.getCartItems());
        saveOrderItemsInOrderHistory(coinTransactions, order);

        return order;
    }

    private Order buildOrder(Account account) {
        Order order = new Order();
        order.setAccount(account);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private List<CoinTransaction> createCoinTransactions(
            List<ShoppingCartItem> cartItems,
            Order order) {
        List<CoinTransaction> coinTransactions = new ArrayList<>();
        for (ShoppingCartItem cartItem : cartItems) {
            CoinTransaction coinTransaction = new CoinTransaction();
            coinTransaction.setItem(cartItem.getItem());
            coinTransaction.setOrder(order);
            coinTransaction.setCoins(cartItem.getQuantity());
            coinTransactions.add(coinTransaction);
        }
        return coinTransactions;
    }

    private void saveOrderAndClearShoppingCart(
            Order order,
            List<ShoppingCartItem> cartItems) {
        order = orderRepository.save(order);
        for (ShoppingCartItem cartItem : cartItems) {
            shoppingCartItemRepository.delete(cartItem);
        }
    }

    private void saveOrderItemsInOrderHistory(
            List<CoinTransaction> coinTransactions,
            Order order) {
        for (CoinTransaction coinTransaction : coinTransactions) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setItem(coinTransaction.getItem());
            // Setzen des Artikelnamens
            orderHistory.setItemName(coinTransaction.getItem().getName());
            orderHistory.setQuantity(coinTransaction.getCoins());
            orderHistoryRepository.save(orderHistory);
        }
    }
}