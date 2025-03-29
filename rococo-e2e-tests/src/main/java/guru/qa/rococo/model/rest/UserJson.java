package guru.qa.rococo.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.entity.userdata.UserDataEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("username") String username,
        @JsonProperty("firstname") String firstname,
        @JsonProperty("lastname") String lastname,
        @JsonProperty("image") String image,
        @JsonIgnore TestData testData
) {

    public UserJson(@Nonnull String username) {
        this(username, null);
    }

    public UserJson(@Nonnull String username, @Nullable TestData testData) {
        this(null, username, null, null, null, testData);
    }

    public UserJson addTestData(@Nonnull TestData testData) {
        return new UserJson(id, username, firstname, lastname, image, testData);
    }

    public static @Nonnull UserJson fromEntity(@Nonnull UserDataEntity entity) {
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getLastname(),
                entity.getImage() != null && entity.getImage().length > 0
                        ? new String(entity.getImage(), StandardCharsets.UTF_8)
                        : null,
                null
        );
    }
}
