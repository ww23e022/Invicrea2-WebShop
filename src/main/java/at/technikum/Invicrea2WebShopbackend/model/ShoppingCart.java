package at.technikum.Invicrea2WebShopbackend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    private List<ShoppingCartItem> cartItems;

    // Getters and setters

    public Long getId () {
        return id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    public Account getAccount () {
        return account;
    }

    public void setAccount ( Account account ) {
        this.account = account;
    }

    public List<ShoppingCartItem> getCartItems () {
        return cartItems;
    }

    public void setCartItems ( List<ShoppingCartItem> cartItems ) {
        this.cartItems = cartItems;
    }
}