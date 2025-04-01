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
    void artistApiTest(@Token String token) {
        final String name = faker.name().fullName();
        var artist = new Artist(null, name, faker.lorem().sentence(), "");
        var createdArtist = client.createArtist(token, artist);
        assertNotNull(
                createdArtist.id(),
                "Created artist ID should not be null");

        Page<Artist> artists = client.getArtists(token);
        assertNotNull(artists);
        assertFalse(artists.isEmpty());
        assertTrue(
                artists.getContent().stream().anyMatch(a -> a.id().equals(createdArtist.id())),
                "Created artist should be in the list");

        var id = createdArtist.id().toString();
        artist = client.getArtistById(token, id);
        assertNotNull(artist);
        assertEquals(
                createdArtist.name(),
                artist.name(),
                "Artist name should be the same");
    }
}
