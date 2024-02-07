package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.PlayerDto;
import at.technikum.Invicrea2WebShopbackend.model.Player;
import at.technikum.Invicrea2WebShopbackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/players")
@CrossOrigin
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<PlayerDto> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return players.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/sorted")
    public List<PlayerDto> getAllPlayersSortedByLevel() {
        List<Player> players = playerService.getAllPlayersSortedByLevel();
        return players.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PlayerDto getPlayerById(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        return convertToDto(player);
    }

    @PostMapping
    public PlayerDto savePlayer(@RequestBody PlayerDto playerDto) {
        Player player = convertToEntity(playerDto);
        Player savedPlayer = playerService.savePlayer(player);
        return convertToDto(savedPlayer);
    }

    @PutMapping("/{id}")
    public PlayerDto updatePlayer(@PathVariable Long id, @RequestBody PlayerDto updatedPlayerDto) {
        Player updatedPlayer = convertToEntity(updatedPlayerDto);
        PlayerDto resultDto = convertToDto(playerService.updatePlayer(id, updatedPlayer));
        resultDto.setId(id); // Keep the same ID as requested
        return resultDto;
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    // Helper method to convert Player to PlayerDto
    private PlayerDto convertToDto(Player player) {
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

    // Helper method to convert PlayerDto to Player entity
    private Player convertToEntity(PlayerDto playerDto) {
        Player player = new Player();
        player.setId(playerDto.getId());
        player.setName(playerDto.getName());
        player.setEmpire(playerDto.getEmpire());
        player.setLevel(playerDto.getLevel());
        // Do not set the Account here, as it's handled separately
        return player;
    }
}
