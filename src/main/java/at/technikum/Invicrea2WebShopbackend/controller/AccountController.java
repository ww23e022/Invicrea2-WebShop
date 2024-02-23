package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.AccountDto;
import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.Salutation;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "account")
@CrossOrigin
public class AccountController {
    
    private final AccountService accountService;
    private final ItemService itemService;

    @Autowired
    public AccountController(AccountService accountService, ItemService itemService) {
        this.accountService = accountService;
        this.itemService = itemService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId);
    }

    @PostMapping("/register")
    public String registerAccount(@RequestBody AccountDto accountDto) {
        if (!accountDto.getPassword().equals(accountDto.getRepeatPassword())) {
            return "Passwords do not match";
        }

        Salutation salutation;

        if (accountDto.getSalutation().equals("OTHER")) {
            salutation = Salutation.OTHER;
        } else {
            try {
                salutation = Salutation.valueOf(accountDto.getSalutation());
            } catch (IllegalArgumentException e) {
                return "Invalid salutation value";
            }
        }

        accountService.registerAccount(accountDto, salutation);
        return "Account registered successfully";
    }

    @PutMapping("/{accountId}")
    public Account updateAccount(@PathVariable Long accountId,
                                 @RequestBody AccountDto accountDto) {
        return accountService.updateAccount(accountId, accountDto);
    }

    @PutMapping("/ban/{accountId}")
    public String banAccount(@PathVariable Long accountId) {
        accountService.banAccount(accountId);
        return "Account banned successfully";
    }

    @PutMapping("/unban/{accountId}")
    public String unbanAccount(@PathVariable Long accountId) {
        accountService.unbanAccount(accountId);
        return "Account unbanned successfully";
    }

    @DeleteMapping("/{accountId}")
    public String deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccountById(accountId);
        return "Account deleted successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody AccountDto accountDto) {
        String usernameOrEmail = accountDto.getUsername();
        // Überprüfen Sie, ob die E-Mail-Adresse gesetzt ist,
        // wenn nicht, verwenden Sie den Benutzernamen
        if (accountDto.getEmail() != null && !accountDto.getEmail().isEmpty()) {
            usernameOrEmail = accountDto.getEmail();
        }

        String password = accountDto.getPassword();

        if (accountService.authenticate(usernameOrEmail, password)) {
            return "Login successful";
        } else {
            return "Invalid username or password";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        return "Logout successful";
    }

    @GetMapping("/{accountId}/items")
    public List<Item> getItemsByAccountId( @PathVariable Long accountId) {
        return accountService.getItems(accountId);
    }
}
