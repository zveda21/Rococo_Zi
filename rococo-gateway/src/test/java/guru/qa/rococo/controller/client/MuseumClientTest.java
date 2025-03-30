package guru.qa.rococo.controller.client;

import guru.qa.rococo.controller.Constants;
import guru.qa.rococo.exception.RemoteServerException;
import guru.qa.rococo.model.Museum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class MuseumClientTest {

    @SuppressWarnings("unused")
    @Value("${rococo-museum.base-uri}")
    private String museumBaseUri;

    @SuppressWarnings("unused")
    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings("unused")
    @Autowired
    private MuseumClient museumClient;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnAllMuseumList() {
        final String requestUrl = museumBaseUri + "/internal/museum";
        final String response = Constants.getResponse(new ClassPathResource("response/internal/museum/get-all.json"));

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        List<Museum> museums = museumClient.getAll(null);

        mockServer.verify();
        assertThat(museums).hasSize(2);
        assertThat(museums.get(0).title()).isEqualTo("Museum of Modern Art");
        assertThat(museums.get(1).title()).isEqualTo("Louvre Museum");
    }

    @Test
    void shouldThrowExceptionWhenServiceIsDown() {
        final String requestUrl = museumBaseUri + "/internal/museum";

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        assertThatThrownBy(
                () -> museumClient.getAll(null))
                .isInstanceOf(RemoteServerException.class);
        mockServer.verify();
    }

    @Test
    void shouldReturnOneMuseumById() {
        final String id = Constants.Museum.ID_1;
        final String requestUrl = museumBaseUri + "/internal/museum/" + id;
        final String response = Constants.getResponse(new ClassPathResource("response/internal/museum/get-one.json"));

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Museum museum = museumClient.getById(UUID.fromString(id));

        mockServer.verify();
        assertThat(museum).isNotNull();
        assertThat(museum.title()).isEqualTo("Museum of Modern Art");
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        final String id = UUID.randomUUID().toString();
        final String requestUrl = museumBaseUri + "/internal/museum/" + id;

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(
                () -> museumClient.getById(UUID.fromString(id)))
                .isInstanceOf(RemoteServerException.class);
        mockServer.verify();
    }

    @Test
    void shouldUpdateMuseum() {
        final String id = Constants.Museum.ID_1;
        final String requestUrl = museumBaseUri + "/internal/museum";
        final String response = Constants.getResponse(new ClassPathResource("response/internal/museum/update.json"));

        mockServer.expect(
                        requestTo(requestUrl))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Museum museum = new Museum(UUID.fromString(id),
                "Museum of Modern Art and Science",
                "A museum dedicated to modern art and science, showcasing contemporary works and innovative scientific exhibits.",
                "data:image/jpeg;base64,...",
                null);
        Museum updatedMuseum = museumClient.update(museum);

        mockServer.verify();
        assertThat(updatedMuseum).isNotNull();
        assertThat(updatedMuseum.title()).isEqualTo("Museum of Modern Art and Science");
        assertThat(updatedMuseum.description()).isEqualTo("A museum dedicated to modern art and science, showcasing contemporary works and innovative scientific exhibits.");
    }
}
