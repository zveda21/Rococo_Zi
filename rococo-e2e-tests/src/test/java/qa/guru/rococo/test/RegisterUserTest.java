package qa.guru.rococo.test;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.rococo.api.AuthApiClient;
import qa.guru.rococo.jupiter.annotation.extension.TestMethodContextExtension;

public class RegisterUserTest {
    private static AuthApiClient authApiClient;
    private static Faker faker;

    @BeforeAll
    static void setUp() {
        authApiClient = new AuthApiClient();
        faker = new Faker();
    }

    @Test
    @ExtendWith(TestMethodContextExtension.class)
    void verifyRegister() {
        final String username = faker.name().username();
        final String password = faker.internet().password();
        authApiClient.register(username, password);
        String token = authApiClient.login(username, password);
        Assertions.assertNotNull(token);
    }
}
