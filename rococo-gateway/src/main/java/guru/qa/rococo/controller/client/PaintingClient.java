package guru.qa.rococo.controller.client;

import guru.qa.rococo.exception.RemoteServerException;
import guru.qa.rococo.model.Painting;
import guru.qa.rococo.model.page.RestPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
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
        List<?> pageData = Optional.ofNullable(restTemplate.getForEntity(url, RestPage.class).getBody()).map(RestPage::getContent).orElse(List.of());
        return ClientUtils.convertPageToTypedList(pageData, Painting.class);
    }

    public Painting getById(UUID id) {
        final String url = this.url + "/" + id.toString();
        return restTemplate.getForEntity(url, Painting.class).getBody();
    }

    public List<Painting> getByArtist(UUID id) {
        Objects.requireNonNull(id);
        final String url = this.url + "/artist/" + id;
        List<?> pageData = Optional.ofNullable(restTemplate.getForEntity(url, RestPage.class).getBody()).map(RestPage::getContent).orElse(List.of());
        return ClientUtils.convertPageToTypedList(pageData, Painting.class);
    }

    public Painting update(Painting painting) {
        return restTemplate.patchForObject(url, getHttpEntity(painting), Painting.class);
    }

    public Painting create(Painting painting) {
        return restTemplate.postForEntity(url, getHttpEntity(painting), Painting.class).getBody();

    }

    private HttpEntity<Painting> getHttpEntity(Painting painting) {
        var copy = Painting.withArtistAndMuseum(painting, painting.artist(), painting.museum());
        return new HttpEntity<>(copy);
    }
}
