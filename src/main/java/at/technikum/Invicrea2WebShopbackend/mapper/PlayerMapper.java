package at.technikum.Invicrea2WebShopbackend.mapper;

import at.technikum.Invicrea2WebShopbackend.dto.PlayerDto;
import at.technikum.Invicrea2WebShopbackend.model.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for mapping between Player and PlayerDto objects.
 */
@Component
public class PlayerMapper {

    /**
     * Maps a Player object to a PlayerDto object.
     */
    public PlayerDto toDto( Player player) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setId(player.getId());
        playerDto.setName(player.getName());
        playerDto.setEmpire(player.getEmpire());
        playerDto.setLevel(player.getLevel());
        if (player.getAccount() != null) {
            playerDto.setAccountId(player.getAccount().getId());
        }
        return playerDto;
    }

    /**
     * Maps a PlayerDto object to a Player object.

     */
    public Player toEntity(PlayerDto playerDto) {
        Player player = new Player();
        player.setId(playerDto.getId());
        player.setName(playerDto.getName());
        player.setEmpire(playerDto.getEmpire());
        player.setLevel(playerDto.getLevel());
        // Account hier nicht setzen, da es separat behandelt wird
        return player;
    }

    /**
     * Maps a list of Player objects to a list of PlayerDto objects.
     *
     */
    public List<PlayerDto> toDtos(List<Player> players) {
        return players.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}