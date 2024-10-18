package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp () {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    public void testGetAllUsers () {
        // Arrange
        List<User> users = List.of( new User( ), new User( ) );
        when( userRepository.findAll( ) ).thenReturn( users );

        // Act
        List<User> result = userService.getAllUsers( );

        // Assert
        assertEquals( 2, result.size( ) );
        verify( userRepository, times( 1 ) ).findAll( );
    }

    @Test
    public void testFindByUsernameOrEmail () {
        // Arrange
        String identifier = "testuser";
        User user = new User( );
        when( userRepository.findByUsernameOrEmail( eq( identifier ), eq( identifier ) ) ).thenReturn( Optional.of( user ) );

        // Act
        Optional<User> result = userService.findByUsernameOrEmail( identifier );

        // Assert
        assertTrue( result.isPresent( ) );
        assertEquals( user, result.get( ) );
        verify( userRepository, times( 1 ) ).findByUsernameOrEmail( identifier, identifier );
    }

    @Test
    public void testGetUserCount () {
        // Arrange
        when( userRepository.count( ) ).thenReturn( 5L );

        // Act
        long result = userService.getUserCount( );

        // Assert
        assertEquals( 5L, result );
        verify( userRepository, times( 1 ) ).count( );
    }

    @Test
    public void testIsValidUserId () {
        // Arrange
        Long userId = 1L;
        when( userRepository.existsById( userId ) ).thenReturn( true );

        // Act
        boolean result = userService.isValidUserId( userId );

        // Assert
        assertTrue( result );
        verify( userRepository, times( 1 ) ).existsById( userId );
    }

    @Test
    public void testGetUserById_Found () {
        // Arrange
        Long userId = 1L;
        User user = new User( );
        when( userRepository.findById( userId ) ).thenReturn( Optional.of( user ) );

        // Act
        User result = userService.getUserById( userId );

        // Assert
        assertEquals( user, result );
        verify( userRepository, times( 1 ) ).findById( userId );
    }

    @Test
    public void testGetUserById_NotFound () {
        // Arrange
        Long userId = 1L;
        when( userRepository.findById( userId ) ).thenReturn( Optional.empty( ) );

        // Act & Assert
        assertThrows( RuntimeException.class, () -> userService.getUserById( userId ) );
        verify( userRepository, times( 1 ) ).findById( userId );
    }

    @Test
    public void testGetUserProfile_Found () {
        // Arrange
        String username = "testuser";
        User user = new User( );
        user.setUsername( username );
        user.setEmail( "test@example.com" );
        when( userRepository.findByUsername( username ) ).thenReturn( Optional.of( user ) );

        // Act
        User result = userService.getUserProfile( username );

        // Assert
        assertEquals( user.getUsername( ), result.getUsername( ) );
        assertEquals( user.getEmail( ), result.getEmail( ) );
        verify( userRepository, times( 1 ) ).findByUsername( username );
    }

    @Test
    public void testGetUserProfile_NotFound () {
        // Arrange
        String username = "testuser";
        when( userRepository.findByUsername( username ) ).thenReturn( Optional.empty( ) );

        // Act & Assert
        assertThrows( UsernameNotFoundException.class, () -> userService.getUserProfile( username ) );
        verify( userRepository, times( 1 ) ).findByUsername( username );
    }

    @Test
    public void testUpdateUserProfile_UsernameExists () {
        // Arrange
        String username = "oldUser";
        User user = new User( );
        user.setUsername( username );
        when( userRepository.findByUsername( username ) ).thenReturn( Optional.of( user ) );

        User updatedUser = new User( );
        updatedUser.setUsername( "newUser" );
        when( userRepository.findByUsername( "newUser" ) ).thenReturn( Optional.of( new User( ) ) );

        // Act & Assert
        assertThrows( IllegalArgumentException.class, () -> userService.updateUserProfile( username, updatedUser ) );
        verify( userRepository, times( 1 ) ).findByUsername( username );
        verify( userRepository, times( 1 ) ).findByUsername( "newUser" );
    }

    @Test
    public void testUpdateUserProfileById () {
        // Arrange
        Long userId = 1L;
        User user = new User( );
        user.setUsername( "testuser" );
        user.setEmail( "test@example.com" );
        when( userRepository.findById( userId ) ).thenReturn( Optional.of( user ) );

        User updatedUser = new User( );
        updatedUser.setUsername( "newuser" );
        updatedUser.setEmail( "new@example.com" );

        when( userRepository.findByUsername( "newuser" ) ).thenReturn( Optional.empty( ) );
        when( userRepository.findByEmail( "new@example.com" ) ).thenReturn( Optional.empty( ) );

        // Act
        userService.updateUserProfileById( userId, updatedUser );

        // Assert
        assertEquals( "newuser", user.getUsername( ) );
        assertEquals( "new@example.com", user.getEmail( ) );
        verify( userRepository, times( 1 ) ).save( user );
    }
}