package at.technikum.Invicrea2WebShopbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Entität, die einen Artikel im Warenkorb repräsentiert.
 */
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

    @Column(name = "item_name")
    private String itemName;

    @NotNull(message = "Item price must be specified")
    @Min(value = 0, message = "Item price must be positive")
    @Column(name = "item_price")
    private int itemPrice;

    @NotNull(message = "Quantity must be specified")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

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

    public String getItemName () {
        return itemName;
    }

    public void setItemName ( String itemName ) {
        this.itemName = itemName;
    }

    public int getItemPrice () {
        return itemPrice;
    }

    public void setItemPrice ( int itemPrice ) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "id=" + id +
                ", shoppingCart=" + shoppingCart +
                ", item=" + item +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", quantity=" + quantity +
                '}';
    }
}