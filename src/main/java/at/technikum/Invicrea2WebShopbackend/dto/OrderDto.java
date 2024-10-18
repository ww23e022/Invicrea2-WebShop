package at.technikum.Invicrea2WebShopbackend.dto;

import at.technikum.Invicrea2WebShopbackend.model.OrderStatus;
import at.technikum.Invicrea2WebShopbackend.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;


/**
 * Dto representing order-related information.
 */
public class OrderDto {
    private Long id;
    private LocalDateTime orderDate;

    @NotNull
    @Positive
    private int totalPrice;

    private OrderStatus status;

    private User user;

    public OrderDto () {
    }

    public OrderDto ( Long id, LocalDateTime orderDate, int totalPrice, User user ) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.user = user;
    }

    public OrderStatus getStatus () {
        return status;
    }

    public void setStatus ( OrderStatus status ) {
        this.status = status;
    }

    public Long getId () {
        return id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    public LocalDateTime getOrderDate () {
        return orderDate;
    }

    public void setOrderDate ( LocalDateTime orderDate ) {
        this.orderDate = orderDate;
    }

    public int getTotalPrice () {
        return totalPrice;
    }

    public void setTotalPrice ( int totalPrice ) {
        this.totalPrice = totalPrice;
    }

    public User getUser () {
        return user;
    }

    public void setUser ( User user ) {
        this.user = user;
    }
}
