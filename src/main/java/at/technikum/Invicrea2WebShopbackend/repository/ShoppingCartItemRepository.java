package at.technikum.Invicrea2WebShopbackend.repository;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

    Optional<ShoppingCartItem> findByShoppingCartIdAndItemId( Long shoppingCartId, Long itemId);

    List<ShoppingCartItem> findAllByShoppingCartId(Long shoppingCartId);

    void deleteAllByShoppingCartId(Long shoppingCartId);
}