package qa.guru.rococo.test.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import qa.guru.rococo.jupiter.annotation.ApiLogin;
import qa.guru.rococo.jupiter.annotation.Token;
import qa.guru.rococo.jupiter.annotation.User;
import qa.guru.rococo.jupiter.annotation.meta.RestTest;
import qa.guru.rococo.jupiter.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.extension.TestMethodContextExtension;
import qa.guru.rococo.model.rest.UserJson;

import static org.junit.jupiter.api.Assertions.*;

@RestTest
public class OAuthTest {

    @RegisterExtension
    @SuppressWarnings("unused")
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void shouldGetTokenWithDefaultUser(@Token String token) {
        assertNotNull(token);
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void shouldGetTokenWhenUserIsRegistered(@Token String token) {
        assertNotNull(token);
    }

    @Test
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void shouldGetUserWhenUserIsRegistered(@User UserJson user) {
        assertNotNull(user);
        assertNotNull(user.username());
        assertFalse(user.username().isEmpty());
    }
}
