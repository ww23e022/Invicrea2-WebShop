package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.PlayerDto;
import at.technikum.Invicrea2WebShopbackend.mapper.PlayerMapper;
import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.Player;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/players")
@CrossOrigin
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final AccountService accountService;

    @Autowired
    public PlayerController(PlayerService playerService,
                            PlayerMapper playerMapper,
                            AccountService accountService) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
        this.accountService = accountService;
    }

    // GET requests on "/players" returns all players
    @GetMapping
    public List<PlayerDto> readAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return playerMapper.toDtos(players);
    }

    // GET requests on "/players/{id}" returns a player by ID
    @GetMapping("/{id}")
    public PlayerDto readPlayer(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        return playerMapper.toDto(player);
    }

    // POST requests on "/players" creates a new player
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerDto createPlayer(@RequestBody PlayerDto playerDto) {
        // Check if the account_id is valid
        if (accountService.isValidAccountId(playerDto.getAccountId())) {
            // Check if the account already has 4 players
            if (playerService.getPlayerCountByAccountId(playerDto.getAccountId()) < 4) {
                // Get the Account object for the given account_id
                Account account = accountService.getAccountById(playerDto.getAccountId());
                // Create a player object and assign it to the account
                Player player = playerMapper.toEntity(playerDto);
                player.setAccount(account);
                player = playerService.savePlayer(player);
                return playerMapper.toDto(player);
            } else {
                throw new IllegalArgumentException("An account can create a maximum of 4 players.");
            }
        } else {
            throw new IllegalArgumentException("Invalid account_id.");
        }
    }

    // PUT requests on "/players/{id}" updates a player
    @PutMapping("/{id}")
    public PlayerDto updatePlayer(@PathVariable Long id,
                                  @RequestBody PlayerDto updatedPlayerDto) {
        // Check if the account_id is valid
        if (accountService.isValidAccountId(updatedPlayerDto.getAccountId())) {
            Player updatedPlayer = playerMapper.toEntity(updatedPlayerDto);
            PlayerDto resultDto = playerMapper.toDto(playerService.updatePlayer(id, updatedPlayer));
            resultDto.setId(id); // Keep the same ID as requested
            return resultDto;
        } else {
            throw new IllegalArgumentException("Invalid Account ID.");
        }
    }

    // DELETE requests on "/players/{id}" deletes a player
    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    // GET requests on "/players/top5" returns the top 5 players by level
    @GetMapping("/top5")
    public List<PlayerDto> getTop5PlayersByLevel() {
        List<Player> topPlayers = playerService.getAllPlayers().stream()
                .sorted(Comparator.comparingInt(Player::getLevel).reversed())
                .limit(5)
                .collect(Collectors.toList());
        return playerMapper.toDtos(topPlayers);
    }

    // GET requests on "/players/sorted" returns all players sorted by level
    @GetMapping("/sorted")
    public List<PlayerDto> getAllPlayersSortedByLevel() {
        List<Player> sortedPlayers = playerService.getAllPlayers().stream()
                .sorted(Comparator.comparingInt(Player::getLevel).reversed())
                .collect(Collectors.toList());
        return playerMapper.toDtos(sortedPlayers);
    }

    // Handler for GET requests on "/players/search" searches players by name
    @GetMapping("/search")
    public List<PlayerDto> searchPlayersByName(@RequestParam String name) {
        List<Player> foundPlayers = playerService.findPlayersByName(name);
        return playerMapper.toDtos(foundPlayers);
    }
}