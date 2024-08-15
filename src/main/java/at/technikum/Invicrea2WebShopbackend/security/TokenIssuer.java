package at.technikum.Invicrea2WebShopbackend.security;

import org.springframework.stereotype.Component;

@Component
public interface TokenIssuer {
    String issue(Long userId, String username, String role);
}
