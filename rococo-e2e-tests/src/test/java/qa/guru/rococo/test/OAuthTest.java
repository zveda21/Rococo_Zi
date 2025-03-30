package qa.guru.rococo.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.rococo.jupiter.annotation.Token;
import qa.guru.rococo.jupiter.annotation.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.annotation.extension.TestMethodContextExtension;

@ExtendWith({ApiLoginExtension.class, TestMethodContextExtension.class})
public class OAuthTest {

    @Test
    void verifyBearerToken(@Token String token) {
        Assertions.assertNotNull(token);
    }
}
