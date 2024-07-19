package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.PlayerDto;
import at.technikum.Invicrea2WebShopbackend.mapper.PlayerMapper;
import at.technikum.Invicrea2WebShopbackend.model.Player;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.service.PlayerService;
import at.technikum.Invicrea2WebShopbackend.service.UserService;
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
    private final UserService userService;

    @Autowired
    public PlayerController(PlayerService playerService,
                            PlayerMapper playerMapper,
                            UserService userService) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
        this.userService = userService;
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
        // Check if the user_id is valid
        if (userService.isValidUserId(playerDto.getUserId())) {
            // Check if the user already has 4 players
            if (playerService.getPlayerCountByUserId(playerDto.getUserId()) < 4) {
                // Get the User object for the given user_id
                User user = userService.getUserById(playerDto.getUserId());
                // Create a player object and assign it to the user
                Player player = playerMapper.toEntity(playerDto);
                player.setUser(user);
                player = playerService.savePlayer(player);
                return playerMapper.toDto(player);
            } else {
                throw new IllegalArgumentException("An User can create a maximum of 4 players.");
            }
        } else {
            throw new IllegalArgumentException("Invalid user_id.");
        }
    }

    // PUT requests on "/players/{id}" updates a player
    @PutMapping("/{id}")
    public PlayerDto updatePlayer(@PathVariable Long id,
                                  @RequestBody PlayerDto updatedPlayerDto) {
        // Check if the user_id is valid
        if (userService.isValidUserId(updatedPlayerDto.getUserId())) {
            Player updatedPlayer = playerMapper.toEntity(updatedPlayerDto);
            PlayerDto resultDto = playerMapper.toDto(playerService.updatePlayer(id, updatedPlayer));
            resultDto.setId(id); // Keep the same ID as requested
            return resultDto;
        } else {
            throw new IllegalArgumentException("Invalid User ID.");
        }
    }

    // DELETE requests on "/players/{id}" deletes a player
    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    // GET requests on "/players?level=top5" returns the top 5 players by level
    @GetMapping(params = "level=top5")
    public List<PlayerDto> getTop5PlayersByLevel() {
        List<Player> topPlayers = playerService.getAllPlayers().stream()
                .sorted(Comparator.comparingInt(Player::getLevel).reversed())
                .limit(5)
                .collect(Collectors.toList());
        return playerMapper.toDtos(topPlayers);
    }

    // GET requests on "/players?level=sorted" returns all players sorted by level
    @GetMapping(params = "level=sorted")
    public List<PlayerDto> getAllPlayersSortedByLevel() {
        List<Player> sortedPlayers = playerService.getAllPlayers().stream()
                .sorted(Comparator.comparingInt(Player::getLevel).reversed())
                .collect(Collectors.toList());
        return playerMapper.toDtos(sortedPlayers);
    }

    // GET requests on "/players?search=name" searches players by name
    @GetMapping(params = "search=name")
    public List<PlayerDto> searchPlayersByName(@RequestParam String name) {
        List<Player> foundPlayers = playerService.findPlayersByName(name);
        return playerMapper.toDtos(foundPlayers);
    }
}
