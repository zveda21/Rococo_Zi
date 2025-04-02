package qa.guru.rococo.model.rest;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.UUID;

public record UserJson(
        UUID id,
        String username,
        String firstname,
        String lastname,
        @JsonAlias("image")
        String avatar) {
}