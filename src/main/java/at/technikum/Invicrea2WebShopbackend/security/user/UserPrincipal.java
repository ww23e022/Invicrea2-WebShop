package at.technikum.Invicrea2WebShopbackend.security.user;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {

    private Long id;
    private String role;

    public UserPrincipal(Long id, String username, String password, String role) {
        super(username, password, List.of(new SimpleGrantedAuthority(role)));
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return super.getUsername();
    }

    public String getRole() {
        return role;
    }
}