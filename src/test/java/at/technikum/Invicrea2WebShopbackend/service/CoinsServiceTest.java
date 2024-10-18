package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.CoinTransaction;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.repository.CoinTransactionRepository;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CoinsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CoinTransactionRepository coinTransactionRepository;

    @InjectMocks
    private CoinsService coinsService;

    @BeforeEach
    void setUp () {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    public void testAddCoinsToUser () {
        // Arrange
        User user = new User( );
        user.setCoins( 100 );
        int coinsToAdd = 50;

        when( userRepository.save( any( User.class ) ) ).thenReturn( user );

        // Act
        coinsService.addCoinsToUser( user, coinsToAdd );

        // Assert
        assertEquals( 150, user.getCoins( ) );
        verify( userRepository ).save( user );
        verify( coinTransactionRepository ).save( any( CoinTransaction.class ) );
    }

    @Test
    public void testSubtractCoinsFromUser () {
        // Arrange
        User user = new User( );
        user.setCoins( 100 );
        int coinsToSubtract = 30;

        when( userRepository.save( any( User.class ) ) ).thenReturn( user );

        // Act
        coinsService.subtractCoinsFromUser( user, coinsToSubtract );

        // Assert
        assertEquals( 70, user.getCoins( ) );
        verify( userRepository ).save( user );
        verify( coinTransactionRepository ).save( any( CoinTransaction.class ) );
    }

    @Test
    public void testSubtractCoinsFromUser_InsufficientCoins () {
        // Arrange
        User user = new User( );
        user.setCoins( 20 );
        int coinsToSubtract = 50;

        // Act & Assert
        assertThrows( IllegalArgumentException.class, () -> {
            coinsService.subtractCoinsFromUser( user, coinsToSubtract );
        } );

        verify( userRepository, never( ) ).save( any( User.class ) );
        verify( coinTransactionRepository, never( ) ).save( any( CoinTransaction.class ) );
    }

    @Test
    public void testGetCoinsInUser () {
        // Arrange
        User user = new User( );
        user.setCoins( 200 );

        // Act
        int result = coinsService.getCoinsInUser( user );

        // Assert
        assertEquals( 200, result );
    }
}