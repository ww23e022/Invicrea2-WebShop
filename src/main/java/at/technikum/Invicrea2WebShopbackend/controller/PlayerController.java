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

    @GetMapping
    public List<PlayerDto> readAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return playerMapper.toDtos(players);
    }

    @GetMapping("/{id}")
    public PlayerDto readPlayer(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        return playerMapper.toDto(player);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerDto createPlayer(@RequestBody PlayerDto playerDto) {
        // Überprüfen, ob die account_id gültig ist
        if (accountService.isValidAccountId(playerDto.getAccountId())) {
            // Überprüfen, ob der Account bereits 4 Spieler hat
            if (playerService.getPlayerCountByAccountId(playerDto.getAccountId()) < 4) {
                // Das Account-Objekt für die gegebene account_id abrufen
                Account account = accountService.getAccountById(playerDto.getAccountId());
                // Spielerobjekt erstellen und dem Account zuweisen
                Player player = playerMapper.toEntity(playerDto);
                player.setAccount(account);
                player = playerService.savePlayer(player);
                return playerMapper.toDto(player);
            } else {
                throw new IllegalArgumentException("Ein Account kann maximal 4 Spieler erstellen.");
            }
        } else {
            throw new IllegalArgumentException("Ungültige account_id.");
        }
    }

    @PutMapping("/{id}")
    public PlayerDto updatePlayer(@PathVariable Long id, @RequestBody PlayerDto updatedPlayerDto) {
        // Überprüfen, ob die account_id gültig ist
        if (accountService.isValidAccountId(updatedPlayerDto.getAccountId())) {
            Player updatedPlayer = playerMapper.toEntity(updatedPlayerDto);
            PlayerDto resultDto = playerMapper.toDto(playerService.updatePlayer(id, updatedPlayer));
            resultDto.setId(id); // Behalte dieselbe ID wie angefordert bei
            return resultDto;
        } else {
            throw new IllegalArgumentException("Ungültige Account ID.");
        }
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    @GetMapping("/top5")
    public List<PlayerDto> getTop5PlayersByLevel() {
        List<Player> topPlayers = playerService.getAllPlayers().stream()
                .sorted(Comparator.comparingInt(Player::getLevel).reversed())
                .limit(5)
                .collect(Collectors.toList());
        return playerMapper.toDtos(topPlayers);
    }

    @GetMapping("/sorted")
    public List<PlayerDto> getAllPlayersSortedByLevel() {
        List<Player> sortedPlayers = playerService.getAllPlayers().stream()
                .sorted(Comparator.comparingInt(Player::getLevel).reversed())
                .collect(Collectors.toList());
        return playerMapper.toDtos(sortedPlayers);
    }

    @GetMapping("/search")
    public List<PlayerDto> searchPlayersByName(@RequestParam String name) {
        List<Player> foundPlayers = playerService.findPlayersByName(name);
        return playerMapper.toDtos(foundPlayers);
    }
}
