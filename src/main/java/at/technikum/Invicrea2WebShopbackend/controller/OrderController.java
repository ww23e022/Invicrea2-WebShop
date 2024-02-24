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

    public OrderController(OrderMapper orderMapper, OrderService orderService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
    }
    @GetMapping
    public List<OrderDto> readAll() {
        return orderService.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public OrderDto read(@PathVariable long id) {
        Order order = orderService.find(String.valueOf(id));

        return orderMapper.toDto(order);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@RequestBody @Valid OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        order = orderService.save(order);

        return orderMapper.toDto(order);
    }

    @PutMapping("/{id}")
    public OrderDto update(@PathVariable long id) {
        return new OrderDto("1",
                LocalDateTime.now(),
                new AccountDto(),
                new ArrayList<CoinTransaction>());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        orderService.delete(id);
    }
}
