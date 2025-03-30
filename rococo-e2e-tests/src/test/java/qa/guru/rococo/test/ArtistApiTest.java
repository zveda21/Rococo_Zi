package qa.guru.rococo.test;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.data.domain.Page;
import qa.guru.rococo.api.RococoApiClient;
import qa.guru.rococo.jupiter.annotation.ApiLogin;
import qa.guru.rococo.jupiter.annotation.Token;
import qa.guru.rococo.jupiter.annotation.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.annotation.extension.TestMethodContextExtension;
import qa.guru.rococo.jupiter.annotation.meta.RestTest;
import qa.guru.rococo.model.rest.Artist;

@RestTest
public class ArtistApiTest {

    @RegisterExtension
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
    @ExtendWith(TestMethodContextExtension.class)
    void getArtists(@Token String token) {
        Page<Artist> artists = client.getArtists(token);
        Assertions.assertNotNull(artists);
        Assertions.assertFalse(artists.isEmpty());
    }

    @Test
    @ApiLogin
    @ExtendWith(TestMethodContextExtension.class)
    void createArtist(@Token String token) {
        Artist artist = new Artist(null, faker.name().fullName(), faker.lorem().sentence(), "");
        artist = client.createArtist(token, artist);
        Assertions.assertNotNull(artist.id());
    }
}
