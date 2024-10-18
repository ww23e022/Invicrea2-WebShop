package at.technikum.Invicrea2WebShopbackend.security.permission;

import at.technikum.Invicrea2WebShopbackend.model.Player;
import at.technikum.Invicrea2WebShopbackend.security.principal.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class PlayerPermission implements AccessPermission {

    @Override
    public boolean supports(Authentication authentication, String className) {
        return className.equals(Player.class.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication, long resourceId) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        // Der Spieler gehört dem eingeloggten Benutzer oder der Benutzer ist Admin
        return principal.getId().equals(resourceId) || principal.getRole().equals("ROLE_ADMIN");
    }
}
