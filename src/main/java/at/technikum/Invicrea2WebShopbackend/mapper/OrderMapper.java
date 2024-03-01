package at.technikum.Invicrea2WebShopbackend.mapper;

import at.technikum.Invicrea2WebShopbackend.dto.OrderDto;
import at.technikum.Invicrea2WebShopbackend.model.Order;
import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for mapping between Order and OrderDto objects.
 */
@Component
public class OrderMapper {

    /**
     * Maps an Order object to an OrderDto object.
     *
     * @param order The Order object to map.
     * @return The corresponding OrderDto object.
     */
    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        return orderDto;
    }
}
