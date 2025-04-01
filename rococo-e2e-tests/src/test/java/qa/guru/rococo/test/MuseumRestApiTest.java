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
import qa.guru.rococo.jupiter.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.extension.TestMethodContextExtension;
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
    void museumApiTest(@Token String token) {
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

        // Get all museums
        var museums = client.getMuseums(token, null);
        assertNotNull(museums);
        assertFalse(museums.isEmpty());
        assertTrue(
                museums.getContent().stream().anyMatch(a -> a.id().equals(createdMuseum.id())),
                "Created museum should be in the list");

        // Get museum by created id
        var id = createdMuseum.id().toString();
        museum = client.getMuseumById(token, id);
        assertNotNull(museum);
        assertEquals(
                createdMuseum.title(),
                museum.title(),
                "Museum title should be the same");

        // Get museum by title
        var museumsSearch = client.getMuseums(token, museumTitle);
        assertNotNull(museumsSearch);
        assertEquals(1, museumsSearch.getTotalElements(), "Should find one museum");
        assertTrue(
                museums.getContent().stream().anyMatch(a -> a.id().equals(createdMuseum.id())),
                "Created museum should be in the list");

        // Update museum
        var updatedTitle = "Updated title " + museumTitle;
        var updatedDescription = "Updated description " + faker.lorem().sentence();
        var updatedMuseum = new Museum(
                createdMuseum.id(),
                updatedTitle,
                updatedDescription,
                "",
                geoLocation);
        var updatedMuseumResponse = client.updateMuseum(token, updatedMuseum);
        assertNotNull(updatedMuseumResponse);
        assertEquals(
                updatedTitle,
                updatedMuseumResponse.title(),
                "Museum title should be updated");
        assertEquals(
                updatedDescription,
                updatedMuseumResponse.description(),
                "Museum description should be updated");
        assertEquals(
                city,
                updatedMuseumResponse.geo().city(),
                "Museum geo location should remain the same");
    }
}
