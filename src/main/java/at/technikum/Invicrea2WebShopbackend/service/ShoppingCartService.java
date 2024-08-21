package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.Item;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartRepository;
import at.technikum.Invicrea2WebShopbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               ShoppingCartItemRepository shoppingCartItemRepository,
                               ItemRepository itemRepository,
                               UserRepository userRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<ShoppingCartItem> getItemsInCartByShoppingCartId(Long userId) {
        ShoppingCart cart = getOrCreateShoppingCart(userId);
        List<ShoppingCartItem> cartItems = shoppingCartItemRepository
                .findAllByShoppingCartId(cart.getId());

        List<ShoppingCartItem> cartItemInfos = new ArrayList<>();
        int totalPrice = 0;

        for (ShoppingCartItem cartItem : cartItems) {
            // Hier sicherstellen, dass der Name und der Preis aktualisiert werden
            Item item = cartItem.getItem();
            cartItem.setItemName(item.getName());
            cartItem.setItemPrice(item.getPrice() * cartItem.getQuantity());
            totalPrice += cartItem.getItemPrice();
            cartItemInfos.add(cartItem);
        }

        // Optional: Gesamtpreis hinzufügen
        ShoppingCartItem totalItem = new ShoppingCartItem();
        totalItem.setItemName("Total Price");
        totalItem.setItemPrice(totalPrice);
        cartItemInfos.add(totalItem);

        return cartItemInfos;
    }

    public void addItemToCart(Long userId, Long itemId, int quantity) {
        ShoppingCart cart = getOrCreateShoppingCart(userId);
        List<ShoppingCartItem> existingItems = shoppingCartItemRepository
                .findAllByShoppingCartIdAndItemId(cart.getId(), itemId);

        if (!existingItems.isEmpty()) {
            // Aktualisiere das vorhandene Item
            ShoppingCartItem item = existingItems.get(0);
            item.setQuantity(item.getQuantity() + quantity);
            item.setItemName(item.getItem().getName());
            int totalPrice = item.getItem().getPrice() * item.getQuantity();
            item.setItemPrice(totalPrice);
            shoppingCartItemRepository.save(item);
        } else {
            // Neues Item zum Warenkorb hinzufügen
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setShoppingCart(cart);
            newItem.setItem(itemRepository.findById(itemId).orElseThrow(()
                    -> new RuntimeException("Item not found")));
            newItem.setQuantity(quantity);
            newItem.setItemName(newItem.getItem().getName());
            int totalPrice = newItem.getItem().getPrice() * newItem.getQuantity();
            newItem.setItemPrice(totalPrice);
            shoppingCartItemRepository.save(newItem);
        }
    }

    public void updateCartItem(Long userId, Long itemId, int quantity) {
        ShoppingCart cart = getOrCreateShoppingCart(userId);
        List<ShoppingCartItem> existingItems = shoppingCartItemRepository
                .findAllByShoppingCartIdAndItemId(cart.getId(), itemId);

        if (!existingItems.isEmpty()) {
            ShoppingCartItem item = existingItems.get(0);
            item.setQuantity(quantity);
            int totalPrice = item.getItem().getPrice() * quantity;
            item.setItemPrice(totalPrice);
            shoppingCartItemRepository.save(item);
        }
    }

    public void removeItemFromCart(Long userId, Long itemId) {
        ShoppingCart cart = getOrCreateShoppingCart(userId);
        List<ShoppingCartItem> existingItems = shoppingCartItemRepository
                .findAllByShoppingCartIdAndItemId(cart.getId(), itemId);

        if (!existingItems.isEmpty()) {
            ShoppingCartItem item = existingItems.get(0);
            shoppingCartItemRepository.delete(item);
        }
    }

    public void clearCart(Long userId) {
        ShoppingCart cart = getOrCreateShoppingCart(userId);
        shoppingCartItemRepository.deleteAllByShoppingCartId(cart.getId());
    }

    private ShoppingCart getOrCreateShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createShoppingCart(userId));
    }

    public ShoppingCart createShoppingCart(Long userId) {
        ShoppingCart cart = new ShoppingCart();
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        cart.setUser(user);
        return shoppingCartRepository.save(cart);
    }


}
