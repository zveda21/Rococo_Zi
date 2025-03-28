package guru.qa.rococo.controller;

import guru.qa.rococo.model.Session;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;


@RestController
@RequestMapping("/api/session")
public class SessionController {

    @GetMapping
    public Session session(@AuthenticationPrincipal Jwt principal) {
        if (principal != null) {
            return new Session(
                    principal.getClaim("sub"),
                    Date.from(Objects.requireNonNullElse(principal.getIssuedAt(), Instant.EPOCH)),
                    Date.from(Objects.requireNonNullElse(principal.getExpiresAt(), Instant.EPOCH)));
        } else {
            return Session.empty();
        }
    }
}
