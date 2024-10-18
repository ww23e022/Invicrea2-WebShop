package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.PlayerDto;
import at.technikum.Invicrea2WebShopbackend.mapper.PlayerMapper;
import at.technikum.Invicrea2WebShopbackend.model.Player;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.security.principal.UserPrincipal;
import at.technikum.Invicrea2WebShopbackend.service.PlayerService;
import at.technikum.Invicrea2WebShopbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping
    public List<PlayerDto> readAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return playerMapper.toDtos(players);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.Player', 'read')")
    public PlayerDto readPlayer(@PathVariable Long id,
                                @AuthenticationPrincipal UserPrincipal principal) {
        Player player = playerService.getPlayerById(id);
        return playerMapper.toDto(player);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerDto createPlayer(@RequestBody PlayerDto playerDto,
                                  @AuthenticationPrincipal UserPrincipal principal) {
        if (userService.isValidUserId(principal.getId())) {
            if (playerService.getPlayerCountByUserId(principal.getId()) < 4) {
                User user = userService.getUserById(principal.getId());
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

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.Player', 'write')")
    public PlayerDto updatePlayer(@PathVariable Long id,
                                  @RequestBody PlayerDto updatedPlayerDto,
                                  @AuthenticationPrincipal UserPrincipal principal) {
        if (userService.isValidUserId(principal.getId())) {
            Player updatedPlayer = playerMapper.toEntity(updatedPlayerDto);
            PlayerDto resultDto = playerMapper.toDto(playerService.updatePlayer(id, updatedPlayer));
            resultDto.setId(id);
            return resultDto;
        } else {
            throw new IllegalArgumentException("Invalid User ID.");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.Player', 'delete')")
    public void deletePlayer(@PathVariable Long id,
                             @AuthenticationPrincipal UserPrincipal principal) {
        playerService.deletePlayer(id);
    }

    @GetMapping(params = "level=top5")
    public List<PlayerDto> getTop5PlayersByLevel() {
        List<Player> topPlayers = playerService.getAllPlayers().stream()
                .sorted(Comparator.comparingInt(Player::getLevel).reversed())
                .limit(5)
                .collect(Collectors.toList());
        return playerMapper.toDtos(topPlayers);
    }

    @GetMapping(params = "level=sorted")
    public List<PlayerDto> getAllPlayersSortedByLevel() {
        List<Player> sortedPlayers = playerService.getAllPlayers().stream()
                .sorted(Comparator.comparingInt(Player::getLevel).reversed())
                .collect(Collectors.toList());
        return playerMapper.toDtos(sortedPlayers);
    }

    @GetMapping(params = "search")
    public List<PlayerDto> searchPlayersByName(@RequestParam String name) {
        List<Player> foundPlayers = playerService.findPlayersByName(name);
        return playerMapper.toDtos(foundPlayers);
    }
}
