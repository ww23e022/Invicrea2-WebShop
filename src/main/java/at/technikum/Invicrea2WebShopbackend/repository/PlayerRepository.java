package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("SELECT COUNT(p) FROM Player p WHERE p.account.id = :accountId")
    int countPlayersByAccountId(@Param("accountId") Long accountId);
}
