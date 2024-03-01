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

    // Konstruktor für das Injizieren von OrderMapper and OrderService
    public OrderController(OrderMapper orderMapper, OrderService orderService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
    }

    // Diese Methode verarbeitet GET-Anfragen auf dem Endpunkt "/orders".
    // Sie ruft alle Bestellungen über den orderService ab.
    @GetMapping
    public List<OrderDto> readAll() {
        return orderService.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    // Diese Methode verarbeitet GET-Anfragen auf dem Endpunkt "/orders/{id}".
    // Sie ruft die Bestellung mit der angegebenen ID über den orderService ab.
    @GetMapping("/{id}")
    public OrderDto read(@PathVariable long id) {
        Order order = orderService.find(String.valueOf(id));
        return orderMapper.toDto(order);
    }

    // Diese Methode verarbeitet POST-Anfragen auf dem Endpunkt "/orders/create/{accountId}".
    // Sie erstellt eine neue Bestellung für das angegebene Konto (Account) über den OrderService.
    @PostMapping("/create/{accountId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@PathVariable Long accountId) {
        Order order = orderService.createOrder(accountId);
        return orderMapper.toDto(order);
    }

    // Diese Methode verarbeitet PUT-Anfragen auf dem Endpunkt "/orders/{id}".
    // Sie sucht die Bestellung in der Datenbank anhand ihrer ID und
    // aktualisiert dann die Eigenschaften der Bestellung mit den neuen Werten.
    @PutMapping("/{id}")
    public OrderDto update(@PathVariable long id, @RequestBody @Valid OrderDto updatedOrderDto) {
        // Sie findet die Bestellung in der Datenbank anhand ihrer ID.
        Order order = orderService.find(String.valueOf(id));

        // Sie aktualisiert dann die Eigenschaften der Bestellung mit den neuen Werten.
        order.setOrderDate(updatedOrderDto.getOrderDate());

        // Sie speichert die aktualisierte Bestellung zurück in die Datenbank.
        order = orderService.save(order);

        // Sie konvertiert die aktualisierte Bestellung zurück in ein DTO und gib es zurück.
        return orderMapper.toDto(order);
    }

    // Diese Methode verarbeitet DELETE-Anfragen auf dem Endpunkt "/orders/{id}".
    // Sie löscht die Bestellung mit der angegebenen ID über den OrderService.
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        orderService.delete(id);
    }
    // Diese Methode verarbeitet GET-Anfragen
    // auf dem Endpunkt "/orders/accounts/{accountId}/orders".
    // Sie ruft die Bestellhistorie für das angegebene Konto (Account)
    // über den orderService ab und gibt sie zurück.
    @GetMapping("/accounts/{accountId}/orders")
    public List<Order> getOrderHistoryByAccountId(@PathVariable Long accountId) {
        return orderService.getOrderHistoryByAccountId(accountId);
    }
    // Diese Methode verarbeitet GET-Anfragen
    // auf dem Endpunkt "/orders/account/{accountId}/order/{orderId}/details".#
    // Sie ruft die Bestelldetails für das angegebene Konto (Account) und
    // die angegebene Bestell-ID über den orderService ab und gibt sie zurück.
    @GetMapping("/account/{accountId}/order/{orderId}/details")
    public List<OrderHistory> getOrderDetailsByAccountIdAndOrderId( @PathVariable Long accountId,
                                                                    @PathVariable String orderId) {
        return orderService.getOrderDetailsByAccountIdAndOrderId(accountId, orderId);
    }
}