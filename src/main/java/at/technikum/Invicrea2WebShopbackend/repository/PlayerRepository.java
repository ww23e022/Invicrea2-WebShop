package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing database operations related to players.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    /**
     * Retrieves the count of players associated with the given account ID.
     *
     * @param accountId the ID of the account
     * @return the count of players associated with the account
     */
    @Query("SELECT COUNT(p) FROM Player p WHERE p.account.id = :accountId")
    int countPlayersByAccountId(@Param("accountId") Long accountId);

    /**
     * Finds players by their name.
     *
     * @param name the name of the player
     * @return a list of players with the specified name
     */
    List<Player> findByName( String name);
}