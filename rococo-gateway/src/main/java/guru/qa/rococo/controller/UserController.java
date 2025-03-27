package guru.qa.rococo.controller;

import guru.qa.rococo.controller.client.UserClient;
import guru.qa.rococo.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserClient client;

    public UserController(UserClient client) {
        this.client = client;
    }

    @GetMapping
    public User user(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return client.getByUsername(username);
    }
}
