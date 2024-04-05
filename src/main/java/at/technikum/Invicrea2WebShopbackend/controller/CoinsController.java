package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.CoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** Controller zur Verwaltung von Münzen. */
@RestController
@RequestMapping("/coins")
public class CoinsController {

    @Autowired
    private CoinsService coinsService;

    @Autowired
    private AccountService accountService;


    /** Fügt Münzen zu einem Konto hinzu. */
    @PostMapping("/{accountId}/")
    public String addCoinsToAccount(@PathVariable Long accountId,
                                    @RequestParam int coinsToAdd) {
        // Holt das Konto anhand der Account-ID
        Account account = accountService.getAccountById(accountId);
        // Fügt die Münzen dem Konto hinzu
        coinsService.addCoinsToAccount(account, coinsToAdd);
        // Gibt eine Bestätigungsnachricht zurück
        return "Münzen wurden erfolgreich hinzugefügt.";
    }

    /** Subtrahiert Münzen von einem Konto. */
    @PutMapping("/{accountId}/")
    public String subtractCoinsFromAccount(@PathVariable Long accountId,
                                           @RequestParam int coinsToSubtract) {
        // Holt das Konto anhand der Account-ID
        Account account = accountService.getAccountById(accountId);
        // Subtrahiert die Münzen vom Konto
        coinsService.subtractCoinsFromAccount(account, coinsToSubtract);
        // Gibt eine Bestätigungsnachricht zurück
        return "Münzen wurden erfolgreich abgezogen.";
    }

    /** Ruft die Anzahl der Münzen im angegebenen Konto ab. */
    @GetMapping("/{accountId}")
    public int viewCoinsInAccount(@PathVariable Long accountId) {
        // Holt das Konto anhand der Account-ID
        Account account = accountService.getAccountById(accountId);
        // Ruft die Anzahl der Münzen im Konto ab
        return coinsService.getCoinsInAccount(account);
    }
}