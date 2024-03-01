package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.OrderDto;
import at.technikum.Invicrea2WebShopbackend.mapper.OrderMapper;
import at.technikum.Invicrea2WebShopbackend.model.Order;
import at.technikum.Invicrea2WebShopbackend.model.OrderHistory;
import at.technikum.Invicrea2WebShopbackend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    private final OrderMapper orderMapper;

    private final OrderService orderService;

    // Constructor for injecting OrderMapper and OrderService
    public OrderController(OrderMapper orderMapper, OrderService orderService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
    }

    // GET requests on "/orders" returns all orders
    @GetMapping
    public List<OrderDto> readAll() {
        return orderService.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET requests on "/orders/{id}" returns an order by ID
    @GetMapping("/{id}")
    public OrderDto read(@PathVariable long id) {
        Order order = orderService.find(String.valueOf(id));
        return orderMapper.toDto(order);
    }

    // Handler for POST requests on "/orders" creates a new order
    @PostMapping("/create/{accountId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@PathVariable Long accountId) {
        Order order = orderService.createOrder(accountId);
        return orderMapper.toDto(order);
    }

    // Handler for PUT requests on "/orders/{id}" updates an order
    @PutMapping("/{id}")
    public OrderDto update(@PathVariable long id, @RequestBody @Valid OrderDto updatedOrderDto) {
        // Find the order in the database by its ID
        Order order = orderService.find(String.valueOf(id));

        // Update the order properties with the new values from the updatedOrderDto
        order.setOrderDate(updatedOrderDto.getOrderDate());
        // Set other properties accordingly

        // Save the updated order back to the database
        order = orderService.save(order);

        // Map the updated order back to a DTO and return it
        return orderMapper.toDto(order);
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

    @GetMapping("/account/{accountId}/order/{orderId}/details")
    public List<OrderHistory> getOrderDetailsByAccountIdAndOrderId( @PathVariable Long accountId,
                                                                    @PathVariable String orderId) {
        return orderService.getOrderDetailsByAccountIdAndOrderId(accountId, orderId);
    }
}