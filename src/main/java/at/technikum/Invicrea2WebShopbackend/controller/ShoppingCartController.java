package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.security.user.UserPrincipal;
import at.technikum.Invicrea2WebShopbackend.service.ShoppingCartService;
import at.technikum.Invicrea2WebShopbackend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller für die Verwaltung von Warenkörben. */
@RestController
@RequestMapping("/shopping-cart")
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
    @PostMapping("/add")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'write')")
    public void addItemToCart(@AuthenticationPrincipal UserPrincipal principal,
                              @RequestParam Long itemId,
                              @RequestParam int quantity) {
        shoppingCartService.addItemToCart(principal.getId(), itemId, quantity);
    }

    /** Aktualisiert die Menge eines Artikels im Warenkorb des eingeloggten Nutzers. */
    @PutMapping("/update")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'write')")
    public void updateCartItem(@AuthenticationPrincipal UserPrincipal principal,
                               @RequestParam Long itemId,
                               @RequestParam int quantity) {
        shoppingCartService.updateCartItem(principal.getId(), itemId, quantity);
    }

    /** Entfernt einen Artikel aus dem Warenkorb des eingeloggten Nutzers. */
    @DeleteMapping("/remove")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'write')")
    public void removeItemFromCart(@AuthenticationPrincipal UserPrincipal principal,
                                   @RequestParam Long itemId) {
        shoppingCartService.removeItemFromCart(principal.getId(), itemId);
    }

    /** Leert den Warenkorb des eingeloggten Nutzers. */
    @DeleteMapping("/clear")
    @PreAuthorize("hasPermission(#principal.id, " +
            "'at.technikum.Invicrea2WebShopbackend.model.ShoppingCart', 'write')")
    public void clearCart(@AuthenticationPrincipal UserPrincipal principal) {
        shoppingCartService.clearCart(principal.getId());
    }
}
