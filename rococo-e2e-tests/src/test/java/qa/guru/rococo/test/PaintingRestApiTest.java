package qa.guru.rococo.test;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
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
import qa.guru.rococo.model.rest.Artist;
import qa.guru.rococo.model.rest.Museum;
import qa.guru.rococo.model.rest.Painting;

import static org.junit.jupiter.api.Assertions.*;

@RestTest
public class PaintingRestApiTest {
    @RegisterExtension
    @SuppressWarnings("unused")
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

    private static RococoApiClient client;
    private static Faker faker;

    private Museum museum;

    @BeforeAll
    static void setUp() {
        client = new RococoApiClient();
        faker = new Faker();
    }

    Artist getArtist(String token) {
        return client.getArtists(token)
                .stream()
                .findAny()
                .orElseThrow(() -> new AssertionError("No artists found"));
    }

    Artist createArtist(String token) {
        var anotherArtist = new Artist(null, faker.name().fullName(), faker.lorem().sentence(), "");
        return client.createArtist(token, anotherArtist);
    }

    Museum getMuseum(String token) {
        return client.getMuseums(token, null)
                .stream()
                .findAny()
                .orElseThrow(() -> new AssertionError("No museums found"));
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void paintingGetApiTest(@Token String token) {
        var artist = getArtist(token);
        museum = getMuseum(token);

        // Create a painting
        var paintingTitle = "The painting of " + artist.name() + " " + faker.number().randomDigit();
        var painting = new Painting(null, paintingTitle, faker.lorem().sentence(), "",
                null, artist,
                null, museum);

        var createdPainting = client.createPainting(token, painting);
        assertNotNull(
                createdPainting,
                "Created painting should not be null");
        assertNotNull(
                createdPainting.id(),
                "Created painting ID should not be null");
        assertEquals(
                paintingTitle,
                createdPainting.title(),
                "Painting title should be the same");
        assertEquals(
                artist.id(),
                createdPainting.artist().id(),
                "Artist ID should be the same");
        assertEquals(
                museum.id(),
                createdPainting.museum().id(),
                "Museum ID should be the same");

        // Get all paintings
        var paintings = client.getPaintings(token);
        assertNotNull(paintings);
        assertFalse(paintings.isEmpty());
        assertTrue(
                paintings.getContent().stream().anyMatch(a -> a.id().equals(createdPainting.id())),
                "Created painting should be in the list");

        // Get painting by created id
        var id = createdPainting.id().toString();
        painting = client.getPaintingById(token, id);
        assertNotNull(painting);
        assertEquals(
                createdPainting.title(),
                painting.title(),
                "Painting title should be the same");
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void paintingUpdateApiTest(@Token String token) {
        var artist = createArtist(token);
        museum = getMuseum(token);

        // Create a painting
        var paintingTitle = "The painting of " + artist.name() + " " + faker.number().randomDigit();
        var painting = new Painting(null, paintingTitle, faker.lorem().sentence(), "",
                null, artist,
                null, museum);

        var createdPainting = client.createPainting(token, painting);

        // Get paintings by previous artist id
        var paintings = client.getPaintingByArtistId(token, artist.id().toString());
        assertTrue(
                paintings.getContent().stream().anyMatch(a -> a.id().equals(createdPainting.id())),
                "Created painting should be in the list");

        // Create a new artist
        var anotherArtist = createArtist(token);

        // Update painting
        var updatedTitle = "Updated title " + paintingTitle;
        var updatedDescription = "Updated description " + faker.lorem().sentence();
        var updatedPainting = new Painting(
                createdPainting.id(),
                updatedTitle,
                updatedDescription,
                "",
                null,
                anotherArtist,
                null,
                museum);
        Painting updatedPaintingResponse = client.updatePainting(token, updatedPainting);
        assertNotNull(updatedPaintingResponse);
        assertEquals(
                updatedTitle,
                updatedPaintingResponse.title(),
                "Painting title should be updated");
        assertEquals(
                updatedDescription,
                updatedPaintingResponse.description(),
                "Painting description should be updated");

        // Get paintings by previous artist id - no longer exists
        try {
            client.getPaintingByArtistId(token, artist.id().toString());
            Assertions.fail("Created painting should not be in the list");
        } catch (Exception e) {
            // pass
        }

        // Get paintings by updated artist id
        paintings = client.getPaintingByArtistId(token, anotherArtist.id().toString());
        assertTrue(
                paintings.getContent().stream().anyMatch(a -> a.id().equals(createdPainting.id())),
                "Created painting should be in the list");
    }
}
