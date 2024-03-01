package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.*;
import at.technikum.Invicrea2WebShopbackend.repository.AccountRepository;
import at.technikum.Invicrea2WebShopbackend.repository.OrderHistoryRepository;
import at.technikum.Invicrea2WebShopbackend.repository.OrderRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        return null;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void delete(String id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrderHistoryByAccountId(Long accountId) {

        return null;
    }

    public Order createOrder(Long accountId) {

        return null;
    }

    private int calculateTotalCoins(List<ShoppingCartItem> cartItems) {

        return 0;
    }

    private Order buildOrder(Account account) {

        return null;
    }

    private void saveOrderAndClearShoppingCart(Order order, ShoppingCart shoppingCart) {

    }

    private void saveOrderItemsInOrderHistory(
            List<ShoppingCartItem> cartItems,
            Order order) {
    }

    public List<OrderHistory> getOrderDetailsByAccountIdAndOrderId(Long accountId, String orderId) {

        return null;
    }

}