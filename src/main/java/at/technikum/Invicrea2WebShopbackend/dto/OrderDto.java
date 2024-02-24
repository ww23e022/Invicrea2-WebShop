package at.technikum.Invicrea2WebShopbackend.dto;

import at.technikum.Invicrea2WebShopbackend.model.CoinTransaction;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    private String id;
    private LocalDateTime orderDate;
    @JsonIgnore
    private AccountDto account;
    private List<CoinTransaction> coinTransactions;

    public OrderDto() {
    }

    public OrderDto(String id, LocalDateTime orderDate, AccountDto account,
                    List<CoinTransaction> coinTransactions) {
        this.id = id;
        this.orderDate = orderDate;
        this.account = account;
        this.coinTransactions = coinTransactions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(AccountDto account) {
        this.account = account;
    }

    public List<CoinTransaction> getCoinTransactions() {
        return coinTransactions;
    }

    public void setCoinTransactions(List<CoinTransaction> coinTransactions) {
        this.coinTransactions = coinTransactions;
    }
}
