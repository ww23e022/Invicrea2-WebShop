package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.CoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST-Controller für die Verwaltung von Coins.
 */
@RestController
@RequestMapping("/coins")
public class CoinsController {

    @Autowired
    private CoinsService coinsService;

    @Autowired
    private AccountService accountService;

    /**
     * Fügt Coins zu einem Konto hinzu.
     *
     * @param accountId    Die ID des Kontos.
     * @param coinsToAdd   Die Anzahl der hinzuzufügenden Coins.
     * @return Eine Bestätigungsmeldung.
     */
    @PutMapping("/addCoins/{accountId}")
    public String addCoinsToAccount(@PathVariable Long accountId,
                                    @RequestParam int coinsToAdd) {
        Account account = accountService.getAccountById(accountId);
        coinsService.addCoinsToAccount(account, coinsToAdd);
        return "Coins were added successfully.";
    }

    /**
     * Subtrahiert Coins von einem Konto.
     *
     * @param accountId         Die ID des Kontos.
     * @param coinsToSubtract   Die Anzahl der abzuziehenden Coins.
     * @return Eine Bestätigungsmeldung.
     */
    @PutMapping("/subtractCoins/{accountId}")
    public String subtractCoinsFromAccount(@PathVariable Long accountId,
                                           @RequestParam int coinsToSubtract) {
        Account account = accountService.getAccountById(accountId);
        coinsService.subtractCoinsFromAccount(account, coinsToSubtract);
        return "Coins were subtracted successfully.";
    }
}