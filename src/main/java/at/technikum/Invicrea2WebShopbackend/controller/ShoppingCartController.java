package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.ShoppingCartService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller für die Verwaltung von Warenkörben. */
@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final AccountService accountService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  AccountService accountService) {
        this.shoppingCartService = shoppingCartService;
        this.accountService = accountService;
    }

    /** Ruft die Artikel im Warenkorb anhand der Account-ID ab. */
    @GetMapping("/{accountId}")
    public List<ShoppingCartItem> getItemsInCartByAccountId(@PathVariable Long accountId) {
        return shoppingCartService.getItemsInCartByAccountId(accountId);
    }

    /** Fügt einen Artikel zum Warenkorb hinzu. */
    @PostMapping("/{shoppingCartId}/{itemId}/add-item")
    public void addItemToCart(@PathVariable Long shoppingCartId,
                              @PathVariable Long itemId,
                              @RequestParam int quantity) {
        shoppingCartService.addItemToCart(shoppingCartId, itemId, quantity);
    }

    /** Aktualisiert die Menge eines Artikels im Warenkorb. */
    @PutMapping("/{shoppingCartId}/{itemId}/update-item")
    public void updateCartItem(@PathVariable Long shoppingCartId,
                               @PathVariable Long itemId,
                               @RequestParam int quantity) {
        shoppingCartService.updateCartItem(shoppingCartId, itemId, quantity);
    }


    /** Entfernt einen Artikel aus dem Warenkorb. */
    @DeleteMapping("/{shoppingCartId}/{itemId}/remove-item")
    public void removeItemFromCart(@PathVariable Long shoppingCartId,
                                   @PathVariable Long itemId) {
        shoppingCartService.removeItemFromCart(shoppingCartId, itemId);
    }

    /** Leert den Warenkorb. */
    @DeleteMapping("/{shoppingCartId}")
    public void clearCart(@PathVariable Long shoppingCartId) {
        shoppingCartService.clearCart(shoppingCartId);
    }
}
