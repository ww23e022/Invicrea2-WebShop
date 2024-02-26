package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository-Schnittstelle für den Zugriff auf Coin-Transaktions-Datenbankoperationen.
 */
@Repository
public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long> {
}