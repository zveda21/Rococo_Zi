package guru.qa.rococo.controller.client;

import guru.qa.rococo.model.Artist;
import guru.qa.rococo.model.page.RestPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class ArtistClient {
    private final String url;
    private final RestTemplate restTemplate;

    public ArtistClient(RestTemplate restTemplate, @Value("${rococo-artist.base-uri}") String artistUri) {
        this.restTemplate = restTemplate;
        this.url = artistUri + "/internal/artist";
    }

    public List<Artist> getAll(Pageable pageable, String name) {
        Objects.requireNonNull(pageable, "Pageable cannot be null");
        String url = this.url + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
        if (name != null && !name.isBlank()) {
            url += "&name=" + name;
        }

        List<?> pageData = Optional.ofNullable(restTemplate.getForEntity(url, RestPage.class).getBody()).map(RestPage::getContent).orElse(List.of());
        return ClientUtils.convertPageToTypedList(pageData, Artist.class);
    }

    public Artist getById(UUID id) {
        final String url = this.url + "/" + id.toString();
        return restTemplate.getForEntity(url, Artist.class).getBody();
    }

    public Artist create(Artist artist) {
        return restTemplate.postForObject(url, new HttpEntity<>(artist), Artist.class);
    }

    public Artist update(Artist artist) {
        return restTemplate.patchForObject(url, new HttpEntity<>(artist), Artist.class);
    }
}
