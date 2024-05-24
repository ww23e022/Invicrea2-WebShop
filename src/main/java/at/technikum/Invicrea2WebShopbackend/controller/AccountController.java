package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.AccountDto;
import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.Salutation;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "account")
@CrossOrigin
public class AccountController {

    // Dependency Injection for AccountService and ItemService ss
    private final AccountService accountService;
    private final ItemService itemService;

    @Autowired
    public AccountController(AccountService accountService,
                             ItemService itemService) {
        this.accountService = accountService;
        this.itemService = itemService;
    }

    // GET requests on "/account" returns a list of all accounts
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    //GET requests on "/account/{accountId}" returns an account by ID
    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId);
    }

    //POST requests registers a new account
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@RequestBody AccountDto accountDto) {
        // Check if passwords match
        if (!accountDto.getPassword().equals(accountDto.getRepeatPassword())) {
            return "Passwords do not match";
        }

        // Check and set the salutation of the account
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

        // Register the account
        accountService.registerAccount(accountDto, salutation);
        return "Account registered successfully";
    }

    // PUT requests on "/account/{accountId}" updates an account
    @PutMapping("/{accountId}")
    public Account updateAccount(@PathVariable Long accountId,
                                 @RequestBody AccountDto accountDto) {
        return accountService.updateAccount(accountId, accountDto);
    }

    // PUT requests on "/account?{accountId}=ban" bans an account
    @PutMapping(params = "{accountId}=ban")
    public String banAccount(@PathVariable Long accountId) {
        accountService.banAccount(accountId);
        return "Account banned successfully";
    }

    // PUT requests on "/account?{accountId}=unban" unbans an account
    @PutMapping(params = "{accountId}=unban")
    public String unbanAccount(@PathVariable Long accountId) {
        accountService.unbanAccount(accountId);
        return "Account unbanned successfully";
    }

    // DELETE requests on "/account/{accountId}" deletes an account
    @DeleteMapping(params = "/{accountId}")
    public String deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccountById(accountId);
        return "Account deleted successfully";
    }


/*
    // POST requests on "/account/login" performs a login
    @PostMapping("/login")
    public String login(@RequestBody AccountDto accountDto) {
        String usernameOrEmail = accountDto.getUsername();
        // Check if email address is set, otherwise use the username
        if (accountDto.getEmail() != null && !accountDto.getEmail().isEmpty()) {
            usernameOrEmail = accountDto.getEmail();
        }

        String password = accountDto.getPassword();

        // Perform user authentication
        if (accountService.authenticate(usernameOrEmail, password)) {
            return "Login successful";
        } else {
            return "Invalid username or password";
        }
    }

    // POST requests on "/account/logout" performs a logout
    @PostMapping("/logout")
    public String logout() {
        return "Logout successful";
    }
    */
}