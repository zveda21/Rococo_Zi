package guru.qa.rococo.service;

import guru.qa.rococo.model.rest.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public interface UsersClient {

    @Nonnull
    UserJson createUser(String username, String password);

    @Nonnull
    Optional<UserJson> findUserByUsername(String username);

}
