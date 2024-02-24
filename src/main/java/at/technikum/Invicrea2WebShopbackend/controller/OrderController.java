package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.AccountDto;
import at.technikum.Invicrea2WebShopbackend.dto.OrderDto;
import at.technikum.Invicrea2WebShopbackend.mapper.OrderMapper;
import at.technikum.Invicrea2WebShopbackend.model.CoinTransaction;
import at.technikum.Invicrea2WebShopbackend.model.Order;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    private final OrderMapper orderMapper;

    @Autowired
    private AccountService accountService;

    private final OrderService orderService;

    // Constructor for injecting OrderMapper and OrderService
    public OrderController(OrderMapper orderMapper, OrderService orderService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
    }

    // Handler for GET requests on "/orders" returns all orders
    @GetMapping
    public List<OrderDto> readAll() {
        return orderService.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    // Handler for GET requests on "/orders/{id}" returns an order by ID
    @GetMapping("/{id}")
    public OrderDto read(@PathVariable long id) {
        Order order = orderService.find(String.valueOf(id));
        return orderMapper.toDto(order);
    }

    // Handler for POST requests on "/orders" creates a new order
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@RequestBody @Valid OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        order = orderService.save(order);
        return orderMapper.toDto(order);
    }

    // Handler for PUT requests on "/orders/{id}" updates an order
    @PutMapping("/{id}")
    public OrderDto update(@PathVariable long id) {
        // This method returns a dummy OrderDto with sample data;
        // it needs to be implemented to update orders
        return new OrderDto("1",
                LocalDateTime.now(),
                new AccountDto(),
                new ArrayList<CoinTransaction>());
    }

    // Handler for DELETE requests on "/orders/{id}" deletes an order
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        orderService.delete(id);
    }

    @GetMapping("/accounts/{accountId}/orders")
    public List<Order> getOrderHistoryByAccountId(@PathVariable Long accountId) {
        return orderService.getOrderHistoryByAccountId(accountId);
    }
}