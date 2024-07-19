package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.service.CoinsService;
import at.technikum.Invicrea2WebShopbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** Controller zur Verwaltung von Münzen. */
@RestController
@RequestMapping("/coins")
public class CoinsController {

    @Autowired
    private CoinsService coinsService;

    @Autowired
    private UserService userService;


    /** Fügt Münzen zu einem Konto hinzu. */
    @PostMapping("/{userId}/")
    public String addCoinsToUser(@PathVariable Long userId,
                                    @RequestParam int coinsToAdd) {
        // Holt das Konto anhand der User-ID
        User user = userService.getUserById(userId);
        // Fügt die Münzen dem Konto hinzu
        coinsService.addCoinsToUser(user, coinsToAdd);
        // Gibt eine Bestätigungsnachricht zurück
        return "Münzen wurden erfolgreich hinzugefügt.";
    }

    /** Subtrahiert Münzen von einem Konto. */
    @PutMapping("/{userId}/")
    public String subtractCoinsFromUser(@PathVariable Long userId,
                                           @RequestParam int coinsToSubtract) {
        // Holt das Konto anhand der User-ID
        User user = userService.getUserById(userId);
        // Subtrahiert die Münzen vom Konto
        coinsService.subtractCoinsFromUser(user, coinsToSubtract);
        // Gibt eine Bestätigungsnachricht zurück
        return "Münzen wurden erfolgreich abgezogen.";
    }

    /** Ruft die Anzahl der Münzen im angegebenen Konto ab. */
    @GetMapping("/{userId}")
    public int viewCoinsInUser(@PathVariable Long userId) {
        // Holt das Konto anhand der User-ID
        User user = userService.getUserById(userId);
        // Ruft die Anzahl der Münzen im Konto ab
        return coinsService.getCoinsInUser(user);
    }
}
