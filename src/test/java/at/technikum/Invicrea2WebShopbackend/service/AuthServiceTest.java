package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.dto.TokenRequest;
import at.technikum.Invicrea2WebShopbackend.dto.TokenResponse;
import at.technikum.Invicrea2WebShopbackend.dto.UserRegistrationRequest;
import at.technikum.Invicrea2WebShopbackend.model.Salutation;
import at.technikum.Invicrea2WebShopbackend.model.Status;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import at.technikum.Invicrea2WebShopbackend.security.TokenIssuer;
import at.technikum.Invicrea2WebShopbackend.security.principal.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private TokenIssuer tokenIssuer;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp () {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    public void testAuthenticate_Success () {
        // Arrange
        // Stellen Sie sicher, dass der Benutzername und das Passwort korrekt zugewiesen sind
        TokenRequest tokenRequest = new TokenRequest( "username", "password" );

        User user = new User( );
        user.setUsername( "username" );
        user.setPassword( "encodedPassword" );
        user.setStatus( Status.ACTIVE );

        // Mock das Verhalten des userService, um den Benutzer zurückzugeben
        when( userService.findByUsernameOrEmail( "username" ) ).thenReturn( Optional.of( user ) );

        // Mock das Verhalten des passwordEncoders
        when( passwordEncoder.matches( "password", "encodedPassword" ) ).thenReturn( true );

        // Mock der Authentifizierung
        Authentication authentication = mock( Authentication.class );
        UserPrincipal principal = mock( UserPrincipal.class );

        // Mocking von Principal-Details
        when( authentication.getPrincipal( ) ).thenReturn( principal );
        when( principal.getId( ) ).thenReturn( 1L );
        when( principal.getUsername( ) ).thenReturn( "username" );
        when( principal.getRole( ) ).thenReturn( "ROLE_USER" );

        // Mock Authentifizierungsmanager
        when( authenticationManager.authenticate( any( UsernamePasswordAuthenticationToken.class ) ) )
                .thenReturn( authentication );

        // Mock des Token-Issuers
        when( tokenIssuer.issue( 1L, "username", "ROLE_USER" ) ).thenReturn( "mockedToken" );

        // Act
        TokenResponse response = authService.authenticate( tokenRequest );

        // Assert
        assertEquals( "mockedToken", response.getToken( ) );
        verify( authenticationManager, times( 1 ) ).authenticate( any( UsernamePasswordAuthenticationToken.class ) );
        verify( tokenIssuer, times( 1 ) ).issue( 1L, "username", "ROLE_USER" );
    }

    @Test
    public void testAuthenticate_UserNotFound () {
        // Arrange
        TokenRequest tokenRequest = new TokenRequest( "unknownUser", "password" );
        when( userService.findByUsernameOrEmail( "unknownUser" ) ).thenReturn( Optional.empty( ) );

        // Act & Assert
        assertThrows( RuntimeException.class, () -> authService.authenticate( tokenRequest ),
                "Invalid username or email" );
    }

    @Test
    public void testAuthenticate_InvalidPassword () {
        // Arrange
        TokenRequest tokenRequest = new TokenRequest( "username", "wrongPassword" );
        User user = new User( );
        user.setUsername( "username" );
        user.setPassword( "encodedPassword" );
        user.setStatus( Status.ACTIVE );

        when( userService.findByUsernameOrEmail( "username" ) ).thenReturn( Optional.of( user ) );
        when( passwordEncoder.matches( "wrongPassword", "encodedPassword" ) ).thenReturn( false );

        // Act & Assert
        assertThrows( RuntimeException.class, () -> authService.authenticate( tokenRequest ),
                "Invalid password" );
    }

    @Test
    public void testAuthenticate_UserBanned () {
        // Arrange
        TokenRequest tokenRequest = new TokenRequest( "username", "password" );
        User user = new User( );
        user.setUsername( "username" );
        user.setPassword( "encodedPassword" );
        user.setStatus( Status.BANNED );

        when( userService.findByUsernameOrEmail( "username" ) ).thenReturn( Optional.of( user ) );

        // Act & Assert
        assertThrows( RuntimeException.class, () -> authService.authenticate( tokenRequest ),
                "User is banned and cannot log in" );
    }

    @Test
    public void testRegister_Success () {
        // Arrange
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest( );
        registrationRequest.setUsername( "newUser" );
        registrationRequest.setEmail( "user@example.com" );
        registrationRequest.setPassword( "SecurePassword1!" );
        registrationRequest.setRepeatPassword( "SecurePassword1!" );
        registrationRequest.setSalutation( Salutation.MALE );
        registrationRequest.setCountry( "Austria" );

        User savedUser = new User( );
        savedUser.setId( 1L );
        savedUser.setUsername( "newUser" );
        savedUser.setEmail( "user@example.com" );
        savedUser.setPassword( "encodedPassword" );
        savedUser.setStatus( Status.ACTIVE );
        savedUser.setRole( "ROLE_USER" );

        when( userRepository.findByUsername( "newUser" ) ).thenReturn( Optional.empty( ) );
        when( userRepository.findByEmail( "user@example.com" ) ).thenReturn( Optional.empty( ) );
        when( passwordEncoder.encode( anyString( ) ) ).thenReturn( "encodedPassword" );
        when( userRepository.save( any( User.class ) ) ).thenReturn( savedUser );
        when( tokenIssuer.issue( 1L, "newUser", "ROLE_USER" ) ).thenReturn( "mockedToken" );

        // Act
        TokenResponse response = authService.register( registrationRequest );

        // Assert
        assertEquals( "mockedToken", response.getToken( ) );
        verify( userRepository, times( 1 ) ).save( any( User.class ) );
        verify( tokenIssuer, times( 1 ) ).issue( 1L, "newUser", "ROLE_USER" );
    }

    @Test
    public void testRegister_UsernameExists () {
        // Arrange
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest( );
        registrationRequest.setUsername( "existingUser" );

        when( userRepository.findByUsername( "existingUser" ) ).thenReturn( Optional.of( new User( ) ) );

        // Act & Assert
        assertThrows( RuntimeException.class, () -> authService.register( registrationRequest ),
                "Username already exists" );
    }

    @Test
    public void testRegister_EmailAlreadyExists () {
        // Arrange
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest( );
        registrationRequest.setUsername( "newUser" );
        registrationRequest.setEmail( "existing@example.com" );

        when( userRepository.findByEmail( "existing@example.com" ) ).thenReturn( Optional.of( new User( ) ) );

        // Act & Assert
        assertThrows( RuntimeException.class, () -> authService.register( registrationRequest ),
                "Email already exists" );
    }

    @Test
    public void testRegister_PasswordsDoNotMatch () {
        // Arrange
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest( );
        registrationRequest.setPassword( "Password1!" );
        registrationRequest.setRepeatPassword( "Password2!" );

        // Act & Assert
        assertThrows( RuntimeException.class, () -> authService.register( registrationRequest ),
                "Passwords do not match" );
    }
}
