package qa.guru.rococo.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.rococo.jupiter.annotation.ApiLogin;
import qa.guru.rococo.jupiter.annotation.Token;
import qa.guru.rococo.jupiter.annotation.User;
import qa.guru.rococo.jupiter.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.extension.TestMethodContextExtension;

@ExtendWith({ApiLoginExtension.class, TestMethodContextExtension.class})
public class OAuthTest {

    @Test
    @ApiLogin
    void verifyBearerToken(@Token String token) {
        Assertions.assertNotNull(token);
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void verifyRegister(@Token String token) {
        Assertions.assertNotNull(token);
    }
}
