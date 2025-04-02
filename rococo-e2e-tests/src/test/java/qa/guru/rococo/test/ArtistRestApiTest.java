package qa.guru.rococo.test;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.data.domain.Page;
import qa.guru.rococo.api.RococoApiClient;
import qa.guru.rococo.jupiter.annotation.ApiLogin;
import qa.guru.rococo.jupiter.annotation.Token;
import qa.guru.rococo.jupiter.annotation.User;
import qa.guru.rococo.jupiter.annotation.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.annotation.extension.TestMethodContextExtension;
import qa.guru.rococo.jupiter.annotation.meta.RestTest;
import qa.guru.rococo.model.rest.Artist;

import static org.junit.jupiter.api.Assertions.*;

@RestTest
public class ArtistRestApiTest {

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
    void getAllArtists(@Token String token) {
        Page<Artist> artists = client.getArtists(token);
        assertNotNull(artists);
        assertFalse(artists.isEmpty());
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getArtistsWithSize(@Token String token) {
        Page<Artist> artists = client.getArtists(token, 1);
        assertNotNull(artists);
        assertFalse(artists.isEmpty());
        assertEquals(1, artists.getTotalElements());
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getArtistsWithNameSearch(@Token String token) {
        final String name = "vincent";
        Page<Artist> artists = client.getArtists(token, 1);
        assertNotNull(artists);
        assertTrue(
                artists.getContent()
                        .stream().allMatch(
                                artist -> artist.name().toLowerCase().contains(name)));
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getArtistById(@Token String token) {
        final String name = faker.name().fullName();
        var artist = new Artist(null, name, faker.lorem().sentence(), "");
        var createdArtist = client.createArtist(token, artist);

        var id = createdArtist.id().toString();
        artist = client.getArtistById(token, id);
        assertNotNull(artist);
        assertEquals(
                createdArtist.name(),
                artist.name(),
                "Artist name should be the same");
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void createArtist(@Token String token) {
        final String name = faker.name().fullName();
        var artist = new Artist(null, name, faker.lorem().sentence(), "");
        var createdArtist = client.createArtist(token, artist);
        assertNotNull(
                createdArtist.id(),
                "Created artist ID should not be null");
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void updateArtist(@Token String token) {
        final String name = faker.name().fullName();
        var artist = new Artist(null, name, faker.lorem().sentence(), "");
        var createdArtist = client.createArtist(token, artist);
        assertNotNull(
                createdArtist.id(),
                "Created artist ID should not be null");

        final String updated = "UPDATED " + name;
        var body = new Artist(createdArtist.id(), updated, createdArtist.biography(), createdArtist.photo());
        var updatedArtist = client.updateArtist(token, body);

        assertNotNull(updatedArtist, "Response should not be null");
        assertEquals(updated, updatedArtist.name(), "Artist name should be updated");
    }
}
