package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    public static Painting withArtistAndMuseum(Painting painting, Artist artist, Museum museum) {
        return new Painting(
                painting.id(),
                painting.title(),
                painting.description(),
                painting.content(),
                Optional.ofNullable(artist).map(Artist::id).orElse(null),
                artist,
                Optional.ofNullable(museum).map(Museum::id).orElse(null),
                museum);
    }
}