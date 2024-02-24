package at.technikum.Invicrea2WebShopbackend.service;


import at.technikum.Invicrea2WebShopbackend.exception.EntityNotFoundException;
import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.Order;
import at.technikum.Invicrea2WebShopbackend.repository.AccountRepository;
import at.technikum.Invicrea2WebShopbackend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final AccountRepository accountRepository;

    public OrderService ( OrderRepository orderRepository, AccountRepository accountRepository ) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
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
}
