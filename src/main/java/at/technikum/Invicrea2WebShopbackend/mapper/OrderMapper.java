package at.technikum.Invicrea2WebShopbackend.mapper;

import at.technikum.Invicrea2WebShopbackend.dto.OrderDto;
import at.technikum.Invicrea2WebShopbackend.model.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMapper {

    public OrderDto toDto (Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());

        return orderDto;
    }
    public Order toEntity(OrderDto orderDto) {
        if (orderDto.getId() == null) {
            return new Order(
                    UUID.randomUUID().toString()
            );
        }

        return new Order(
                orderDto.getId()
        );
    }
}
