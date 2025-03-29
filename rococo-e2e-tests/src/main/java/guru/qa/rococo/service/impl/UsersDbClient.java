package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.auth.Authority;
import guru.qa.rococo.data.entity.auth.AuthorityEntity;
import guru.qa.rococo.data.entity.auth.UserEntity;
import guru.qa.rococo.model.rest.UserJson;
import guru.qa.rococo.service.UsersClient;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final String defaultPassword = "12345";
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @NotNull
    @Override
    @Step("Create user using SQL")
    public UserJson createUser(String username, String password) {
        return null;
    }

    @NotNull
    @Override
    public Optional<UserJson> findUserByUsername(String username) {
        return Optional.empty();
    }

    @Nonnull
    private UserEntity authUserEntity(String username, String password) {
        UserEntity authUser = new UserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }
}
