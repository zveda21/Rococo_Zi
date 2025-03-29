package guru.qa.rococo.model;

import guru.qa.rococo.data.PaintingEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record Painting(
        UUID id,
        String title,
        String description,
        String content,
        UUID artistId,
        UUID museumId) {

    public static Painting ofEntity(PaintingEntity entity) {
        return new Painting(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getContent() != null && entity.getContent().length > 0 ?
                        new String(entity.getContent(), StandardCharsets.UTF_8) :
                        null,
                entity.getArtistId(),
                entity.getMuseumId());
    }
}
