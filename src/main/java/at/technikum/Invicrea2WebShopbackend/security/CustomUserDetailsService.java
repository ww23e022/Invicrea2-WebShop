package at.technikum.Invicrea2WebShopbackend.security;

import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.security.principal.UserPrincipal;
import at.technikum.Invicrea2WebShopbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Optional<User> optionalUser = userService.findByUsernameOrEmail(identifier);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(identifier);
        }

        User user = optionalUser.get();

        return new UserPrincipal(user.getId(),
                user.getUsername(), user.getPassword(), user.getRole());
    }
}
