package at.technikum.Invicrea2WebShopbackend.security.permission;

import at.technikum.Invicrea2WebShopbackend.model.Order;
import at.technikum.Invicrea2WebShopbackend.security.principal.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPermission implements AccessPermission {

    @Override
    public boolean supports(Authentication authentication, String className) {
        return className.equals(Order.class.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication, long resourceId) {
        log.info("Order Permission accessed for {}", resourceId);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId().equals(resourceId) || principal.getRole().equals("ROLE_ADMIN");
    }
}
