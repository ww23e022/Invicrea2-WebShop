package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               ShoppingCartItemRepository shoppingCartItemRepository,
                               ItemRepository itemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.itemRepository = itemRepository;
    }

    public List<ShoppingCartItem> getItemsInCartByAccountId(Long accountId) {

        return null;
    }

    public void addItemToCart(Long accountId, Long itemId, int quantity) {

    }

    public void updateCartItem(Long shoppingCartId, Long itemId, int quantity) {

    }


    public void removeItemFromCart(Long shoppingCartId, Long itemId) {

    }

    public void clearCart(Long shoppingCartId) {
        shoppingCartItemRepository.deleteAllByShoppingCartId(shoppingCartId);
    }

    public ShoppingCart createShoppingCart(Long accountId) {

        return null;
    }
}