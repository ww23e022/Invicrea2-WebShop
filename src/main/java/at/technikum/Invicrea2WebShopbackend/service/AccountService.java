package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.dto.AccountDto;
import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.Salutation;
import at.technikum.Invicrea2WebShopbackend.model.Status;
import at.technikum.Invicrea2WebShopbackend.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$";

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account registerAccount(AccountDto accountDto, Salutation salutation) {
        validateRegistration(accountDto);

        Account account = new Account();
        account.setSalutation(salutation);

        // Hier können Sie additionalInfo in der Datenbank speichern,
        // falls "Other" ausgewählt wurde.
        if (salutation == Salutation.OTHER) {
            account.setAdditionalInfo(accountDto.getAdditionalInfo());
        }

        // Weitere Felder hinzufügen
        account.setEmail(accountDto.getEmail());
        account.setUsername(accountDto.getUsername());
        account.setPassword(accountDto.getPassword());
        account.setCountry(accountDto.getCountry());
        account.setRole(accountDto.getRole());
        account.setStatus(Status.ACTIVE);

        return accountRepository.save(account);
    }

    public Account updateAccount(Long accountId, AccountDto accountDto) {
        validateUpdate(accountDto);

        Account existingAccount = getAccountById(accountId);

        Salutation salutation;
        if (accountDto.getSalutation().equals("OTHER")) {
            // Hier sollten Sie sicherstellen, dass additionalInfo nicht null ist.
            String additionalInfo = accountDto.getAdditionalInfo();
            // Hier können Sie die additionalInfo in der Datenbank speichern.
            salutation = Salutation.OTHER;
            existingAccount.setAdditionalInfo(additionalInfo);
        } else {
            try {
                salutation = Salutation.valueOf(accountDto.getSalutation());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid salutation value");
            }
        }

        existingAccount.setSalutation(salutation);
        existingAccount.setEmail(accountDto.getEmail());
        existingAccount.setUsername(accountDto.getUsername());
        existingAccount.setCountry(accountDto.getCountry());
        existingAccount.setRole(accountDto.getRole());

        return accountRepository.save(existingAccount);
    }

    public void banAccount(Long accountId) {
        Account account = getAccountById(accountId);
        account.setStatus(Status.BANNED);
        accountRepository.save(account);
    }

    public void unbanAccount(Long accountId) {
        Account account = getAccountById(accountId);
        account.setStatus(Status.ACTIVE);
        accountRepository.save(account);
    }

    public void deleteAccountById(Long accountId) {
        Account account = getAccountById(accountId);
        accountRepository.delete(account);
    }

    private void validateRegistration(AccountDto accountDto) {
        if (usernameExists(accountDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (isEmail(accountDto.getUsername())) {
            throw new RuntimeException("Username cannot be an email address");
        }

        if (!isValidPassword(accountDto.getPassword())) {
            throw new RuntimeException("Invalid password format " +
                    "minimum 12 characters, including uppercase and " +
                    "lowercase letters, numbers, and symbols");
        }

        if (!accountDto.getPassword().equals(accountDto.getRepeatPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (!isValidEmail(accountDto.getEmail())) {
            throw new RuntimeException("Invalid email format");
        }

        if (emailExists(accountDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
    }

    private void validateUpdate(AccountDto accountDto) {
        // Implement validation logic for account update
        // For example, check for unique email, username, etc.

        if (usernameExists(accountDto.getUsername(), accountDto.getId())) {
            throw new RuntimeException("Username already exists");
        }

        if (isEmail(accountDto.getUsername())) {
            throw new RuntimeException("Username cannot be an email address");
        }

        if (!isValidPassword(accountDto.getPassword())) {
            throw new RuntimeException("Invalid password format " +
                    "minimum 12 characters, including uppercase and " +
                    "lowercase letters, numbers, and symbols");
        }

        if (!accountDto.getPassword().equals(accountDto.getRepeatPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (!isValidEmail(accountDto.getEmail())) {
            throw new RuntimeException("Invalid email format");
        }

        if (emailExists(accountDto.getEmail(), accountDto.getId())) {
            throw new RuntimeException("Email already exists");
        }
    }

    private boolean isEmail(String input) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        // Check length
        if (password.length() < 12) {
            return false;
        }

        // Check for at least one lowercase letter,
        // one uppercase letter, one digit, and one special character
        if (!password.matches(".*[a-z].*")
                || !password.matches(".*[A-Z].*")
                || !password.matches(".*\\d.*")
                || !password.matches(".*[@$!%*?&].*")) {
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean usernameExists(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }

    private boolean emailExists(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    private boolean usernameExists(String username, Long accountId) {
        return accountRepository.findByUsernameAndIdNot(username, accountId).isPresent();
    }

    private boolean emailExists(String email, Long accountId) {
        return accountRepository.findByEmailAndIdNot(email, accountId).isPresent();
    }

    public boolean authenticate(String username, String password) {
        // Implementieren Sie die Logik zur Überprüfung der Anmeldeinformationen
        // Beispiel: Überprüfen Sie Benutzername und Passwort in der Datenbank
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(
                        "Account not found for username: " + username));

        return account.getPassword().equals(password);
    }

}
