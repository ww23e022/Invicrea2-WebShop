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

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    public OrderService(OrderRepository orderRepository,
                        AccountRepository accountRepository,
                        ShoppingCartItemRepository shoppingCartItemRepository) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
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
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            return account.getOrder();
        } else {
            throw new EntityNotFoundException("Das Benutzerkonto mit der ID "
                    + accountId + " wurde nicht gefunden.");
        }
    }

    public Order createOrder(Long accountId) {
        Account account = getAccountById(accountId);
        Order order = buildOrder(account);
        List<CoinTransaction> coinTransactions =
                createCoinTransactions(
                        account.getShoppingCart().getCartItems(),
                        order);
        saveOrderAndClearShoppingCart(order, account.getShoppingCart().getCartItems());
        saveOrderItemsInOrderHistory(coinTransactions, order);
        return order;
    }

    private Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Das Benutzerkonto mit der ID " + accountId + " wurde nicht gefunden."));
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

    private void saveOrderAndClearShoppingCart(Order order, List<ShoppingCartItem> cartItems) {
        orderRepository.save(order);
        shoppingCartItemRepository.deleteAll(cartItems);
    }

    private void saveOrderItemsInOrderHistory(List<CoinTransaction> coinTransactions, Order order) {
        for (CoinTransaction coinTransaction : coinTransactions) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setItem(coinTransaction.getItem());
            orderHistory.setQuantity(coinTransaction.getCoins());
            orderHistoryRepository.save(orderHistory);
        }
    }
}
