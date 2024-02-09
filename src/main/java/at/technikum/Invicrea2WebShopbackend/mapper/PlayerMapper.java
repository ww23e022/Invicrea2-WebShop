package at.technikum.Invicrea2WebShopbackend.mapper;

import at.technikum.Invicrea2WebShopbackend.dto.PlayerDto;
import at.technikum.Invicrea2WebShopbackend.model.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlayerMapper {

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

    public Player toEntity(PlayerDto playerDto) {
        Player player = new Player();
        player.setId(playerDto.getId());
        player.setName(playerDto.getName());
        player.setEmpire(playerDto.getEmpire());
        player.setLevel(playerDto.getLevel());
        // Account hier nicht setzen, da es separat behandelt wird
        return player;
    }

    public List<PlayerDto> toDtos(List<Player> players) {
        return players.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Player> toEntities( List<PlayerDto> playerDtos) {
        return playerDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
