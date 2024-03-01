package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.CoinTransaction;
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
        account.setCoins(account.getCoins() + coinsToAdd);
        accountRepository.save(account);
        recordCoinTransaction(account, coinsToAdd, "ADD");
    }

    @Transactional
    public void subtractCoinsFromAccount(Account account, int coinsToSubtract) {
        if (account.getCoins() >= coinsToSubtract) {
            account.setCoins(account.getCoins() - coinsToSubtract);
            accountRepository.save(account);
            recordCoinTransaction(account, coinsToSubtract, "SUBTRACT");
        } else {
            throw new IllegalArgumentException("Insufficient coins.");
        }
    }

    private void recordCoinTransaction(Account account, int coinsChanged, String transactionType) {
        CoinTransaction coinTransaction = new CoinTransaction();
        coinTransaction.setAccount(account);
        coinTransaction.setCoins(coinsChanged);
        coinTransaction.setTransactionType(transactionType);
        coinTransactionRepository.save(coinTransaction);
    }

    public int getCoinsInAccount(Account account) {
        return account.getCoins();
    }
}