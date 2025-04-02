package guru.qa.rococo.model;

import guru.qa.rococo.data.GeoEntity;

public record GeoLocation(String city, Country country) {
    public static GeoLocation ofEntity(GeoEntity entity) {
        return new GeoLocation(entity.getCity(), Country.ofEntity(entity.getCountry()));
    }
}
