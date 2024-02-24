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
    private AccountService accountService; // Here, the AccountService needs to be injected

    // Handler for PUT requests on "/coins/addCoins/{accountId}" adds coins to an account
    @PutMapping("/addCoins/{accountId}")
    public String addCoinsToAccount(@PathVariable Long accountId,
                                    @RequestParam int coinsToAdd) {
        try {
            // Get the account by ID
            Account account = accountService.getAccountById(accountId);
            // Add coins to the account
            coinsService.addCoinsToAccount(account, coinsToAdd);
            return "Coins were added successfully.";
        } catch (Exception e) {
            return "Error adding coins: " + e.getMessage();
        }
    }

    // Handler for PUT requests on "/coins/subtractCoins/{accountId}"
    // subtracts coins from an account
    @PutMapping("/subtractCoins/{accountId}")
    public String subtractCoinsFromAccount(@PathVariable Long accountId,
                                           @RequestParam int coinsToSubtract) {
        try {
            // Get the account by ID
            Account account = accountService.getAccountById(accountId);
            // Subtract coins from the account
            coinsService.subtractCoinsFromAccount(account, coinsToSubtract);
            return "Coins were subtracted successfully.";
        } catch (Exception e) {
            return "Error subtracting coins: " + e.getMessage();
        }
    }
}