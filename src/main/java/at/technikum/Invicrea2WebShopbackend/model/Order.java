package at.technikum.Invicrea2WebShopbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.BOUGHT;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Order () {
        this.orderDate = LocalDateTime.now( );
        this.status = OrderStatus.BOUGHT;
    }
}
