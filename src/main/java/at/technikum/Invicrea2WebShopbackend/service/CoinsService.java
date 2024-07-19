package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.CoinTransaction;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.repository.CoinTransactionRepository;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoinTransactionRepository coinTransactionRepository;

    @Transactional
    public void addCoinsToUser( User user, int coinsToAdd) {
        user.setCoins(user.getCoins() + coinsToAdd);
        userRepository.save(user);
        recordCoinTransaction(user, coinsToAdd, "ADD");
    }

    @Transactional
    public void subtractCoinsFromUser(User user, int coinsToSubtract) {
        if (user.getCoins() >= coinsToSubtract) {
            user.setCoins(user.getCoins() - coinsToSubtract);
            userRepository.save(user);
            recordCoinTransaction(user, coinsToSubtract, "SUBTRACT");
        } else {
            throw new IllegalArgumentException("Insufficient coins.");
        }
    }

    private void recordCoinTransaction(User user, int coinsChanged, String transactionType) {
        CoinTransaction coinTransaction = new CoinTransaction();
        coinTransaction.setUser(user);
        coinTransaction.setCoins(coinsChanged);
        coinTransaction.setTransactionType(transactionType);
        coinTransactionRepository.save(coinTransaction);
    }

    public int getCoinsInUser(User user) {
        return user.getCoins();
    }
}