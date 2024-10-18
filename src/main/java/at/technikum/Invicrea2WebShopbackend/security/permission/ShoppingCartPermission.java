package at.technikum.Invicrea2WebShopbackend.security.permission;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import at.technikum.Invicrea2WebShopbackend.security.principal.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartPermission implements AccessPermission {

    @Override
    public boolean supports(Authentication authentication, String className) {
        return className.equals(ShoppingCart.class.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication, long resourceId) {
        return ((UserPrincipal) authentication.getPrincipal()).getId().equals(resourceId);
    }
}
