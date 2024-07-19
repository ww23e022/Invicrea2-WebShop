package at.technikum.Invicrea2WebShopbackend.controller;

import at.technikum.Invicrea2WebShopbackend.dto.TokenResponse;
import at.technikum.Invicrea2WebShopbackend.dto.UserRegistrationRequest;
import at.technikum.Invicrea2WebShopbackend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final AuthService authService;

    public UserController ( AuthService authService ) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public TokenResponse register( @RequestBody @Valid
                                   UserRegistrationRequest registrationRequest) {
        return authService.register(registrationRequest);
    }
}
