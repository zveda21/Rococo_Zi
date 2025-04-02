package qa.guru.rococo.test;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import qa.guru.rococo.api.RococoApiClient;
import qa.guru.rococo.jupiter.annotation.ApiLogin;
import qa.guru.rococo.jupiter.annotation.Token;
import qa.guru.rococo.jupiter.annotation.User;
import qa.guru.rococo.jupiter.annotation.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.annotation.extension.TestMethodContextExtension;
import qa.guru.rococo.jupiter.annotation.meta.RestTest;
import qa.guru.rococo.model.rest.GeoLocation;
import qa.guru.rococo.model.rest.Museum;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RestTest
public class MuseumRestApiTest {
    @RegisterExtension
    @SuppressWarnings("unused")
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

    private static RococoApiClient client;
    private static Faker faker;

    @BeforeAll
    static void setUp() {
        client = new RococoApiClient();
        faker = new Faker();
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getAllMuseums(@Token String token) {
        var museums = client.getMuseums(token, null);

        assertNotNull(museums);
        assertFalse(museums.isEmpty());
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getMuseumsWithPageSize(@Token String token) {
        var museums = client.getMuseums(token, 1);

        assertNotNull(museums);
        assertFalse(museums.isEmpty());
        assertEquals(1, museums.getTotalElements());
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getMuseumsWithTitleSearch(@Token String token) {
        final var searchTerm = "muse";
        var museums = client.getMuseums(token, searchTerm);

        assertNotNull(museums);
        assertFalse(museums.isEmpty());
        assertTrue(
                museums.getContent()
                        .stream().allMatch(
                                item -> item.title().toLowerCase().contains(searchTerm)));
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getMuseumById(@Token String token) {
        var museum = client.getMuseums(token, null).getContent().getFirst();
        var id = museum.id().toString();

        var byId = client.getMuseumById(token, id);

        assertNotNull(museum);
        assertEquals(
                museum.title(),
                byId.title(),
                "Museum title should be the same");
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void createMuseum(@Token String token) {
        // Geolocation constants
        final var city = "Paris";
        final var expectedCountry = "France";

        // Create a museum
        final var museumTitle = "The museum of " + city + " " + UUID.randomUUID();
        var geoLocation = new GeoLocation(city, null);
        var museum = new Museum(null, museumTitle, faker.lorem().sentence(), "", geoLocation);
        var createdMuseum = client.createMuseum(token, museum);

        assertNotNull(
                createdMuseum.id(),
                "Created museum ID should not be null");
        assertEquals(
                expectedCountry,
                createdMuseum.geo().country().name(),
                "Museum country should be France");
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void updateMuseum(@Token String token) {
        var museum = client.getMuseums(token, null).getContent().getFirst();
        var updatedTitle = "UPDATED " + UUID.randomUUID() + " " + museum.title();

        var body = new Museum(
                museum.id(),
                updatedTitle,
                museum.description(),
                "",
                museum.geo());
        var response = client.updateMuseum(token, body);

        assertNotNull(response);
        assertEquals(
                updatedTitle,
                response.title(),
                "Museum title should be updated");
        assertEquals(
                museum.description(),
                response.description(),
                "Museum description should not be updated");
        assertEquals(
                museum.geo().city(),
                response.geo().city(),
                "Museum geo location should remain the same");
    }
}
