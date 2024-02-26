package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing database operations related to account objects.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds an account by username.
     *
     * @param username the username of the account
     * @return an Optional containing an Account object if found, otherwise null
     */
    Optional<Account> findByUsername( String username);

    /**
     * Finds an account by email address.
     *
     * @param email the email address of the account
     * @return an Optional containing an Account object if found, otherwise null
     */
    Optional<Account> findByEmail(String email);
}