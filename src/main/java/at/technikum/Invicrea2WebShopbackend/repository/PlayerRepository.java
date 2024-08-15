package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    /** Ruft die Anzahl der Spieler ab, die mit der angegebenen Kontonummer verknüpft sind. */
    @Query("SELECT COUNT(p) FROM Player p WHERE p.user.id = :userId")
    int countPlayersByUserId(@Param("userId") Long userId);

    /** Sucht Spieler anhand ihres Namens. */
    List<Player> findByName( String name);
}