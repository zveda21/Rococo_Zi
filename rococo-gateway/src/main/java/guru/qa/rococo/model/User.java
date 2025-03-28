package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record User(
        UUID id,
        String username,
        String firstname,
        String lastname,
        @JsonProperty("avatar")
        String image) {
}