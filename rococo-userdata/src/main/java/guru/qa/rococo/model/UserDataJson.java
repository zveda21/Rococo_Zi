package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.UserDataEntity;
import jakarta.annotation.Nonnull;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDataJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("lastname")
        String lastname,
        @JsonProperty("image")
        String image) {
    public static @Nonnull UserDataJson fromEntity(@Nonnull UserDataEntity entity) {
        return new UserDataJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getLastname(),
                entity.getImage() != null && entity.getImage().length > 0 ? new String(entity.getImage(), StandardCharsets.UTF_8) : null
        );
    }
}
