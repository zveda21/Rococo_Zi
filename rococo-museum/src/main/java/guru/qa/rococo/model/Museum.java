package guru.qa.rococo.model;

import guru.qa.rococo.data.MuseumEntity;

import java.util.UUID;

public record Museum(UUID id, String title, GeoLocation geo) {
    public static Museum ofEntity(MuseumEntity entity) {
        return new Museum(entity.getId(), entity.getTitle(), GeoLocation.ofEntity(entity.getGeo()));
    }
}
