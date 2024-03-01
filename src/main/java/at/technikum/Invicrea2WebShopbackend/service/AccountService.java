package at.technikum.Invicrea2WebShopbackend.service;
import at.technikum.Invicrea2WebShopbackend.dto.AccountDto;
import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.Salutation;
import at.technikum.Invicrea2WebShopbackend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }



    public boolean isValidAccountId(Long accountId) {

        return false;
    }

    public Account getAccountById(Long accountId) {

        return null;
    }

    public void registerAccount( AccountDto accountDto, Salutation salutation) {

    }

    public Account updateAccount(Long accountId, AccountDto accountDto) {

        return null;
    }

    public void banAccount(Long accountId) {

    }

    public void unbanAccount(Long accountId) {

    }

    public void deleteAccountById(Long accountId) {

    }

}
