package at.technikum.Invicrea2WebShopbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// extends AbstractPersistable<Long> // Id kann man auch so angeben

/**
 * Entität, die ein Benutzerkonto im Webshop repräsentiert.
 */
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Salutation salutation;

    @Column(length = 30) // Passen Sie die Länge an Ihre Anforderungen an
    private String additionalInfo; // Hinzugefügtes Feld für "Other"

    private String email;

    @NotNull
    private String username;

    private String password;

    private String repeatPassword;

    private String country;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private int coins;

    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<Player> players;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Order> order;

    @OneToOne(mappedBy = "account")
    private ShoppingCart shoppingCart;

    public Long getId () {
        return id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    public Salutation getSalutation () {
        return salutation;
    }

    public void setSalutation ( Salutation salutation ) {
        this.salutation = salutation;
    }

    public String getAdditionalInfo () {
        return additionalInfo;
    }

    public void setAdditionalInfo ( String additionalInfo ) {
        this.additionalInfo = additionalInfo;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername ( String username ) {
        this.username = username;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword ( String password ) {
        this.password = password;
    }

    public String getRepeatPassword () {
        return repeatPassword;
    }

    public void setRepeatPassword ( String repeatPassword ) {
        this.repeatPassword = repeatPassword;
    }

    public String getCountry () {
        return country;
    }

    public void setCountry ( String country ) {
        this.country = country;
    }

    public Role getRole () {
        return role;
    }

    public void setRole ( Role role ) {
        this.role = role;
    }

    public Status getStatus () {
        return status;
    }

    public void setStatus ( Status status ) {
        this.status = status;
    }

    public boolean isAdmin() {
        return Role.ADMIN.equals(role);
    }

    public int getCoins () {
        return coins;
    }

    public void setCoins ( int coins ) {
        this.coins = coins;
    }

    public List<Player> getPlayers () {
        return players;
    }

    public void setPlayers ( List<Player> players ) {
        this.players = players;
    }

    public List<Order> getOrder () {
        return order;
    }

    public void setOrder ( List<Order> order ) {
        this.order = order;
    }

    public ShoppingCart getShoppingCart () {
        return shoppingCart;
    }

    public void setShoppingCart ( ShoppingCart shoppingCart ) {
        this.shoppingCart = shoppingCart;
    }
}
