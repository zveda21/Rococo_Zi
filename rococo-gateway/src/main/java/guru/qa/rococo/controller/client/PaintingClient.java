package guru.qa.rococo.controller.client;

import guru.qa.rococo.exception.NoResponseException;
import guru.qa.rococo.model.Painting;
import guru.qa.rococo.model.page.RestPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaintingClient {
    private final String url;
    private final RestTemplate restTemplate;

    public PaintingClient(@Value("${rococo-painting.base-uri}") String url, RestTemplate restTemplate) {
        this.url = url + "/internal/painting";
        this.restTemplate = restTemplate;
    }

    public List<Painting> getAll(String title) {
        final String url = this.url + (title != null ? "?title=" + title : "");
        try {
            List<?> pageData = Optional.ofNullable(restTemplate.getForEntity(url, RestPage.class).getBody()).map(RestPage::getContent).orElse(List.of());
            return ClientUtils.convertPageToTypedList(pageData, Painting.class);
        } catch (Exception e) {
            log.error("Error {}", e.getMessage(), e);
            throw new NoResponseException("No REST response in " + url, e);
        }
    }

    public Painting getById(UUID id) {
        final String url = this.url + "/" + id.toString();
        try {
            return restTemplate.getForEntity(url, Painting.class).getBody();
        } catch (Exception e) {
            log.error("Error {}", e.getMessage(), e);
            throw new NoResponseException("No REST response in " + url, e);
        }
    }

    public Painting update(Painting painting) {
        try {
            return restTemplate.patchForObject(url, getHttpEntity(painting), Painting.class);
        } catch (Exception e) {
            log.error("Error {}", e.getMessage(), e);
            throw new NoResponseException("No REST response in " + url, e);
        }
    }

    public Painting create(Painting painting) {
        try {
            return restTemplate.postForEntity(url, getHttpEntity(painting), Painting.class).getBody();
        } catch (Exception e) {
            log.error("Error {}", e.getMessage(), e);
            throw new NoResponseException("No REST response in " + url, e);
        }
    }

    private HttpEntity<Painting> getHttpEntity(Painting painting) {
        var copy = Painting.withArtistAndMuseum(painting, painting.artist(), painting.museum());
        return new HttpEntity<>(copy);
    }
}
