package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.security.principal.UserPrincipal;
import at.technikum.Invicrea2WebShopbackend.service.ShoppingCartService;
import at.technikum.Invicrea2WebShopbackend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller für die Verwaltung von Warenkörben. */
@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    /** Ruft die Artikel im Warenkorb des eingeloggten Nutzers ab. */
    @GetMapping
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'read')")
    public List<ShoppingCartItem> getItemsInCart(@AuthenticationPrincipal UserPrincipal principal) {
        return shoppingCartService.getItemsInCartByShoppingCartId(principal.getId());
    }

    /** Fügt einen Artikel zum Warenkorb des eingeloggten Nutzers hinzu. */
    @PostMapping()
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'write')")
    public void addItemToCart(@AuthenticationPrincipal UserPrincipal principal,
                              @RequestParam Long itemId,
                              @RequestParam int quantity) {
        shoppingCartService.addItemToCart(principal.getId(), itemId, quantity);
    }

    /** Aktualisiert die Menge eines Artikels im Warenkorb des eingeloggten Nutzers. */
   /* @PutMapping()
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'write')")
    public void updateCartItem(@AuthenticationPrincipal UserPrincipal principal,
                               @RequestParam Long itemId,
                               @RequestParam int quantity) {
        shoppingCartService.updateCartItem(principal.getId(), itemId, quantity);
    }*/

    @PutMapping()
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'write')")
    public void updateCartItem(@AuthenticationPrincipal UserPrincipal principal,
                               @RequestParam Long itemId,
                               @RequestParam int quantity) {
        if (quantity == 0) {
            // If quantity is 0, remove the item from the cart
            shoppingCartService.removeItemFromCart(principal.getId(), itemId);
        } else {
            // Otherwise, update the item quantity
            shoppingCartService.updateCartItem(principal.getId(), itemId, quantity);
        }
    }


    /** Entfernt einen Artikel aus dem Warenkorb des eingeloggten Nutzers. */
    /*@DeleteMapping("/remove")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'write')")
    public void removeItemFromCart(@AuthenticationPrincipal UserPrincipal principal,
                                   @RequestParam Long itemId) {
        shoppingCartService.removeItemFromCart(principal.getId(), itemId);
    }*/

    /** Leert den Warenkorb des eingeloggten Nutzers. */
    @DeleteMapping()
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'write')")
    public void clearCart(@AuthenticationPrincipal UserPrincipal principal) {
        shoppingCartService.clearCart(principal.getId());
    }
}
