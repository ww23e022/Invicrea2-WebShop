package at.technikum.Invicrea2WebShopbackend.mapper;

import at.technikum.Invicrea2WebShopbackend.dto.OrderDto;
import at.technikum.Invicrea2WebShopbackend.model.Order;
import org.springframework.stereotype.Component;


/**
 * Mapper-Klasse, die für die Abbildung zwischen
 * Order- und OrderDto-Objekten verantwortlich ist.
 */
@Component
public class OrderMapper {

    /**
     * Bildet ein Order-Objekt auf ein OrderDto-Objekt ab.
     */
    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        return orderDto;
    }
}
