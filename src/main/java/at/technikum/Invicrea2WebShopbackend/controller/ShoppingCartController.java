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

    // Constructor for injecting ShoppingCartService and AccountService
    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  AccountService accountService) {
        this.shoppingCartService = shoppingCartService;
        this.accountService = accountService;
    }

    // Handler for POST requests on "/shopping-cart/create" creates a new shopping cart
    @PostMapping("/create")
    public ResponseEntity<?> createShoppingCart(@RequestBody AccountDto accountDto) {
        // Check if the account exists
        Account account = accountService.findById(accountDto.getId());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        // The account exists, so create the shopping cart
        ShoppingCart shoppingCart = shoppingCartService.createShoppingCart(account);
        return ResponseEntity.ok(shoppingCart);
    }

    // Handler for GET requests on "/shopping-cart/{accountId}"
    // returns the shopping cart by account ID
    @GetMapping("/{accountId}")
    public Optional<ShoppingCart> getShoppingCartByAccountId( @PathVariable Long accountId) {
        return shoppingCartService.getShoppingCartByAccountId(accountId);
    }

    // Handler for GET requests on "/shopping-cart/{shoppingCartId}/items"
    // returns items in the cart
    @GetMapping("/{shoppingCartId}/items")
    public List<ShoppingCartItem> getItemsInCart( @PathVariable Long shoppingCartId) {
        return shoppingCartService.getItemsInCart(shoppingCartId);
    }

    // Handler for POST requests on "/shopping-cart/{shoppingCartId}/add-item/{itemId}"
    // adds an item to the cart
    @PostMapping("/{shoppingCartId}/add-item/{itemId}")
    public void addItemToCart(@PathVariable Long shoppingCartId,
                              @PathVariable Long itemId,
                              @RequestParam int quantity) {
        shoppingCartService.addItemToCart(shoppingCartId, itemId, quantity);
    }

    // Handler for DELETE requests on "/shopping-cart/{shoppingCartId}/remove-item/{itemId}"
    // removes an item from the cart
    @DeleteMapping("/{shoppingCartId}/remove-item/{itemId}")
    public void removeItemFromCart(@PathVariable Long shoppingCartId,
                                   @PathVariable Long itemId) {
        shoppingCartService.removeItemFromCart(shoppingCartId, itemId);
    }

    // Handler for DELETE requests on "/shopping-cart/{shoppingCartId}/clear" clears the cart
    @DeleteMapping("/{shoppingCartId}/clear")
    public void clearCart(@PathVariable Long shoppingCartId) {
        shoppingCartService.clearCart(shoppingCartId);
    }
}