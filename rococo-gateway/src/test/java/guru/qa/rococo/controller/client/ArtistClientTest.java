package guru.qa.rococo.controller.client;

import guru.qa.rococo.controller.Constants;
import guru.qa.rococo.model.Artist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ArtistClientTest {

    @SuppressWarnings("unused")
    @Value("${rococo-artist.base-uri}")
    private String artistBaseUri;

    @SuppressWarnings("unused")
    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings("unused")
    @Autowired
    private ArtistClient artistClient;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnAllArtistList() throws IOException {
        final String requestUrl = artistBaseUri + "/internal/artist?page=0&size=10";
        final String response = Constants.getResponse(new ClassPathResource("response/internal/artist/get-all.json"));

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        List<Artist> artists = artistClient.getAll(Pageable.ofSize(10), null);

        mockServer.verify();
        assertThat(artists).hasSize(3);
        assertThat(artists.get(0).name()).isEqualTo("Vincent Vah Gogh");
        assertThat(artists.get(1).name()).isEqualTo("Pablo Picasso");
        assertThat(artists.get(2).name()).isEqualTo("Leonardo da Vinci");
    }

    @Test
    void shouldReturnArtistById() {
        final String id = Constants.Artist.ID_1;
        final String requestUrl = artistBaseUri + "/internal/artist/" + id;
        final String response = Constants.getResponse(new ClassPathResource("response/internal/artist/get-one.json"));

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Artist artist = artistClient.getById(UUID.fromString(id));

        mockServer.verify();
        assertThat(artist.name()).isEqualTo("Vincent Vah Gogh");
        assertThat(artist.biography()).isNotNull();
        assertThat(artist.photo()).isNotNull();
    }

    @Test
    void shouldCreateArtist() {
        final String requestUrl = artistBaseUri + "/internal/artist";
        final String response = Constants.getResponse(new ClassPathResource("response/internal/artist/create.json"));
        final Artist artist = new Artist(
                null,
                "Vincent Vah Gogh",
                "Vincent van Gogh was a Dutch post-impressionist painter who is among the most famous and influential figures in the history of Western art.",
                "data:image/jpeg;base64,..."
        );

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Artist createdArtist = artistClient.create(artist);

        mockServer.verify();
        assertThat(createdArtist.id()).isNotNull();
        assertThat(createdArtist.name()).isEqualTo(artist.name());
        assertThat(createdArtist.biography()).isEqualTo(artist.biography());
        assertThat(createdArtist.photo()).isNotNull();
    }

    @Test
    void shouldUpdateArtist() {
        final String id = Constants.Artist.ID_1;
        final String requestUrl = artistBaseUri + "/internal/artist";
        final String response = Constants.getResponse(new ClassPathResource("response/internal/artist/update.json"));
        final Artist artist = new Artist(
                UUID.fromString(id),
                "Vincent Vah Gogh - UPDATED",
                "Vincent van Gogh was a Dutch post-impressionist painter who is among the most famous and influential figures in the history of Western art.",
                "data:image/jpeg;base64,..."
        );

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Artist updatedArtist = artistClient.update(artist);

        mockServer.verify();
        assertThat(updatedArtist.id()).isEqualTo(artist.id());
        assertThat(updatedArtist.name()).isEqualTo(artist.name());
        assertThat(updatedArtist.biography()).isEqualTo(artist.biography());
        assertThat(updatedArtist.photo()).isNotNull();
    }
}
