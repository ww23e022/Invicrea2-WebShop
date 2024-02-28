package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.CoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing coins.
 */
@RestController
@RequestMapping("/coins")
public class CoinsController {

    @Autowired
    private CoinsService coinsService;

    @Autowired
    private AccountService accountService;

    /**
     * Adds coins to an account.
     *
     * @param accountId    The ID of the account.
     * @param coinsToAdd   The number of coins to add.
     * @return A confirmation message.
     */
    @PutMapping("/addCoins/{accountId}")
    public String addCoinsToAccount(@PathVariable Long accountId,
                                    @RequestParam int coinsToAdd) {
        Account account = accountService.getAccountById(accountId);
        coinsService.addCoinsToAccount(account, coinsToAdd);
        return "Coins were added successfully.";
    }

    /**
     * Subtracts coins from an account.
     *
     * @param accountId         The ID of the account.
     * @param coinsToSubtract   The number of coins to subtract.
     * @return A confirmation message.
     */
    @PutMapping("/subtractCoins/{accountId}")
    public String subtractCoinsFromAccount(@PathVariable Long accountId,
                                           @RequestParam int coinsToSubtract) {
        Account account = accountService.getAccountById(accountId);
        coinsService.subtractCoinsFromAccount(account, coinsToSubtract);
        return "Coins were subtracted successfully.";
    }

    /**
     * Retrieves the number of coins in the specified account.
     *
     * @param accountId The ID of the account.
     * @return The number of coins in the account.
     */
    @GetMapping("/viewCoins/{accountId}")
    public int viewCoinsInAccount(@PathVariable Long accountId) {
        // Ruft das Konto mit der angegebenen Account-ID ab
        Account account = accountService.getAccountById(accountId);

        // Ruft die Anzahl der Münzen im Konto über den CoinsService ab
        return coinsService.getCoinsInAccount(account);
    }
}