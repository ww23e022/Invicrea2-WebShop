package at.technikum.Invicrea2WebShopbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    // Konstruktor
    public TokenRequest ( String username, String password ) {
        this.username = username;
        this.password = password;
    }

    // Getter und Setter
    public String getUsername () {
        return username;
    }

    public String getPassword () {
        return password;
    }

}
