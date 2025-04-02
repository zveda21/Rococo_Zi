package guru.qa.rococo.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.model.utils.StringAsBytes;
import jakarta.annotation.Nonnull;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record ArtistJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name,
        @JsonProperty("biography")
        String biography,
        @JsonProperty("photo")
        String photo) {

    public static @Nonnull ArtistJson fromEntity(@Nonnull ArtistEntity entity) {
        return new ArtistJson(
                entity.getId(),
                entity.getName(),
                entity.getBiography(),
                entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null
        );
    }

    public @Nonnull ArtistEntity toEntity() {
        ArtistEntity entity = new ArtistEntity();
        entity.setName(name);
        entity.setBiography(biography);
        entity.setPhoto(
                new StringAsBytes(
                        photo
                ).bytes()
        );
        return entity;
    }
}
