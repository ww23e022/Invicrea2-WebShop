package at.technikum.Invicrea2WebShopbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_cart_items") // Ändern Sie den Tabellennamen entsprechend
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "id")
    private ShoppingCart shoppingCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "itemId")
    private Item item;

    private int quantity;

    // Getters and setters


    public Long getId () {
        return id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    public ShoppingCart getShoppingCart () {
        return shoppingCart;
    }

    public void setShoppingCart ( ShoppingCart shoppingCart ) {
        this.shoppingCart = shoppingCart;
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
}