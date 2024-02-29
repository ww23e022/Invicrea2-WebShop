package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.ShoppingCartService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
REST-Controller für die Verwaltung von Warenkörben.
*/
@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final AccountService accountService;

    /**
    Konstruktor für die Injection von ShoppingCartService und AccountService.
    @param shoppingCartService Der ShoppingCartService.
    @param accountService      Der AccountService.
    */
    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  AccountService accountService) {
        this.shoppingCartService = shoppingCartService;
        this.accountService = accountService;
    }

    /**
     * Handler für GET-Anfragen auf "/shopping-cart/{accountId}".
     * Gibt den Warenkorb anhand der Kontonummer zurück.
     *
     * @param accountId Die ID des Kontos.
     * @return Der Warenkorb des Kontos, falls vorhanden.
     */
    @GetMapping("/{accountId}")
    public Optional<ShoppingCart> getShoppingCartByAccountId( @PathVariable Long accountId) {
        return shoppingCartService.getShoppingCartByAccountId(accountId);
    }

    /**
     * Für GET-Anfragen auf "/shopping-cart/{shoppingCartId}/items".
     * Gibt die Artikel im Warenkorb zurück.
     * @param shoppingCartId Die ID des Warenkorbs.
     * @return Eine Liste von Warenkorbinhalten.
     */
    @GetMapping("/{shoppingCartId}/items")
    public List<ShoppingCartItem> getItemsInCart( @PathVariable Long shoppingCartId) {
        return shoppingCartService.getItemsInCart(shoppingCartId);
    }

    /**
     * POST-Anfragen auf "/shopping-cart/{shoppingCartId}/add-item/{itemId}".
     * Fügt einen Artikel zum Warenkorb hinzu.
     *
     * @param shoppingCartId Die ID des Warenkorbs.
     * @param itemId         Die ID des hinzuzufügenden Artikels.
     * @param quantity       Die Menge des hinzuzufügenden Artikels.
     */
    @PostMapping("/{shoppingCartId}/add-item/{itemId}")
    public void addItemToCart(@PathVariable Long shoppingCartId,
                              @PathVariable Long itemId,
                              @RequestParam int quantity) {
        shoppingCartService.addItemToCart(shoppingCartId, itemId, quantity);
    }

    /**
     * PUT-Anfragen auf "/shopping-cart/{shoppingCartId}/update-item/{itemId}".
     * Aktualisiert die Menge eines Artikels im Warenkorb.
     *
     * @param shoppingCartId Die ID des Warenkorbs.
     * @param itemId         Die ID des zu aktualisierenden Artikels.
     * @param quantity       Die neue Menge des Artikels.
     */
    @PutMapping("/{shoppingCartId}/update-item/{itemId}")
    public void updateCartItem(@PathVariable Long shoppingCartId,
                               @PathVariable Long itemId,
                               @RequestParam int quantity) {
        shoppingCartService.updateCartItem(shoppingCartId, itemId, quantity);
    }


    /**
     * DELETE-Anfragen auf "/shopping-cart/{shoppingCartId}/remove-item/{itemId}".
     * Entfernt einen Artikel aus dem Warenkorb.
     *
     * @param shoppingCartId Die ID des Warenkorbs.
     * @param itemId         Die ID des zu entfernenden Artikels.
     */
    @DeleteMapping("/{shoppingCartId}/remove-item/{itemId}")
    public void removeItemFromCart(@PathVariable Long shoppingCartId,
                                   @PathVariable Long itemId) {
        shoppingCartService.removeItemFromCart(shoppingCartId, itemId);
    }

    /**
     * DELETE-Anfragen auf "/shopping-cart/{shoppingCartId}/clear".
     * Leert den Warenkorb.
     *
     * @param shoppingCartId Die ID des zu leerenden Warenkorbs.
     */
    @DeleteMapping("/{shoppingCartId}/clear")
    public void clearCart(@PathVariable Long shoppingCartId) {
        shoppingCartService.clearCart(shoppingCartId);
    }
}