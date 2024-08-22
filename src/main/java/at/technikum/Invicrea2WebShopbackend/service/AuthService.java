package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.dto.TokenRequest;
import at.technikum.Invicrea2WebShopbackend.dto.TokenResponse;
import at.technikum.Invicrea2WebShopbackend.dto.UserRegistrationRequest;
import at.technikum.Invicrea2WebShopbackend.model.Salutation;
import at.technikum.Invicrea2WebShopbackend.model.Status;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import at.technikum.Invicrea2WebShopbackend.security.TokenIssuer;
import at.technikum.Invicrea2WebShopbackend.security.user.UserPrincipal;
import at.technikum.Invicrea2WebShopbackend.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AuthService {

    private final TokenIssuer tokenIssuer;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public TokenResponse authenticate(TokenRequest tokenRequest) {
        Optional<User> optionalUser = userService.findByUsernameOrEmail(tokenRequest.getUsername());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid username or email");
        }

        User user = optionalUser.get();

        // Überprüfen, ob der Benutzer gesperrt ist
        if (user.getStatus() == Status.BANNED) {
            throw new RuntimeException("User is banned and cannot log in");
        }

        if (!passwordEncoder.matches(tokenRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        user.getUsername(), tokenRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String token = tokenIssuer.issue(principal.getId(),
                principal.getUsername(), principal.getRole());
        return new TokenResponse(token);
    }

/*    public TokenResponse authenticate(TokenRequest tokenRequest) {
        Optional<User> optionalUser = userService.findByUsernameOrEmail(tokenRequest.getUsername());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid username or email");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(tokenRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        user.getUsername(), tokenRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String token = tokenIssuer.issue(principal.getId(),
                principal.getUsername(), principal.getRole());
        return new TokenResponse(token);
    } */

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public TokenResponse register(UserRegistrationRequest registrationRequest) {
        validateRegistration(registrationRequest);

        User newUser = buildUserFromRequest(registrationRequest);

        User savedUser = userRepository.save(newUser);

        String token = tokenIssuer.issue(savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole().toString());

        return new TokenResponse(token);
    }

    private User buildUserFromRequest(UserRegistrationRequest registrationRequest) {
        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setRepeatPassword(passwordEncoder.encode(registrationRequest.getRepeatPassword()));
        newUser.setRole("ROLE_USER");
        newUser.setStatus(Status.ACTIVE);
        newUser.setCoins(0);
        newUser.setSalutation(registrationRequest.getSalutation());
        newUser.setCountry(registrationRequest.getCountry());

        // Set additionalInfo if salutation is OTHER
        if (registrationRequest.getSalutation() == Salutation.OTHER) {
            validateAdditionalInfo(registrationRequest.getAdditionalInfo());
            newUser.setAdditionalInfo(registrationRequest.getAdditionalInfo());
        }

        return newUser;
    }

    private void validateAdditionalInfo(String additionalInfo) {
        if (additionalInfo == null || additionalInfo.isEmpty()) {
            throw new RuntimeException("AdditionalInfo is required when Salutation is OTHER");
        }
    }

    private void validateRegistration(UserRegistrationRequest registrationRequest) {
        if (usernameExists(registrationRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (isEmail(registrationRequest.getUsername())) {
            throw new RuntimeException("Username cannot be an email address");
        }

        if (!isValidPassword(registrationRequest.getPassword())) {
            throw new RuntimeException("Invalid password format " +
                    "minimum 12 characters, including uppercase and " +
                    "lowercase letters, numbers, and symbols");
        }

        if (!registrationRequest.getPassword().equals(registrationRequest.getRepeatPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (!isValidEmail(registrationRequest.getEmail())) {
            throw new RuntimeException("Invalid email format");
        }

        if (emailExists(registrationRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
    }

    private boolean isEmail(String input) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 12) {
            return false;
        }

        if (!password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") ||
                !password.matches(".*\\d.*") || !password.matches(".*[@$!%*?&].*")) {
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
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}