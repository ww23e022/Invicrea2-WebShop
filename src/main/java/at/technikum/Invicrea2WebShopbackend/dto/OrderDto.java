package at.technikum.Invicrea2WebShopbackend.dto;

import at.technikum.Invicrea2WebShopbackend.model.CoinTransaction;
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
    private AccountDto account;

    public OrderDto() {
    }

    public OrderDto(Long id, LocalDateTime orderDate, AccountDto account,
                    List<CoinTransaction> coinTransactions) {
        this.id = id;
        this.orderDate = orderDate;
        this.account = account;
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

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(AccountDto account) {
        this.account = account;
    }

}
