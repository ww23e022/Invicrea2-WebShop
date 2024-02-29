package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<ShoppingCart> getShoppingCartByAccountId(Long accountId) {
        Optional<ShoppingCart> optionalShoppingCart =
                shoppingCartRepository.
                        findByAccountId(accountId);

        if (optionalShoppingCart.isPresent()) {
            ShoppingCart shoppingCart = optionalShoppingCart.get();
            List<ShoppingCartItem> cartItems =
                    shoppingCartItemRepository.
                            findAllByShoppingCartId(shoppingCart.getId());

            // Iteriere durch die Warenkorbpositionen und
            // setze den Namen basierend auf dem Item-Objekt
            for (ShoppingCartItem item : cartItems) {
                item.setItemName(item.getItem().getName());
            }

            shoppingCart.setCartItems(cartItems);
            return Optional.of(shoppingCart);
        } else {
            return Optional.empty();
        }
    }


    public List<ShoppingCartItem> getItemsInCart(Long shoppingCartId) {
        List<ShoppingCartItem> cartItems =
                shoppingCartItemRepository.
                        findAllByShoppingCartId(shoppingCartId);
        // Iteriere durch die Warenkorbpositionen und
        // setze den Namen basierend auf dem Item-Objekt
        for (ShoppingCartItem item : cartItems) {
            item.setItemName(item.getItem().getName());
        }
        return cartItems;
    }


    public void addItemToCart(Long accountId, Long itemId, int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByAccountId(accountId)
                .orElseGet(() -> createShoppingCart(accountId));

        Optional<ShoppingCartItem> existingItem = shoppingCartItemRepository
                .findByShoppingCartIdAndItemId(shoppingCart.getId(), itemId);

        if (existingItem.isPresent()) {
            ShoppingCartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setItemName(item.getItem().getName());
            int totalPrice =
                    item.getItem().getPrice()
                            * item.getQuantity(); // Berechnung des Gesamtpreises
            item.setItemPrice(totalPrice); // Setzen des Gesamtpreises
            shoppingCartItemRepository.save(item);
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setShoppingCart(shoppingCart);
            newItem.setItem(itemRepository.getById(itemId));
            newItem.setQuantity(quantity);
            newItem.setItemName(newItem.getItem().getName());
            int totalPrice =
                    newItem.getItem().getPrice() *
                            newItem.getQuantity(); // Berechnung des Gesamtpreises
            newItem.setItemPrice(totalPrice); // Setzen des Gesamtpreises
            shoppingCartItemRepository.save(newItem);
        }
    }

    public void updateCartItem(Long shoppingCartId, Long itemId, int quantity) {
        Optional<ShoppingCartItem> existingItem = shoppingCartItemRepository
                .findByShoppingCartIdAndItemId(shoppingCartId, itemId);

        existingItem.ifPresent(item -> {
            item.setQuantity(quantity);
            int totalPrice = item.getItem().getPrice() * quantity;
            item.setItemPrice(totalPrice);
            shoppingCartItemRepository.save(item);
        });
    }


    public void removeItemFromCart(Long shoppingCartId, Long itemId) {
        Optional<ShoppingCartItem> existingItem = shoppingCartItemRepository
                .findByShoppingCartIdAndItemId(shoppingCartId, itemId);

        existingItem.ifPresent(shoppingCartItemRepository::delete);
    }

    public void clearCart(Long shoppingCartId) {
        shoppingCartItemRepository.deleteAllByShoppingCartId(shoppingCartId);
    }

    public ShoppingCart createShoppingCart(Long accountId) {
        ShoppingCart cart = new ShoppingCart();
        Account account = new Account();
        account.setId(accountId);
        cart.setAccount(account);
        return shoppingCartRepository.save(cart);
    }
}