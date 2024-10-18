package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.security.principal.UserPrincipal;
import at.technikum.Invicrea2WebShopbackend.service.CoinsService;
import at.technikum.Invicrea2WebShopbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** Controller zur Verwaltung von Münzen. */
@RestController
@RequestMapping("/coins")
public class CoinsController {

    @Autowired
    private CoinsService coinsService;

    @Autowired
    private UserService userService;

    /** Fügt Münzen zu einem Konto hinzu. Nur Admins können dies tun. */
    @PostMapping("/{userId}/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCoinsToUser(@PathVariable Long userId,
                                 @RequestParam int coinsToAdd) {
        User user = userService.getUserById(userId);
        coinsService.addCoinsToUser(user, coinsToAdd);
        return "Münzen wurden erfolgreich hinzugefügt.";
    }

    /** Subtrahiert Münzen von einem Konto. Nur Admins können dies tun. */
    @PutMapping("/{userId}/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String subtractCoinsFromUser(@PathVariable Long userId,
                                        @RequestParam int coinsToSubtract) {
        User user = userService.getUserById(userId);
        coinsService.subtractCoinsFromUser(user, coinsToSubtract);
        return "Münzen wurden erfolgreich abgezogen.";
    }

    /** Ruft die Anzahl der Münzen im Konto des eingeloggten Nutzers ab. */
    @GetMapping()
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.User', 'read')")
    public int viewCoinsInUser(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        return coinsService.getCoinsInUser(user);
    }
}
