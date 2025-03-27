package guru.qa.rococo.model;

import guru.qa.rococo.data.UserEntity;

import java.util.UUID;

public record UserJson(
        UUID id,
        String username,
        String firstname,
        String lastname,
        String image) {

    public static UserJson fromEntity(UserEntity entity) {
        return new UserJson(entity.getId(), entity.getUsername(), "", "", "");
    }
}
