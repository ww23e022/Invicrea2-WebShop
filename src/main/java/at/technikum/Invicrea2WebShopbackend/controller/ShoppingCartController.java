package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.AccountDto;
import at.technikum.Invicrea2WebShopbackend.model.Account;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCart;
import at.technikum.Invicrea2WebShopbackend.model.ShoppingCartItem;
import at.technikum.Invicrea2WebShopbackend.service.AccountService;
import at.technikum.Invicrea2WebShopbackend.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/create")
    public ResponseEntity<?> createShoppingCart(@RequestBody AccountDto accountDto) {
        // Überprüfen, ob das Konto existiert
        Account account = accountService.findById(accountDto.getId());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        // Das Konto existiert, also erstellen Sie den Einkaufswagen
        ShoppingCart shoppingCart = shoppingCartService.createShoppingCart(account);
        return ResponseEntity.ok(shoppingCart);
    }

    @GetMapping("/{accountId}")
    public Optional<ShoppingCart> getShoppingCartByAccountId( @PathVariable Long accountId) {
        return shoppingCartService.getShoppingCartByAccountId(accountId);
    }

    @GetMapping("/{shoppingCartId}/items")
    public List<ShoppingCartItem> getItemsInCart( @PathVariable Long shoppingCartId) {
        return shoppingCartService.getItemsInCart(shoppingCartId);
    }

    @PostMapping("/{shoppingCartId}/add-item/{itemId}")
    public void addItemToCart(@PathVariable Long shoppingCartId,
                              @PathVariable Long itemId,
                              @RequestParam int quantity) {
        shoppingCartService.addItemToCart(shoppingCartId, itemId, quantity);
    }

    @DeleteMapping("/{shoppingCartId}/remove-item/{itemId}")
    public void removeItemFromCart(@PathVariable Long shoppingCartId,
                                   @PathVariable Long itemId) {
        shoppingCartService.removeItemFromCart(shoppingCartId, itemId);
    }

    @DeleteMapping("/{shoppingCartId}/clear")
    public void clearCart(@PathVariable Long shoppingCartId) {
        shoppingCartService.clearCart(shoppingCartId);
    }
}
