package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.repository.AccountRepository;
import at.technikum.Invicrea2WebShopbackend.repository.CoinTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CoinTransactionRepository coinTransactionRepository;

    @Transactional
    public void addCoinsToAccount(Account account, int coinsToAdd) {

    }


    public void subtractCoinsFromAccount(Account account, int coinsToSubtract) {

    }


    public int getCoinsInAccount(Account account) {
        return account.getCoins();
    }
}