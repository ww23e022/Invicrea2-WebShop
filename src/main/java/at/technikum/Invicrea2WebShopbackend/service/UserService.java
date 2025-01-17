package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.dto.TokenResponse;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import at.technikum.Invicrea2WebShopbackend.security.jwt.JwtIssuer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtIssuer tokenIssuer;

    public List<User> getAllUsers () {
        return (List<User>) userRepository.findAll( );
    }


    public Optional<User> findByUsernameOrEmail ( String identifier ) {
        return userRepository.findByUsernameOrEmail( identifier, identifier );
    }

    public long getUserCount () {
        return userRepository.count( );
    }

    public boolean isValidUserId ( Long userId ) {
        // Überprüfen, ob die userId gültig ist
        return userRepository.existsById( userId );
    }

    public User getUserById ( Long userId ) {
        return userRepository.findById( userId )
                .orElseThrow( () -> new RuntimeException( "User not found" ) );
    }

    public User getUserProfile ( String username ) {
        User user = userRepository.findByUsername( username )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found" ) );
        return new User( user.getUsername( ), user.getEmail( ), user.getRole( ), user.getCoins( ) );
    }

    public TokenResponse updateUserProfile ( String username, User updatedUser ) {
        User user = userRepository.findByUsername( username )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found" ) );
        boolean shouldUpdateToken = false;
        if (updatedUser.getUsername( ) != null && !updatedUser.getUsername( )
                .equals( user.getUsername( ) )) {
            if (userRepository.findByUsername( updatedUser.getUsername( ) ).isPresent( )) {
                throw new IllegalArgumentException( "Username already exists" );
            }
            user.setUsername( updatedUser.getUsername( ) );
            shouldUpdateToken = true;
        }
        if (updatedUser.getEmail( ) != null && !updatedUser.getEmail( )
                .equals( user.getEmail( ) )) {
            if (userRepository.findByEmail( updatedUser.getEmail( ) ).isPresent( )) {
                throw new IllegalArgumentException( "Email already exists" );
            }
            user.setEmail( updatedUser.getEmail( ) );
            shouldUpdateToken = true;
        }
        userRepository.save( user );
        if (shouldUpdateToken) {
            // Neues Token generieren und zurückgeben
            String newToken = tokenIssuer.issue(
                    user.getId( ), user.getUsername( ), user.getRole( ) );
            return new TokenResponse( newToken );
        }
        return null; // Wenn keine Änderungen vorgenommen wurden, wird null zurückgegeben
    }

    // Aktualisierung des Benutzerprofils durch Admin
    public void updateUserProfileById ( Long userId, User updatedUser ) {
        User user = userRepository.findById( userId )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found" ) );
        // Überprüfen und Aktualisieren des Benutzernamens
        if (updatedUser.getUsername( ) != null && !updatedUser.getUsername( )
                .equals( user.getUsername( ) )) {
            if (userRepository.findByUsername( updatedUser.getUsername( ) ).isPresent( )) {
                throw new IllegalArgumentException( "Username already exists" );
            }
            user.setUsername( updatedUser.getUsername( ) );
        }
        // Überprüfen und Aktualisieren der E-Mail
        if (updatedUser.getEmail( ) != null && !updatedUser.getEmail( ).
                equals( user.getEmail( ) )) {
            if (userRepository.findByEmail( updatedUser.getEmail( ) ).isPresent( )) {
                throw new IllegalArgumentException( "Email already exists" );
            }
            user.setEmail( updatedUser.getEmail( ) );
        }
        // Status ändern, falls angegeben
        if (updatedUser.getStatus( ) != null && updatedUser.getStatus( ) != user.getStatus( )) {
            user.setStatus( updatedUser.getStatus( ) );
        }
        // Rolle ändern, falls angegeben
        if (updatedUser.getRole( ) != null && !updatedUser.getRole( ).equals( user.getRole( ) )) {
            user.setRole( updatedUser.getRole( ) );
        }
        userRepository.save( user );
    }
}