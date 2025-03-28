package guru.qa.rococo.controller;

import guru.qa.rococo.controller.client.UserClient;
import guru.qa.rococo.exception.InvalidRequestException;
import guru.qa.rococo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @PatchMapping
    public User update(@AuthenticationPrincipal Jwt principal, @RequestBody User user) {
        log.info("User data update request {}", user);
        String username = principal.getClaim("sub");
        if (!username.equals(user.username())) {
            throw new InvalidRequestException("Username mismatch");
        }
        return client.update(user);
    }
}
