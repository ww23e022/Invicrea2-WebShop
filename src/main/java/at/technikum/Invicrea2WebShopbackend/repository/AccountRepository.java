package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername( String username);
    Optional<Account> findByEmail(String email);

}