package at.technikum.Invicrea2WebShopbackend.security.jwt;

import at.technikum.Invicrea2WebShopbackend.property.JwtProperties;
import at.technikum.Invicrea2WebShopbackend.security.TokenIssuer;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class JwtIssuer implements TokenIssuer {
    private final JwtProperties jwtProperties;


    @Override
    public String issue(Long userId, String username, String role) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(2, ChronoUnit.MINUTES)))
                .withClaim("username", username)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }
}
