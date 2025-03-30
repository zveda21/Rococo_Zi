package qa.guru.rococo.model.rest;


import java.util.UUID;

public record Painting(
        UUID id,
        String title,
        String description,
        String content,
        UUID artistId,
        Artist artist,
        UUID museumId,
        Museum museum) {
}