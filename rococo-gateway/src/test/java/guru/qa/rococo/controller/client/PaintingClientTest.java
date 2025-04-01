package guru.qa.rococo.controller.client;

import guru.qa.rococo.controller.Constants;
import guru.qa.rococo.model.Painting;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class PaintingClientTest {

    @SuppressWarnings("unused")
    @Value("${rococo-painting.base-uri}")
    private String paintingBaseUri;

    @SuppressWarnings("unused")
    @Autowired
    private PaintingClient paintingClient;

    @SuppressWarnings("unused")
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnAllPaintingList() {
        final String requestUrl = paintingBaseUri + "/internal/painting?page=0&size=10";
        final String response = Constants.getResponse(new ClassPathResource("response/internal/painting/get-all.json"));

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        List<Painting> paintings = paintingClient.getAll(Pageable.ofSize(10), null);

        mockServer.verify();
        assertThat(paintings).isNotEmpty();
        assertThat(paintings).hasSize(2);
    }

    @Test
    void shouldReturnPaintingById() {
        final String id = Constants.Painting.ID_1;
        final String requestUrl = paintingBaseUri + "/internal/painting/" + id;
        final String response = Constants.getResponse(new ClassPathResource("response/internal/painting/get-one.json"));

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Painting painting = paintingClient.getById(UUID.fromString(id));

        mockServer.verify();
        assertThat(painting).isNotNull();
        assertThat(painting.id()).isEqualTo(UUID.fromString(id));
    }

    @Test
    void shouldReturnPaintingByArtist() {
        final String artistId =  Constants.Artist.ID_1;
        final String requestUrl = paintingBaseUri + "/internal/painting/artist/" + artistId;
        final String response = Constants.getResponse(new ClassPathResource("response/internal/painting/get-by-artist.json"));

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        List<Painting> paintings = paintingClient.getByArtist(UUID.fromString(artistId));

        mockServer.verify();
        assertThat(paintings).isNotEmpty();
        assertThat(paintings).hasSize(1);
    }

    @Test
    void shouldCreatePainting() {
        final String requestUrl = paintingBaseUri + "/internal/painting";
        final String response = Constants.getResponse(new ClassPathResource("response/internal/painting/create.json"));

        final UUID artistId = UUID.fromString(Constants.Artist.ID_1);
        final UUID museumId = UUID.fromString(Constants.Museum.ID_1);

        final Painting painting = new Painting(
                null,
                "Starry Night",
                "A painting by Vincent van Gogh, depicting a starry night sky over a small town.",
                "data:image/jpeg;base64,...",
                artistId, null,
                museumId, null
        );

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Painting createdPainting = paintingClient.create(painting);

        mockServer.verify();
        assertThat(createdPainting).isNotNull();
        assertThat(createdPainting.id().toString()).isEqualTo(Constants.Painting.ID_1);
        assertThat(createdPainting.title()).isEqualTo("Starry Night");
    }

    @Test
    void shouldUpdatePainting() {
        final String id = Constants.Painting.ID_1;
        final String requestUrl = paintingBaseUri + "/internal/painting";
        final String response = Constants.getResponse(new ClassPathResource("response/internal/painting/update.json"));

        final UUID artistId = UUID.fromString(Constants.Artist.ID_1);
        final UUID museumId = UUID.fromString(Constants.Museum.ID_1);

        final Painting painting = new Painting(
                UUID.fromString(id),
                "Starry Night - UPDATED",
                "A painting by Vincent van Gogh, depicting a starry night sky over a small town.",
                "data:image/jpeg;base64,...",
                artistId, null,
                museumId, null
        );

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Painting updatedPainting = paintingClient.update(painting);

        mockServer.verify();
        assertThat(updatedPainting).isNotNull();
        assertThat(updatedPainting.id().toString()).isEqualTo(id);
        assertThat(updatedPainting.title()).isEqualTo("Starry Night - UPDATED");
    }
}
