package qa.guru.rococo.test.api;

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

    Artist getArtist(String token, int index) {
        return client.getArtists(token).getContent().get(index);
    }

    Artist createArtist(String token) {
        var anotherArtist = new Artist(null, faker.name().fullName(), faker.lorem().sentence(), "");
        return client.createArtist(token, anotherArtist);
    }

    Museum getMuseum(String token, int index) {
        return client.getMuseums(token, null).getContent().get(index);
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getAllPaintings(@Token String token) {
        var paintings = client.getPaintings(token, null);

        assertNotNull(paintings);
        assertFalse(paintings.isEmpty());
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getPaintingsWithPageSize(@Token String token) {
        var paintings = client.getPaintings(token, 1);

        assertNotNull(paintings);
        assertFalse(paintings.isEmpty());
        assertEquals(1, paintings.getTotalElements());
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getPaintingsWithTitleSearch(@Token String token) {
        var searchTerm = client.getPaintings(token, null).getContent().getFirst().title().substring(1).toLowerCase();

        var paintings = client.getPaintings(token, searchTerm);

        assertNotNull(paintings);
        assertFalse(paintings.isEmpty());
        assertTrue(
                paintings.getContent()
                        .stream().allMatch(
                                item -> item.title().toLowerCase().contains(searchTerm)));
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getPaintingById(@Token String token) {
        Painting first = client.getPaintings(token, null).getContent().getFirst();
        var id = first.id().toString();

        var painting = client.getPaintingById(token, id);

        assertNotNull(painting);
        assertEquals(first.title(), painting.title());
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void getPaintingByArtistId(@Token String token) {
        Painting first = client.getPaintings(token, null).getContent().getFirst();

        // set artist
        var artist = getArtist(token, 0);
        var artistId = artist.id().toString();

        // update the first painting with artist
        var updated = new Painting(
                first.id(), first.title(), first.description(), first.content(), null, artist, null, getMuseum(token, 0));
        client.updatePainting(token, updated);

        var paintings = client.getPaintingByArtistId(token, artistId);

        assertTrue(paintings
                .getContent()
                .stream()
                .map(Painting::artist)
                .allMatch(artist::equals));
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void createPainting(@Token String token) {
        var artist = getArtist(token, 0);
        var museum = getMuseum(token, 0);

        // Create a painting
        var paintingTitle = "The painting of " + artist.name() + " " + faker.number().randomDigit();
        var painting = new Painting(null, paintingTitle, faker.lorem().sentence(), "",
                null, artist,
                null, museum);
        var createdPainting = client.createPainting(token, painting);

        // verify
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
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void updatePaintingTitleAndDescription(@Token String token) {
        Painting first = client.getPaintings(token, null).getContent().getFirst();
        var title = "Updated " + faker.artist().name() + " " + first.title();
        var description = " Updated " + faker.number() + " " + first.description();

        var body = new Painting(
                first.id(), title, description, first.content(),
                first.artistId(), first.artist(), first.museumId(), first.museum());
        var response = client.updatePainting(token, body);

        assertEquals(title, response.title());
        assertEquals(description, response.description());
        assertEquals(first.content(), response.content());
        assertEquals(first.artist(), response.artist());
        assertEquals(first.museum(), response.museum());
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void updatePaintingArtist(@Token String token) {
        var artist = getArtist(token, 0);
        var museum = getMuseum(token, 0);
        var paintingTitle = "The painting of " + artist.name() + " " + faker.number().randomDigit();
        var painting = new Painting(null, paintingTitle, faker.lorem().sentence(), "",
                null, artist,
                null, museum);
        painting = client.createPainting(token, painting);

        // update artist
        var newArtist = getArtist(token, 1);
        var body = new Painting(
                painting.id(), painting.title(), painting.description(), painting.content(),
                null, newArtist, null, null
        );
        var response = client.updatePainting(token, body);

        // verify
        assertEquals(paintingTitle, response.title());
        assertEquals(painting.description(), response.description());
        assertEquals(newArtist.id(), response.artist().id());
        assertEquals(museum.id(), response.museum().id());
    }

    @Test
    @ApiLogin
    @User
    @ExtendWith(TestMethodContextExtension.class)
    void updatePaintingMuseum(@Token String token) {
        var artist = getArtist(token, 0);
        var museum = getMuseum(token, 0);
        var paintingTitle = "The painting of " + artist.name() + " " + faker.number().randomDigit();
        var painting = new Painting(null, paintingTitle, faker.lorem().sentence(), "",
                null, artist,
                null, museum);
        painting = client.createPainting(token, painting);

        // update museum
        var newMuseum = getMuseum(token, 1);
        var body = new Painting(
                painting.id(), painting.title(), painting.description(), painting.content(),
                null, null, null, newMuseum
        );
        var response = client.updatePainting(token, body);

        // verify
        assertEquals(paintingTitle, response.title());
        assertEquals(painting.description(), response.description());
        assertEquals(artist.id(), response.artist().id());
        assertEquals(newMuseum.id(), response.museum().id());
    }
}
