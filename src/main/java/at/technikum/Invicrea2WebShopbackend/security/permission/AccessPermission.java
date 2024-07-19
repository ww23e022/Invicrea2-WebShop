package at.technikum.Invicrea2WebShopbackend.security.permission;

import org.springframework.security.core.Authentication;

public interface AccessPermission {
    boolean supports(Authentication authentication, String className);

    boolean hasPermission(Authentication authentication, long resourceId);
}
