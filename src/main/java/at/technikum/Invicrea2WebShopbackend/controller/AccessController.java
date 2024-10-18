package at.technikum.Invicrea2WebShopbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessController {
    @GetMapping("/")
    public String hello() {
        return "Hello! This is public to everyone";
    }

    /*@GetMapping("/secured")
    public String secured(@AuthenticationPrincipal UserPrincipal principal) {
        return "This can only be seen by a logged-in user. Your Username is: " +
                principal.getUsername() + " your ID: " + principal.getId();
    }*/

    /*@GetMapping("/admin")
    public String admin(@AuthenticationPrincipal UserPrincipal principal) {
        return "If you see this, you are an admin. Your ID: " + principal.getId();
    }*/
}