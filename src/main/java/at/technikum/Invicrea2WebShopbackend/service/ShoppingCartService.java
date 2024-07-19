package at.technikum.Invicrea2WebShopbackend.service;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.repository.ItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartItemRepository;
import at.technikum.Invicrea2WebShopbackend.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<ShoppingCartItem> getItemsInCartByShoppingCartId(Long shoppingCartId) {
        List<ShoppingCartItem> cartItems =
                shoppingCartItemRepository.
                        findAllByShoppingCartId(shoppingCartId);

        List<ShoppingCartItem> cartItemInfos = new ArrayList<>();
        int totalPrice = 0;

        for (ShoppingCartItem cartItem : cartItems) {
            ShoppingCartItem itemInfo = new ShoppingCartItem();
            itemInfo.setItemName(cartItem.getItemName());
            itemInfo.setQuantity(cartItem.getQuantity());
            itemInfo.setItemPrice(cartItem.getItemPrice());
            totalPrice += cartItem.getItemPrice();
            cartItemInfos.add(itemInfo);
        }

        // Add total price to a dummy item for convenience
        ShoppingCartItem totalItem = new ShoppingCartItem();
        totalItem.setItemName("Total Price");
        totalItem.setItemPrice(totalPrice);
        cartItemInfos.add(totalItem);

        return cartItemInfos;
    }

    public void addItemToCart(Long shoppingCartId, Long itemId, int quantity) {
        Optional<ShoppingCartItem> existingItem =
                shoppingCartItemRepository.
                        findByShoppingCartIdAndItemId(shoppingCartId, itemId);

        if (existingItem.isPresent()) {
            ShoppingCartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setItemName(item.getItem().getName());
            int totalPrice = item.getItem().getPrice() * item.getQuantity();
            item.setItemPrice(totalPrice); // Setzen des Gesamtpreises
            shoppingCartItemRepository.save(item);
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setShoppingCart(shoppingCartRepository.getById(shoppingCartId));
            newItem.setItem(itemRepository.getById(itemId));
            newItem.setQuantity(quantity);
            newItem.setItemName(newItem.getItem().getName());
            int totalPrice = newItem.getItem().getPrice() * newItem.getQuantity();
            newItem.setItemPrice(totalPrice); // Setzen des Gesamtpreises
            shoppingCartItemRepository.save(newItem);
        }
    }

    public void updateCartItem(Long shoppingCartId, Long itemId, int quantity) {
        Optional<ShoppingCartItem> existingItem =
                shoppingCartItemRepository.
                        findByShoppingCartIdAndItemId(shoppingCartId, itemId);

        existingItem.ifPresent(item -> {
            item.setQuantity(quantity);
            int totalPrice = item.getItem().getPrice() * quantity;
            item.setItemPrice(totalPrice);
            shoppingCartItemRepository.save(item);
        });
    }

    public void removeItemFromCart(Long shoppingCartId, Long itemId) {
        Optional<ShoppingCartItem> existingItem =
                shoppingCartItemRepository.
                        findByShoppingCartIdAndItemId(shoppingCartId, itemId);

        existingItem.ifPresent(shoppingCartItemRepository::delete);
    }

    public void clearCart(Long shoppingCartId) {
        shoppingCartItemRepository.deleteAllByShoppingCartId(shoppingCartId);
    }

    public ShoppingCart createShoppingCart(Long userId) {
        ShoppingCart cart = new ShoppingCart();
        User user = new User();
        user.setId(userId);
        cart.setUser(user);
        return shoppingCartRepository.save(cart);
    }
}
