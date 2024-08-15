package at.technikum.Invicrea2WebShopbackend.security.permission;

import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.security.user.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ItemsPermission implements AccessPermission {

    @Override
    public boolean supports(Authentication authentication, String className) {
        return className.equals(Item.class.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication, long resourceId) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        // Nur Admins dürfen Änderungen an Items vornehmen
        return principal.getRole().equals("ROLE_ADMIN");
    }
}
