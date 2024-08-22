package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.OrderDto;
import at.technikum.Invicrea2WebShopbackend.mapper.OrderMapper;
import at.technikum.Invicrea2WebShopbackend.model.Order;
import at.technikum.Invicrea2WebShopbackend.model.OrderHistory;
import at.technikum.Invicrea2WebShopbackend.security.user.UserPrincipal;
import at.technikum.Invicrea2WebShopbackend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    public OrderController(OrderMapper orderMapper, OrderService orderService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
    }

    @GetMapping(params = "status=sold")
    public long getTotalQuantitySold() {
        return orderService.getTotalQuantitySold();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<OrderDto> readAll() {
        return orderService.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.Order', 'read')")
    public OrderDto read(@PathVariable long id,
                         @AuthenticationPrincipal UserPrincipal principal) {
        Order order = orderService.find(String.valueOf(id));
        return orderMapper.toDto(order);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@AuthenticationPrincipal UserPrincipal principal) {
        Order order = orderService.createOrder(principal.getId());
        return orderMapper.toDto(order);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.Order', 'write')")
    public OrderDto update(@PathVariable long id,
                           @RequestBody @Valid OrderDto updatedOrderDto,
                           @AuthenticationPrincipal UserPrincipal principal) {
        Order order = orderService.find(String.valueOf(id));
        order.setOrderDate(updatedOrderDto.getOrderDate());
        order = orderService.save(order);
        return orderMapper.toDto(order);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.Order', 'delete')")
    public void delete(@PathVariable String id,
                       @AuthenticationPrincipal UserPrincipal principal) {
        orderService.delete(id);
    }

    @GetMapping("/history")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.Order', 'read')")
    public List<Order> getOrderHistory(@AuthenticationPrincipal UserPrincipal principal) {
        return orderService.getOrderHistoryByUserId(principal.getId());
    }

    @GetMapping("/details/{orderId}")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.Order', 'read')")
    public List<OrderHistory> getOrderDetails(@PathVariable String orderId,
                                              @AuthenticationPrincipal UserPrincipal principal) {
        return orderService.getOrderDetailsByUserIdAndOrderId(principal.getId(), orderId);
    }

    @GetMapping("/details/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<OrderHistory> getAllOrderDetails() {
        return orderService.getAllOrderDetails();
    }
}
