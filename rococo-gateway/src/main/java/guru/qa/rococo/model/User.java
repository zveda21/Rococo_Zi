package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.UUID;

public record User(
        UUID id,
        String username,
        String firstname,
        String lastname,
        @JsonAlias("image")
        String avatar) {
}