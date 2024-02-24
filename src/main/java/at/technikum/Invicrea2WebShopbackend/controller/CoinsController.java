package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.CoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coins")
public class CoinsController {

    @Autowired
    private CoinsService coinsService;

    @Autowired
    private AccountService accountService; // Hier muss der AccountService eingebunden werden

    @PutMapping("/addCoins/{accountId}")
    public String addCoinsToAccount(@PathVariable Long accountId, @RequestParam int coinsToAdd) {
        try {
            Account account = accountService.getAccountById(accountId);
            coinsService.addCoinsToAccount(account, coinsToAdd);
            return "Coins wurden erfolgreich hinzugefügt.";
        } catch (Exception e) {
            return "Fehler beim Hinzufügen von Coins: " + e.getMessage();
        }
    }

    @PutMapping("/subtractCoins/{accountId}")
    public String subtractCoinsFromAccount(@PathVariable Long accountId,
                                           @RequestParam int coinsToSubtract) {
        try {
            Account account = accountService.getAccountById(accountId);
            coinsService.subtractCoinsFromAccount(account, coinsToSubtract);
            return "Coins wurden erfolgreich abgezogen.";
        } catch (Exception e) {
            return "Fehler beim Abziehen von Coins: " + e.getMessage();
        }
    }
}