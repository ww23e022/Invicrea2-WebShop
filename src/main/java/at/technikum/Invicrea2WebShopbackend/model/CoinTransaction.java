package at.technikum.Invicrea2WebShopbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing a transaction with coins.
 */
@Entity
@Table(name = "coin_transaction")
public class CoinTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "itemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull(message = "Coins amount must be specified")
    @Min(value = 0, message = "Coins amount must be positive")
    private int coins;

    @NotBlank(message = "Transaction type must be specified")
    private String transactionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Order getOrder () {
        return order;
    }

    public void setOrder ( Order order ) {
        this.order = order;
    }
}