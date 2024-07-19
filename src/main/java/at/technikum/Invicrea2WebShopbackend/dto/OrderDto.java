package at.technikum.Invicrea2WebShopbackend.dto;

import at.technikum.Invicrea2WebShopbackend.model.CoinTransaction;
import at.technikum.Invicrea2WebShopbackend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Dto representing order-related information.
 */
public class OrderDto {
    private Long id;
    private LocalDateTime orderDate;

    @NotNull
    @Positive
    private int totalPrice;
    @JsonIgnore
    private User user;

    public OrderDto() {
    }

    public OrderDto(Long id, LocalDateTime orderDate, User user,
                    List<CoinTransaction> coinTransactions) {
        this.id = id;
        this.orderDate = orderDate;
        this.user = user;
    }

    public Long getId () {
        return id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
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
