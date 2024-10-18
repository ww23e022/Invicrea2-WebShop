package at.technikum.Invicrea2WebShopbackend.security.jwt;

import at.technikum.Invicrea2WebShopbackend.security.principal.UserPrincipal;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class JwtToPrincipalConverter {

    public UserPrincipal convert(DecodedJWT jwt) {
        return new UserPrincipal(
                Long.parseLong(jwt.getSubject()),
                jwt.getClaim("username").asString(),
                "",
                jwt.getClaim("role").asString()
        );
    }
}
