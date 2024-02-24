package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public ShoppingCart createShoppingCart(Account account) {
        ShoppingCart cart = new ShoppingCart();
        cart.setAccount(account);
        return shoppingCartRepository.save(cart);
    }

    public Optional<ShoppingCart> getShoppingCartByAccountId(Long accountId) {
        return shoppingCartRepository.findByAccountId(accountId);
    }

    public List<ShoppingCartItem> getItemsInCart(Long shoppingCartId) {
        return shoppingCartItemRepository.findAllByShoppingCartId(shoppingCartId);
    }

    public void addItemToCart(Long shoppingCartId, Long itemId, int quantity) {
        Optional<ShoppingCartItem> existingItem = shoppingCartItemRepository
                .findByShoppingCartIdAndItemId(shoppingCartId, itemId);

        if (existingItem.isPresent()) {
            ShoppingCartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            shoppingCartItemRepository.save(item);
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setShoppingCart(shoppingCartRepository.getById(shoppingCartId));
            newItem.setItem(itemRepository.getById(itemId));
            newItem.setQuantity(quantity);
            shoppingCartItemRepository.save(newItem);
        }
    }

    public void removeItemFromCart(Long shoppingCartId, Long itemId) {
        Optional<ShoppingCartItem> existingItem = shoppingCartItemRepository
                .findByShoppingCartIdAndItemId(shoppingCartId, itemId);

        existingItem.ifPresent(shoppingCartItemRepository::delete);
    }

    public void clearCart(Long shoppingCartId) {
        shoppingCartItemRepository.deleteAllByShoppingCartId(shoppingCartId);
    }
}