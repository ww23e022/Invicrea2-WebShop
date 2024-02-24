package at.technikum.Invicrea2WebShopbackend.mapper;

import at.technikum.Invicrea2WebShopbackend.dto.OrderDto;
import at.technikum.Invicrea2WebShopbackend.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto toDto (Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());

        return orderDto;
    }
    public Order toEntity(OrderDto orderDto) {
        if (orderDto.getId() == null) {
            return new Order(); // Wenn die ID null ist, erstellen Sie
            // eine neue Bestellung mit automatisch generierter ID
        } else {
            return new Order(orderDto.getId());
            // Andernfalls verwenden Sie die angegebene ID aus dem OrderDto
        }
    }
}
