package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.Player;
import at.technikum.Invicrea2WebShopbackend.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService serviceUnderTest;

    @BeforeEach
    void setUp () {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    public void testGetAllPlayers () {
        // Arrange
        List<Player> players = Arrays.asList( new Player( ), new Player( ) );
        when( playerRepository.findAll( ) ).thenReturn( players );

        // Act
        List<Player> result = serviceUnderTest.getAllPlayers( );

        // Assert
        assertEquals( 2, result.size( ) );
        verify( playerRepository ).findAll( );
    }

    @Test
    public void testGetPlayerById () {
        // Arrange
        Player player = new Player( );
        player.setId( 1L );
        when( playerRepository.findById( 1L ) ).thenReturn( Optional.of( player ) );

        // Act
        Player result = serviceUnderTest.getPlayerById( 1L );

        // Assert
        assertEquals( 1L, result.getId( ) );
        verify( playerRepository ).findById( 1L );
    }

    @Test
    public void testGetPlayerById_NotFound () {
        // Arrange
        when( playerRepository.findById( anyLong( ) ) ).thenReturn( Optional.empty( ) );

        // Act
        Player result = serviceUnderTest.getPlayerById( 1L );

        // Assert
        assertNull( result );
        verify( playerRepository ).findById( 1L );
    }

    @Test
    public void testSavePlayer () {
        // Arrange
        Player player = new Player( );
        when( playerRepository.save( any( Player.class ) ) ).thenReturn( player );

        // Act
        Player result = serviceUnderTest.savePlayer( player );

        // Assert
        assertEquals( player, result );
        verify( playerRepository ).save( player );
    }

    @Test
    public void testUpdatePlayer () {
        // Arrange
        Player existingPlayer = new Player( );
        existingPlayer.setId( 1L );
        existingPlayer.setName( "Old Name" );

        Player updatedPlayer = new Player( );
        updatedPlayer.setName( "New Name" );

        when( playerRepository.findById( 1L ) ).thenReturn( Optional.of( existingPlayer ) );
        when( playerRepository.save( any( Player.class ) ) ).thenReturn( existingPlayer );

        // Act
        Player result = serviceUnderTest.updatePlayer( 1L, updatedPlayer );

        // Assert
        assertEquals( "New Name", result.getName( ) );
        verify( playerRepository ).findById( 1L );
        verify( playerRepository ).save( existingPlayer );
    }

    @Test
    public void testUpdatePlayer_NotFound () {
        // Arrange
        Player updatedPlayer = new Player( );
        updatedPlayer.setName( "New Name" );

        when( playerRepository.findById( anyLong( ) ) ).thenReturn( Optional.empty( ) );

        // Act
        Player result = serviceUnderTest.updatePlayer( 1L, updatedPlayer );

        // Assert
        assertNull( result );
        verify( playerRepository ).findById( 1L );
        verify( playerRepository, times( 0 ) ).save( any( Player.class ) );
    }

    @Test
    public void testDeletePlayer () {
        // Act
        serviceUnderTest.deletePlayer( 1L );

        // Assert
        verify( playerRepository ).deleteById( 1L );
    }

    @Test
    public void testGetPlayerCountByUserId () {
        // Arrange
        when( playerRepository.countPlayersByUserId( 1L ) ).thenReturn( 3 );

        // Act
        int result = serviceUnderTest.getPlayerCountByUserId( 1L );

        // Assert
        assertEquals( 3, result );
        verify( playerRepository ).countPlayersByUserId( 1L );
    }

    @Test
    public void testFindPlayersByName () {
        // Arrange
        List<Player> players = Arrays.asList( new Player( ), new Player( ) );
        when( playerRepository.findByName( eq( "Aden" ) ) ).thenReturn( players );

        // Act
        List<Player> result = serviceUnderTest.findPlayersByName( "Aden" );

        // Assert
        assertEquals( 2, result.size( ) );
        verify( playerRepository ).findByName( "Aden" );
    }
}
