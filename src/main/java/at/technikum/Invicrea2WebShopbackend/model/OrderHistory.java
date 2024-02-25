package at.technikum.Invicrea2WebShopbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_history")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "itemId")
    private Item item;

    @Column(name = "item_name")
    private String itemName;

    private int quantity;

    // Konstruktoren, Getter und Setter


    public Long getId () {
        return id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    public Order getOrder () {
        return order;
    }

    public void setOrder ( Order order ) {
        this.order = order;
    }

    public Item getItem () {
        return item;
    }

    public void setItem ( Item item ) {
        this.item = item;
    }

    public int getQuantity () {
        return quantity;
    }

    public void setQuantity ( int quantity ) {
        this.quantity = quantity;
    }

    public String getItemName () {
        return itemName;
    }

    public void setItemName ( String itemName ) {
        this.itemName = itemName;
    }
}