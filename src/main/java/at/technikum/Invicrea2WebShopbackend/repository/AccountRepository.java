package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    /** Sucht ein Konto anhand des Benutzernamens. */
    Optional<Account> findByUsername( String username);

    /** Sucht ein Konto anhand der E-Mail-Adresse. */
    Optional<Account> findByEmail(String email);
}