package qa.guru.rococo.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.rococo.jupiter.annotation.ApiLogin;
import qa.guru.rococo.jupiter.annotation.Token;
import qa.guru.rococo.jupiter.annotation.User;
import qa.guru.rococo.jupiter.annotation.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.annotation.extension.TestMethodContextExtension;
import qa.guru.rococo.jupiter.annotation.extension.UserExtension;
import qa.guru.rococo.model.rest.UserJson;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({ApiLoginExtension.class, TestMethodContextExtension.class, UserExtension.class})
public class OAuthTest {

    @Test
    @ApiLogin
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
    void shouldGetUserWhenUserIsRegistered(UserJson user) {
        assertNotNull(user);
        assertNotNull(user.username());
        assertFalse(user.username().isEmpty());
    }
}
