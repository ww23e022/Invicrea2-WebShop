package at.technikum.Invicrea2WebShopbackend.dto;

import at.technikum.Invicrea2WebShopbackend.model.Order;
import at.technikum.Invicrea2WebShopbackend.model.Role;
import at.technikum.Invicrea2WebShopbackend.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor //(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class AccountDto {
    private Long id;
    private String salutation;
    private String additionalInfo; // Hinzugefügtes Feld für "Other" Geschlecht
    private String email;
    private String username;
    private String password;
    private String repeatPassword;
    private String country;
    private Role role;
    private Status status;
    private int coins;
    private List<Order> order;

    public String getSalutation () {
        return salutation;
    }

    public void setSalutation ( String salutation ) {
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

    public Long getId () {
        return id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    public int getCoins () {
        return coins;
    }

    public void setCoins ( int coins ) {
        this.coins = coins;
    }

    public List<Order> getOrder () {
        return order;
    }

    public void setOrder ( List<Order> order ) {
        this.order = order;
    }
}
