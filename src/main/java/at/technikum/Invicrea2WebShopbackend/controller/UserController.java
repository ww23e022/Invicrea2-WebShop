package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.TokenResponse;
import at.technikum.Invicrea2WebShopbackend.dto.UserRegistrationRequest;
import at.technikum.Invicrea2WebShopbackend.model.User;
import at.technikum.Invicrea2WebShopbackend.service.AuthService;
import at.technikum.Invicrea2WebShopbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final AuthService authService;

    @Autowired
    private UserService userService;

    public UserController ( AuthService authService ) {
        this.authService = authService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        long userCount = userService.getUserCount();
        return ResponseEntity.ok(userCount);
    }

    @PostMapping("/register")
    public TokenResponse register( @RequestBody @Valid
                                   UserRegistrationRequest registrationRequest) {
        return authService.register(registrationRequest);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile( Principal principal) {
        String username = principal.getName();
        User userProfile = userService.getUserProfile(username);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(
            @RequestBody User updatedUser, Principal principal) {
        String username = principal.getName();
        try {
            userService.updateUserProfile(username, updatedUser);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/profile")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAnyUserProfile(@PathVariable Long id,
                                                  @RequestBody User updatedUser) {
        try {
            userService.updateUserProfileById(id, updatedUser);
            return ResponseEntity.ok("User profile updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
