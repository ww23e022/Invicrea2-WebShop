package at.technikum.Invicrea2WebShopbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String id;

    private LocalDateTime orderDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToMany
    private List<CoinTransaction> coinTransactions;

    public Order(String id) {
        this.id = id;
        orderDate = LocalDateTime.now();
    }

    protected Order() {
        orderDate = LocalDateTime.now();
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<CoinTransaction> getCoinTransactions() {
        return coinTransactions;
    }

    public void setCoinTransactions(List<CoinTransaction> coinTransactions) {
        this.coinTransactions = coinTransactions;
    }
}