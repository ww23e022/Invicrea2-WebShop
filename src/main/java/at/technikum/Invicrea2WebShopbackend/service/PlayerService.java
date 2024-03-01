package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.Player;
import at.technikum.Invicrea2WebShopbackend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(Long id) {

        return null;
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Long id, Player updatedPlayer) {

        return updatedPlayer;
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    public int getPlayerCountByAccountId(Long accountId) {
        return playerRepository.countPlayersByAccountId(accountId);
    }

    public List<Player> findPlayersByName(String name) {
        return playerRepository.findByName(name);
    }
}
