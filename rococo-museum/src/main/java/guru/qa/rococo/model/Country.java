package guru.qa.rococo.model;

import guru.qa.rococo.data.CountryEntity;

import java.util.UUID;

public record Country(UUID id, String name) {
    static Country ofEntity(CountryEntity entity) {
        return new Country(entity.getId(), entity.getName());
    }
}
