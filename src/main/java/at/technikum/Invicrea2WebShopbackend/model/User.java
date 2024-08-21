package at.technikum.Invicrea2WebShopbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Email(message = "Email is not valid")
    private String email;

    private String password;

    private String repeatPassword;

    private String role;

    @Enumerated(EnumType.STRING)
    private Salutation salutation;

    private String country;

    private int coins = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE; // Standardstatus

    private String additionalInfo;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Player> players;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> order;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private ShoppingCart shoppingCart;
    // Weitere Felder und Methoden nach Bedarf

    public User ( String username, String email, String role, int coins ) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.coins = coins;
    }

    public User() {
    }
}
