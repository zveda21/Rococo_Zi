package guru.qa.rococo.model;

import guru.qa.rococo.data.MuseumEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record Museum(UUID id, String title, String description, String photo, GeoLocation geo) {
    public static Museum ofEntity(MuseumEntity entity) {
        return new Museum(
                entity.getId(), entity.getTitle(), entity.getDescription(),
                entity.getPhoto() != null && entity.getPhoto().length > 0 ?
                        new String(entity.getPhoto(), StandardCharsets.UTF_8) :
                        null,
                GeoLocation.ofEntity(entity.getGeo()));
    }
}
